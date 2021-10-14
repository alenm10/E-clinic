<template>
    <div>

        <v-card>
            <clinic-room-search
                    :value="$route.params.requestID"
                    :selected-date-time="new Date(selectedDateTime)"
                    @searched="searched"
                    @reset="reset"
            />

            <v-data-table
                    :headers="headers"
                    :items="clinicRooms"
                    class="elevation-1"
                    :loading="loading"
                    :options.sync="options"
                    :server-items-length="length"
                    loading-text="Visiting all the clinic rooms..."
            >
                <template v-slot:item.update="{ item }">
                    <v-icon
                            @click="updateDialog(item)"
                            color="amber darken-2"
                    >
                        mdi-pencil
                    </v-icon>
                </template>

                <template v-slot:item.remove="{ item }">
                    <v-icon v-if="item.interventions == null || item.interventions.length == 0"
                            @click="deleteDialog(item)"
                            color="red"
                    >
                        mdi-delete
                    </v-icon>
                    <v-icon v-else
                            color="gray"
                    >
                        mdi-delete
                    </v-icon>
                </template>

                <template v-slot:item.schedule="{ item }">
                    <v-icon
                            @click="scheduleDialog(item)"
                            color="red"
                    >
                        mdi-check
                    </v-icon>
                </template>

                <template v-slot:item.calendar="{ item }">
                    <v-icon
                            @click="toggleAvailableDialog(item)"
                            color="red"
                    >
                        mdi-calendar
                    </v-icon>
                </template>

                <template v-slot:no-data>
                    <p v-if="searchRequest">NO ROOMS NOW. GOTTA SEARCH BETTER</p>
                    <p v-else>There are no existing clinic rooms in this clinic.</p>
                </template>
            </v-data-table>
        </v-card>
        <delete-dialog
                v-model="dialog"
                :room="roomToDelete"
                @close="deleteDialog(null)"
                @delete="deleteRoom"
        />
        <schedule-dialog
                v-model="dialogSchedule"
                :room="roomToSchedule"
                :request="appointmentRequest"
                @close="scheduleDialog(null)"
                @schedule="scheduleRoom"
        />
        <modify-clinic-room-dialog
                mode="update"
                :edit-room="editRoom"
                v-model="editDialog"/>
        <change-doctor
                v-model="doctorError.isError"
                :request="appointmentRequest"
                @doctorChanged="doctorChanged"
                :error-message="doctorError.errorMessages"
                :room="roomToSchedule"
                @close="resetError"/>
        <clinic-room-available
                :value="availableDialog"
                @close="toggleAvailableDialog(null)"
                :clinic-room="availableRoom"
        ></clinic-room-available>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDialog from "./DeleteDialog";
    import ModifyClinicRoomDialog from "./ModifyClinicRoomDialog";
    import {ClinicalAdmin, Patient} from "../../utils/DrawerItems";
    import ClinicRoomSearch from "./ClinicRoomSearch";
    import ScheduleDialog from "./ScheduleDialog";
    import {DayOfTheWeek} from "../../utils/DayOfTheWeek";
    import ChangeDoctor from "./ChangeDoctor";
    import ClinicRoomAvailable from "./ClinicRoomAvailable";
    import JSOG from "jsog";

    export default {
        name: "ClinicRoomsTable",
        components: {
            ClinicRoomAvailable,
            ChangeDoctor, ScheduleDialog, ClinicRoomSearch, ModifyClinicRoomDialog, DeleteDialog},
        data: () => ({
            searchRequest: null,
            appointmentRequest: null,
            selectedDateTime: null,
            selectedDoctorID: null,
            loading: false,
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            roomToDelete: null,
            roomToSchedule: null,
            dialogSchedule: false,
            editRoom: null,
            options: {
                page: 1,
                itemsPerPage: 10
            },
            adminCode: ClinicalAdmin.code,
            patientCode: Patient.code,
            doctorError: {
                isError: false,
                errorMessages: ''
            },
            availableDialog: false,
            availableRoom: null,
        }),
        computed: {
            ...mapState('auth', ['role', 'token']),
            ...mapState('clinicRooms/clinicRooms', ['length']),
            clinicRooms() {
                if (this.options.itemsPerPage > 0)
                    return this.$store.state.clinicRooms.clinicRooms.clinicRooms.slice(0, this.options.itemsPerPage);
                else
                    return this.$store.state.clinicRooms.clinicRooms.clinicRooms;
            },
            headers() {
                let regularHeaders = [
                    {text: 'ID', align: 'start', value: 'id'},
                    {text: 'Name', value: 'name', align: 'center'},
                    {text: 'Calendar', value: 'calendar', sortable: false, align: 'center'}
                ];
                let adminHeaders = [
                    {text: 'Update', value: 'update', sortable: false, align: 'center'},
                    {text: 'Remove', value: 'remove', sortable: false, align: 'center'}
                ];
                let scheduleHeaders = [
                    {text: 'Schedule', value: 'schedule', sortable: false, align: 'center'},
                ];
                if (this.searchRequest)
                    regularHeaders = regularHeaders.concat(scheduleHeaders);
                else if (this.role === this.adminCode)
                    regularHeaders = regularHeaders.concat(adminHeaders);
                return regularHeaders;
            }
        },
        watch: {
            clinicRooms(val) {
                this.loading = false;
                if(this.options.itemsPerPage <= this.length && val.length < this.options.itemsPerPage) {
                    this.loading = true;
                    this.populate();
                }
            },
            options(val) {
                this.loading = true;
                if(!this.searchRequest)
                    this.getClinicRooms({
                        clinicID: this.$route.params.clinicID,
                        pageNumber: val.page,
                        pageSize: val.itemsPerPage,
                        sort: val.sortBy,
                        desc: val.sortDesc
                    });
                else
                    this.searchApi(
                        {
                            clinicID: this.$route.params.clinicID,
                            pageNumber: val.page,
                            pageSize: val.itemsPerPage,
                            sort: val.sortBy,
                            desc: val.sortDesc,
                            request: this.searchRequest
                        });
            }
        },
        methods: {
            ...mapActions('clinicRooms/clinicRooms', ['getClinicRooms', 'deleteRoomApi', 'searchApi']),
            ...mapActions('intervention/intervention', ['addRequestedIntervention']),

            async doctorChanged(doctor) {
                this.appointmentRequest.doctorID = doctor.id;
                await this.updateRequest();
                this.doctorEror = {
                    isError: false,
                    errorMessages: ''
                };
            },
            resetError() {
                this.doctorError = {
                    isError: false,
                    errorMessages: ''
                };
            },
            deleteDialog(room) {
            	console.log("deleteDialgo = ");
            	console.log(room);
                this.roomToDelete = room;
                this.dialog = !this.dialog;
            },
            deleteRoom() {
                this.deleteRoomApi(this.roomToDelete);
                this.deleteDialog(null);
            },
            toggleAvailableDialog(room) {
                this.availableRoom = room;
                this.availableDialog = !this.availableDialog;
            },
            scheduleDialog(room) {
                this.roomToSchedule = room;
                this.dialogSchedule = !this.dialogSchedule;
            },
            async updateRequest() {
                let {data: appReq} = await this.$axios.put(`/api/appointment-requests/${this.appointmentRequest.id}`,
                    this.appointmentRequest, {headers: {"Authorization": 'Bearer ' + this.token}});
                this.appointmentRequest = appReq;
            },
            async scheduleRoom() {
                let correct = true;
                if (new Date(this.selectedDateTime).getTime() !== new Date(this.appointmentRequest.dateTime).getTime()){
                    correct = await this.check();
                    if(correct) {
                        this.appointmentRequest.doctorID = this.selectedDoctorID;
                        this.appointmentRequest.dateTime = this.selectedDateTime;
                        await this.updateRequest();
                    }
                }
                if (correct) {
                    this.doctorError = {
                        isError: false,
                        errorMessages: ''
                    };
                    this.addRequestedIntervention({
                        requestID: this.appointmentRequest.id,
                        clinicRoomID: this.roomToSchedule.id
                    });
                    this.$router.push(`/appointmentRequests/clinic=${this.$route.params.clinicID}`);
                }
            },
            updateDialog(room) {
                this.editRoom = room;
                this.editDialog = true;
            },
            populate() {
                if(!this.searchRequest)
                    this.getClinicRooms({
                        clinicID: this.$route.params.clinicID,
                        pageNumber: this.options.page,
                        pageSize: this.options.itemsPerPage,
                        sort: this.options.sortBy,
                        desc: this.options.sortDesc
                    });
                else
                    this.searchApi(
                        {
                            clinicID: this.$route.params.clinicID,
                            pageNumber: this.options.page,
                            pageSize: this.options.itemsPerPage,
                            sort: this.options.sortBy,
                            desc: this.options.sortDesc,
                            request: this.searchRequest
                        });
            },
            searched(payload) {
                this.selectedDateTime = payload.dateTime;
                this.searchRequest = payload;
                this.loading = true;
                this.populate();
            },
            reset() {
                this.searchRequest = null;
                this.loading = true;
                this.populate();
            },
            async check() {
                let {data: doctor} = await this.$axios.get(`/api/doctor/${this.appointmentRequest.doctorID}`,
                    {headers: {"Authorization": 'Bearer ' + this.token}});

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
                    let endOfSelected = new Date(this.selectedDateTime);
                    endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

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
                    let endOfSelected = new Date(this.selectedDateTime);
                    endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

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
                let endOfSelected = new Date(this.selectedDateTime);
                endOfSelected.setMinutes(endOfSelected.getMinutes() + 30);

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
                for(let intervention of JSOG.decode(this.roomToSchedule.interventions)) {

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

        },
        async mounted() {
            this.loading = true;
            if(this.$route.params.requestID) {

                let {data: request} = await this.$axios.get(`/api/appointment-requests/${this.$route.params.requestID}`,
                    {headers: {"Authorization": 'Bearer ' + this.token}});
                this.appointmentRequest = request;
                this.selectedDateTime = request.dateTime;
                this.selectedDoctorID = request.doctorID;
                this.searchRequest = {
                    dateTime: request.dateTime,
                    roomName: '',
                    roomId: ''
                }
            }
            this.populate();
        }
    }
</script>

<style scoped>

</style>
