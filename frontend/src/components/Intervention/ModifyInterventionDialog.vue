<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="700px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add an intervention</span>
                <span v-else class="headline">Update intervention</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                        <v-row>

                           <v-col cols="6">
							   <v-menu
									   ref="menu"
									   v-model="dateOpen"
									   :close-on-content-click="false"
									   :return-value.sync="date"
									   transition="scale-transition"
									   offset-y
									   min-width="290px"
							   >
								   <template v-slot:activator="{ on }">
									   <v-text-field
											   v-model="date"
											   label="Date"
											   readonly
											   v-on="on"
											   prepend-icon="mdi-calendar-month"
									   ></v-text-field>
								   </template>
								   <v-date-picker
										   v-model="date"
										   :rules="dateRules"
										   color="green lighten-1"
										   @click:date="selectedDate"
										   :min="today"
								   >
									   <v-spacer></v-spacer>
									   <v-btn text color="primary" @click="dateOpen = false">Cancel</v-btn>
								   </v-date-picker>
							   </v-menu>
                           </v-col>
                           <v-col cols="6">
							   <v-menu
									   ref="timeMenu"
									   v-model="timeOpen"
									   :close-on-content-click="false"
									   :nudge-right="40"
									   :return-value.sync="time"
									   transition="scale-transition"
									   offset-y
									   max-width="290px"
									   min-width="290px"
							   >
								   <template v-slot:activator="{ on }">
									   <v-text-field
											   v-model="time"
											   label="Time"
											   readonly
											   v-on="on"
											   prepend-icon="mdi-clock"

									   ></v-text-field>
								   </template>
								   <v-time-picker
										   v-model="time"
										   :rules="timeRules"
										   format="24hr"
										   color="green lighten-1"
										   @click:minute="$refs.timeMenu.save(time)"
										   :min="now"
								   >
								   </v-time-picker>
							   </v-menu>
                           </v-col>

							<v-col cols="12">
                                <clinic-room-selection
	                                v-model="selectedClinicRoom"
	                                :disabled="mode === `update`"/>
                            </v-col>

                            <v-col cols="12">
                                <doctor-selection
	                                v-model="selectedDoctor"
	                                :disabled="mode === `update`"/>
                            </v-col>

                            <v-col cols="6">
                                <intervention-type-selection
                                	v-model="selectedInterventionType"
                                	:disabled="mode === `update`"/>
                            </v-col>

                            <v-col cols="6">
								<v-text-field
									label="Price for intervention(in $)*"
									required
									disabled
									:value="selectedInterventionType.price"
								>
								</v-text-field>
							</v-col>
                            <v-col cols="6">
								<v-text-field
									label="Duration (in minutes)*"
									required
									:rules="durationRules"
									v-model="duration"
								>
								</v-text-field>
							</v-col>
							<v-col cols="6">
								<v-text-field
									label="Price (in $)*"
									required
									:rules="priceRules"
									v-model="price"
								>
								</v-text-field>
							</v-col>
                        </v-row>
                    </v-form>
                </v-container>
                <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Close</v-btn>
                <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(addInterventionApi)">Add</v-btn>
                <v-btn color="blue darken-1" v-else text @click="submit(updateInterventionApi)">Update</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
	import {mapActions, mapState} from "vuex";
	import {emptyIntervention} from "../../utils/skeletons";
	import ClinicRoomSelection from "../ClinicRooms/ClinicRoomSelection";
	import DoctorSelection from "../Doctor/DoctorSelection";
	import InterventionTypeSelection from "../InterventionType/InterventionTypeSelection";

	export default {
        name: "ModifyInterventionDialog",
        components: {ClinicRoomSelection, DoctorSelection, InterventionTypeSelection},
        data: () => ({
            intervention: emptyIntervention,
			dateOpen: false,
			timeOpen: false,

			dateTime: null,
            selectedClinicRoom: '',
            selectedDoctor: '',
            selectedInterventionType: '',
            duration: '',
            price: '',
            dateRules: [v => !!v || 'Date is required.'],
            timeRules: [v => !!v || 'Time is required.'],
            durationRules: [v => !!v || 'Duration is required.'],
            priceRules: [v => !!v || 'Price is required.']
        }),
        props: {
            editIntervention: null,
            value: false,
            mode: {
                type: String,
                default: 'add'
            }
        },
        computed: {
            ...mapState('auth', ['user']),
            ...mapState('auth', ['clinic']),
			today() {
            	return (new Date()).toISOString().substr(0, 10);
			},
			now() {
            	let current = new Date();
            	current.setMinutes(current.getMinutes() + 5);
            	if(!this.dateTime) this.dateTime = current;
            	if (current.getFullYear() === this.dateTime.getFullYear() &&
					current.getMonth() === this.dateTime.getMonth() &&
					current.getDate() === this.dateTime.getDate()) {


            		return `${current.getHours()}:${current.getMinutes()}`;
				}
            	else return null;
			},
			date: {
            	get() {
					if(!this.dateTime) {
						this.dateTime = new Date();
						this.dateTime.setMinutes(this.dateTime.getMinutes() + 10);
					}
					return this.dateTime.toISOString().substr(0, 10);
				},
				set(val){
					let newDate;
            		if(!val) newDate = new Date();
					else newDate = new Date(val);
					this.dateTime = new Date();
					this.dateTime.setMinutes(this.dateTime.getMinutes() + 10);
					this.dateTime.setFullYear(newDate.getFullYear());
					this.dateTime.setMonth(newDate.getMonth());
					this.dateTime.setDate(newDate.getDate());
					this.dateTime = new Date(this.dateTime);  // da bi vue reagovao :)
            	}
			},
			time: {
            	get() {
            		if (!this.dateTime) {
            			this.dateTime = new Date();
						this.dateTime.setMinutes(this.dateTime.getMinutes() + 10);
					}
					return ('0' + this.dateTime.getHours()).slice(-2) + ':' +
							('0' + this.dateTime.getMinutes()).slice(-2);
				},
				set(val){
            		if(!val) val = '00:00';
            		if(!this.dateTime) {
            			this.dateTime = new Date();
						this.dateTime.setMinutes(this.dateTime.getMinutes() + 10);
					}
					let hours = val.slice(0, 2);
					let minutes = val.slice(-2);
					this.dateTime.setHours(hours);
					this.dateTime.setMinutes(minutes);
					this.dateTime.setSeconds(0);
					this.dateTime.setMilliseconds(0);
					this.dateTime = new Date(this.dateTime);
				}
			}
        },
        watch: {
            value() {
                if (this.editIntervention) {
                    this.intervention = this.editIntervention;
                }
                if(!this.intervention.dateTime) this.dateTime = new Date();
                else this.dateTime = new Date(this.intervention.dateTime);
                this.selectedClinicRoom = this.intervention.selectedClinicRoom;
                this.selectedDoctor = this.intervention.selectedDoctor;
                this.selectedInterventionType = this.intervention.selectedInterventionType;
                this.duration = this.intervention.duration;
                this.price = this.intervention.price;
               	console.log(this.date);
            }
        },
        methods: {
            ...mapActions('intervention/intervention', ['addInterventionApi']),
            ...mapActions('intervention/intervention', ['updateInterventionApi']),

            submit(fun) {

          		this.intervention.clinic = this.clinic.id;

                if (this.$refs.form.validate()) {
                	console.log("add pressed");
                	console.log(this.selectedClinicRoom);
                  	console.log(this.selectedDoctor);
                    console.log(this.selectedInterventionType);
                    console.log(this.duration);
                    console.log(this.price);

                    this.intervention.dateTime = this.dateTime;
                    this.intervention.selectedClinicRoom = this.selectedClinicRoom.id;
                    this.intervention.selectedDoctor = this.selectedDoctor.id;
                    this.intervention.selectedInterventionType = this.selectedInterventionType.id;
                    this.intervention.duration = parseFloat(this.duration);
                    this.intervention.price = parseFloat(this.price);
                    console.log("intervention = ");
                    console.log(this.intervention);
                    fun(this.intervention);
                    this.close();
                }
            },
            close() {
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            },
			selectedDate() {
            	this.$refs.menu.save(this.date);
            	this.timeOpen = true;
			}
        }
    }
</script>

<style scoped>

</style>
