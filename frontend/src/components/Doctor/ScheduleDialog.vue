<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="600"
            @keydown.enter="$emit('close')"
    >
        <v-card>
            <v-card-title class="headline">Appointment</v-card-title>
            <v-form ref="form" @submit.prevent="accept">
                <v-card-text class="subtitle-1">
                    <div>The appointment will be done for {{typeName}}</div>
                    <div>It will cost you ${{typePrice}}.</div>
                    <div>The date of the appointment is {{date | filterDate}}.</div>
                    <div>The doctor you chose is {{ doctorName }}!</div>
                    <div class="text--primary text--darken-2">Now, you should choose your preferred time of appointment.</div>
                    <v-menu
                            ref="menu"
                            v-model="menu"
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
                                    label="Choose appointment time"
                                    prepend-icon="mdi-clock"
                                    readonly
                                    v-on="on"
                                    :error="error.isError"
                                    :error-messages="error.errorMessages"
                            ></v-text-field>
                        </template>
                        <v-time-picker
                                v-if="menu"
                                v-model="time"
                                full-width
                                format="24h"
                                @click:minute="$refs.menu.save(time)"
                        ></v-time-picker>
                    </v-menu>
                </v-card-text>
                <v-card-actions>
                    <v-btn
                            color="green darken-1"
                            text
                            @click="$emit('close')"
                    >
                        Close
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                            color="green darken-1"
                            text
                            @click="accept"
                    >
                        Sure
                    </v-btn>

                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
    import {DayOfTheWeek} from "../../utils/DayOfTheWeek";
    import {mapActions, mapState} from "vuex";

    export default {
        name: "ScheduleDialog",
        data: () => ({
            time: '',
            menu: false,
            error: {
                isError: false,
                errorMessages: ''
            }
        }),
        computed: {
            ...mapState('auth', ['user']),
            typeName() {
                return this.type ? this.type.name : '';
            },
            typePrice() {
                return this.type ? this.type.price : '';
            },
            doctorName() {
                return this.doctor ? this.doctor.name : '';
            }
        },
        props: {
            value: {
                type: Boolean,
                default: false
            },
            clinic: null,
            date: null,
            type: null,
            doctor: null
        },
        methods: {
            ...mapActions('appointmentRequests', ['addRequestApi']),
            async accept() {
                if(!this.validateTime()) return;
                let date = new Date(this.date);
                let parsedTime = this.parseShortTime(this.time);
                date.setHours(parsedTime.hours, parsedTime.minutes);

                let {data: patient} = await this.$axios.get(`/api/patient/user/${this.user.id}`,
                    {headers: {"Authorization": 'Bearer ' + this.$store.state.auth.token}});

                let offsetDate = new Date(date);
                offsetDate.setMinutes(date.getMinutes() - date.getTimezoneOffset());
                this.addRequestApi({
                    dateTime: offsetDate,
                    interventionTypeID: this.type.id,
                    clinicID: this.clinic.id,
                    doctorID: this.doctor.id,
                    patientID: patient.id
                });
                this.$router.push('/clinics');
            },
            validateTime() {

                this.error.isError = false;
                this.error.errorMessages = '';
                if(!this.time) {
                    this.error.isError = true;
                    this.error.errorMessages = 'Time is required.';
                    return false;
                }
                let date = new Date(this.date);
                let selectedTime = this.parseShortTime(this.time);
                let selectedDateTime = new Date(date);
                selectedDateTime.setHours(selectedTime.hours, selectedTime.minutes);
                let weekday = DayOfTheWeek[date.getDay()];
                let timeperiod = this.doctor.workingSchedule[weekday];
                let start = this.parseTime(timeperiod.start);
                let startDateTime = new Date(date);
                startDateTime.setHours(start.hours, parseInt(start.minutes), start.seconds);
                if(selectedDateTime < startDateTime){
                    this.error.isError = true;
                    this.error.errorMessages = 'This is before the doctor starts workday.';
                    return false;
                }
                let end = this.parseTime(timeperiod.end);
                let endDateTime = new Date(date);
                endDateTime.setHours(end.hours, parseInt(end.minutes), end.seconds);
                if(selectedDateTime > endDateTime){
                    this.error.isError = true;
                    this.error.errorMessages = 'This is after the doctor ends workday.';
                    return false;
                }
                for(let timeperiod of this.doctor.busyTimes) {
                    let startDateTime = new Date(timeperiod.start);
                    let endDateTime = new Date(timeperiod.end);
                    endDateTime.setHours(end.hours, end.minutes, end.seconds);
                    let endOfSelected = new Date(selectedDateTime);
                    endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

                    if(selectedDateTime >= startDateTime && selectedDateTime <= endDateTime){
                        this.error.isError = true;
                        this.error.errorMessages = 'Doctor has an appointment during this time.';
                        return false;
                    }
                    if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
                        this.error.isError = true;
                        this.error.errorMessages = 'The next intervention is too close to this one.';
                        return false;
                    }
                }
                let endOfSelected = new Date(selectedDateTime);
                endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

                for(let ar of this.doctor.appointmentRequests) {
                    let time = ar.dateTime;
                    let startDateTime = new Date(time);
                    let endDateTime = new Date(startDateTime);
                    endDateTime.setMinutes(startDateTime.getMinutes() + 30);

                    if(selectedDateTime >= startDateTime && selectedDateTime <= endDateTime){
                        this.error.isError = true;
                        this.error.errorMessages = 'There is an appointment request for this time.';
                        return false;
                    }
                    if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
                        this.error.isError = true;
                        this.error.errorMessages = 'The next appointment is too close to this one.';
                        return false;
                    }
                }

                for(let timeperiod of this.doctor.vacations) {
                    let startDateTime = new Date(timeperiod.start);
                    let endDateTime = new Date(timeperiod.end);
                    endDateTime.setHours(end.hours, end.minutes, end.seconds);
                    let endOfSelected = new Date(selectedDateTime);
                    endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

                    if(selectedDateTime >= startDateTime && selectedDateTime <= endDateTime){
                        this.error.isError = true;
                        this.error.errorMessages = 'Doctor is on vacation during this time.';
                        return false;
                    }
                    if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
                        this.error.isError = true;
                        this.error.errorMessages = 'Doctor will be on vacation during this time..';
                        return false;
                    }
                }

                for(let timeperiod of this.doctor.oneClickAppointments) {
                    let startDateTime = new Date(timeperiod.start);
                    let endDateTime = new Date(timeperiod.end);
                    endDateTime.setHours(end.hours, end.minutes, end.seconds);
                    let endOfSelected = new Date(selectedDateTime);
                    endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

                    if(selectedDateTime >= startDateTime && selectedDateTime <= endDateTime){
                        this.error.isError = true;
                        this.error.errorMessages = 'Doctor has an appointment during this time.';
                        return false;
                    }
                    if(endOfSelected >= startDateTime && endOfSelected <= endDateTime) {
                        this.error.isError = true;
                        this.error.errorMessages = 'The next intervention is too close to this one.';
                        return false;
                    }
                }
                return true;
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
            },
            parseShortTime(time) {
                let hours = time.slice(0, 2);
                let minutes = time.slice(3, 5);
                return {
                    hours: hours,
                    minutes: minutes,
                };
            },

        },
        filters:{
            filterDate(date) {
                if (!date) return 'never';
                return new Date(date).toLocaleDateString();
            }
        }
    }
</script>

<style scoped>

</style>
