<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="700px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add one click appointment</span>
                <span v-else class="headline">Update one click appointment</span>
            </v-card-title>
			<v-card-text>
				<v-container>
					<v-form ref="form" @submit.prevent="submit">
						<v-row justify="space-between" align="center">
							<v-col cols="12" sm="4">
								<v-datetime-picker
										label="Date and time"
										v-model="selectedDateTime"
										:date-picker-props="{min: tomorrow}"
										:time-picker-props="{format: '24h'}"
								>
									<template v-slot:dateIcon>
										<v-icon>
											mdi-calendar
										</v-icon>
									</template>
									<template v-slot:timeIcon>
										<v-icon>
											mdi-clock
										</v-icon>
									</template>
								</v-datetime-picker>
							</v-col>
							<v-col cols="12" sm="3">
								<v-text-field
										label="Duration"
										v-model="selectedDuration"
										suffix="minutes"
								></v-text-field>
							</v-col>
							<v-col cols="12" sm="5">
								<intervention-type-selection
									v-model="selectedInterventionType"
									:clinic-id="$route.params.clinicID"
								></intervention-type-selection>
							</v-col>
							<v-col cols="12" sm="6">
								<clinic-room-selection
										v-model="selectedClinicRoom"
										:clinic-id="$route.params.clinicID"
										:duration="selectedDuration"
										:date-time="selectedDateTime"
										:disabled="!selectedDuration || !selectedDateTime"
								></clinic-room-selection>
							</v-col>
							<v-col cols="12" sm="6">
								<doctor-selection
										v-model="selectedDoctor"
										:type-id="oneClick.typeID"
										:clinic-id="$route.params.clinicID"
										:duration="selectedDuration"
										:date-time="selectedDateTime"
										:disabled="!selectedDateTime || !selectedDuration || !selectedInterventionType"
								></doctor-selection>
							</v-col>
							<v-col cols="12">
								<span class="red--text darken-1" v-if="error.isError">{{ error.errorMessage }}</span>
							</v-col>
							<v-col cols="6">
								<v-btn @click="$emit('input', false)" text>Close</v-btn>
							</v-col>
							<v-col cols="6">
								<v-btn type="submit" text>Submit</v-btn>
							</v-col>
						</v-row>
					</v-form>
				</v-container>
			</v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
	import {mapActions, mapState} from "vuex";
	import {emptyOneClick} from "../../utils/skeletons";
	import ClinicRoomSelection from "../ClinicRooms/ClinicRoomSelection";
	import DoctorSelection from "../Doctor/DoctorSelection";
	import InterventionTypeSelection from "../InterventionType/InterventionTypeSelection";
	import {DayOfTheWeek} from "../../utils/DayOfTheWeek";
	import JSOG from "jsog";

	export default {
        name: "ModifyOneClickDialog",
        components: {ClinicRoomSelection, DoctorSelection, InterventionTypeSelection},
        data: () => ({
            oneClick: emptyOneClick,
			selectedDateTime: null,
			selectedDuration: null,
			selectedClinicRoom: null,
			selectedInterventionType: null,
			selectedDoctor: null,
			rules: {
            	required: v => !!v || 'Required.',
			},
			error: {
            	isError: false,
				errorMessage: ''
			}
        }),
        props: {
            value: {
            	type: Boolean,
				default: false
			},
            mode: {
                type: String,
                default: 'add'
            },
			editOneClick: null
        },
		computed: {
			...mapState('auth', ['role']),
			tomorrow() {
				let now = new Date();
				now.setDate(now.getDate() + 1);
				return now.toISOString().slice(0, -1);
			},
		},
        watch: {
            value() {
                if (this.editOneClick) {
                    this.oneClick = this.editOneClick;
                }
            },
			selectedClinicRoom(val) {
            	this.oneClick.clinicRoomID = val.id;
			},
			selectedInterventionType(val) {
            	this.oneClick.typeID = val.id;
			},
			selectedDoctor(val) {
            	this.oneClick.doctorID = val.id;
			}
        },
        methods: {
            ...mapActions('oneClicks', ['addOneClickApi', 'deleteOneClickApi', 'updateOneClickApi']),

            submit() {
            	let fun = this.mode === 'add' ? this.addOneClickApi : this.updateOneClickApi;
                if (this.validate()) {
					this.oneClick.clinicID = this.$route.params.clinicID;
					this.oneClick.dateTime ={
						start: new Date(this.selectedDateTime),
						end: new Date(this.selectedDateTime)
					};
					this.oneClick.dateTime.end.setMinutes(this.selectedDateTime.getMinutes() + Number(this.selectedDuration));

					this.oneClick.dateTime.start.setMinutes(this.oneClick.dateTime.start.getMinutes() - this.oneClick.dateTime.start.getTimezoneOffset());
					this.oneClick.dateTime.end.setMinutes(this.oneClick.dateTime.end.getMinutes() - this.oneClick.dateTime.end.getTimezoneOffset());

					fun(this.oneClick);
                    this.close();
                }
            },
			validate() {
            	if (this.selectedDuration < 30) {
					this.error = {
						isError: true,
						errorMessage: `Duration can't be less than 30 minutes.`
					};
					return false;
				}
            	let doctor = this.selectedDoctor;
            	let endOfSelected = new Date(this.selectedDateTime);
				endOfSelected.setMinutes(this.selectedDateTime.getMinutes() + this.selectedDuration);
            	if (!this.selectedDoctor.specialties.includes(this.selectedInterventionType.id)) {
            		this.error = {
            			isError: true,
						errorMessage: 'The selected doctor can not perform this type of intervention.'
					};
					return false;
				}
				let weekday = DayOfTheWeek[this.selectedDateTime.getDay()];
				let timeperiod = doctor.workingSchedule[weekday];
				if(!timeperiod) {
					this.doctorError.isError = true;
					this.doctorError.errorMessages = 'Doctor does not work on this day.';
					return false;
				}
				let start = this.parseTime(timeperiod.start);
				let startDateTime = new Date(this.selectedDateTime);
				startDateTime.setHours(start.hours, parseInt(start.minutes), start.seconds);
				if(this.selectedDateTime < startDateTime){
					this.doctorError.isError = true;
					this.doctorError.errorMessages = 'This is before the doctor starts workday.';
					return false;
				}
				let end = this.parseTime(timeperiod.end);
				let endDateTime = new Date(this.selectedDateTime);
				endDateTime.setHours(end.hours, parseInt(end.minutes), end.seconds);
				if(this.selectedDateTime > endDateTime){
					this.doctorError.isError = true;
					this.doctorError.errorMessages = 'This is after the doctor ends workday.';
					return false;
				}
				for(let timeperiod of doctor.busyTimes) {
					let startDateTime = new Date(timeperiod.start);
					let endDateTime = new Date(timeperiod.end);
					endDateTime.setHours(end.hours, end.minutes, end.seconds);

					if(this.selectedDateTime >= startDateTime && this.selectedDateTime <= endDateTime){
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'Doctor has an appointment during this time.';
						return false;
					}
					if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'The next intervention is too close to this one.';
						return false;
					}
				}
				for(let timeperiod of doctor.oneClickAppointments) {
					let startDateTime = new Date(timeperiod.start);
					let endDateTime = new Date(timeperiod.end);
					endDateTime.setHours(end.hours, end.minutes, end.seconds);

					if(this.selectedDateTime >= startDateTime && this.selectedDateTime <= endDateTime){
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'Doctor has an appointment during this time.';
						return false;
					}
					if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'The next intervention is too close to this one.';
						return false;
					}
				}
				for(let ar of doctor.appointmentRequests) {
					let time = ar.dateTime;
					let startDateTime = new Date(time);
					let endDateTime = new Date(startDateTime);
					endDateTime.setMinutes(startDateTime.getMinutes() + 30);

					if(this.selectedDateTime >= startDateTime && this.selectedDateTime <= endDateTime){
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'There is an appointment request for this time.';
						return false;
					}
					if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'The next appointment is too close to this one.';
						return false;
					}
				}

				for(let intervention of JSOG.decode(this.selectedClinicRoom.interventions)) {

					let startDateTime = new Date(intervention.dateTime.start);
					let endDateTime = new Date(intervention.dateTime.end);
					if(this.selectedDateTime >= startDateTime && this.selectedDateTime <= endDateTime){
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'Clinic room not available during this time';
						return false;
					}
					if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
						this.doctorError.isError = true;
						this.doctorError.errorMessages = 'Clinic room not available during this time';
						return false;
					}
				}
				return true;
			},
            close() {
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            },
			parseTime(time) {
				let hours = time.slice(0, 2);
				let minutes = time.slice(3, 5);
				let seconds = time.slice(6, 8);
				return {
					hours: hours,
					minutes: minutes,
					seconds: seconds
				};
			}
        },
		mounted() {
			this.oneClick.clinicID = this.$route.params.clinicID;
		}
    }
</script>

<style scoped>

</style>
