<template>
    <div>

        <v-card>
            <doctor-search
                    :value="!fromClinic"
                    :selected-date="scheduleDate"
                    :selected-type.sync="scheduleType"
                    @searched="searched"
                    @reset="reset"/>

            <v-data-table
                    :headers="headers"
                    :items="doctors"
                    class="elevation-1"
                    :loading="loading"
                    :options.sync="options"
                    :server-items-length="length"
                    loading-text="Contacting all the doctors to see if they still work here..."
            >
                <template v-slot:item.update="{ item }">
                    <v-icon
                            @click="toProfile(item)"
                            color="amber darken-2"
                    >
                        mdi-pencil
                    </v-icon>
                </template>

                <template v-slot:item.remove="{ item }">
                    <v-icon v-if="item.busyTimes == null || item.busyTimes.length == 0"
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

                <template v-slot:item.availability="{ item }">
                    <v-icon
                            @click="availableDialog(item)"
                            color="primary darken-2"
                    >
                        mdi-calendar
                    </v-icon>
                </template>
                <template v-slot:item.schedule="{ item }">
                    <v-icon
                            @click="toggleScheduleDialog(item)"
                            color="amber darken-2"
                    >
                        mdi-clock
                    </v-icon>
                </template>
                <template v-slot:no-data>
                    <p>There aren't any doctors here :(</p>
                </template>
            </v-data-table>
        </v-card>
        <delete-dialog
                v-model="dialog"
                :doctor="doctorToDelete"
                @close="deleteDialog(null)"
                @delete="deleteDoctor"
        />
        <dialog-available
                :value="dialogAvailable"
                :date="scheduleDate"
                :doctor="doctorAvailability"
                @close="availableDialog(null)"
        ></dialog-available>
        <schedule-dialog
                :value="scheduleDialog"
                :date="scheduleDate"
                :doctor="doctorToSchedule"
                :type="scheduleType"
                :clinic="scheduleClinic"
                @close="toggleScheduleDialog(null)"
        ></schedule-dialog>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDialog from "./DeleteDialog";
    import ModifyDoctorDialog from "./ModifyDoctorDialog";
    import {ClinicalAdmin, ClinicalCenterAdmin, Patient} from "../../utils/DrawerItems";
    import DoctorSearch from "./DoctorSearch";
    import DialogAvailable from "./DialogAvailable";
    import ScheduleDialog from "./ScheduleDialog";

    export default {
        name: "DoctorTable",
        components: {ScheduleDialog, DialogAvailable, DoctorSearch, DeleteDialog, ModifyDoctorDialog},
        data: () => ({
            loading: false,
            dialog: false,
            dialogAvailable: false,
            doctorToDelete: null,
            doctorAvailability: null,
            options: {
                page: 1,
                itemsPerPage: 10
            },
            adminCode: ClinicalAdmin.code,
            patientCode: Patient.code,
            searchRequest: null,
            fromClinic: false,
            scheduleDate: null,
            scheduleDialog: false,
            doctorToSchedule: null,
            scheduleClinic: null,
        }),
        computed: {
            ...mapState('doctor/doctor', ['length']),
            ...mapState('auth', ['token', 'role']),
            doctors() {
                if (this.options.itemsPerPage > 0)
                    return this.$store.state.doctor.doctor.doctors.slice(0, this.options.itemsPerPage);
                else
                    return this.$store.state.doctor.doctor.doctors;
            },
            headers() {
                let regularHeaders = [
                    {text: 'Name', align: 'start', value: 'name'},
                    {text: 'Surname', align: 'center', value: 'surname'},
                    {text: 'Email', align: 'center', value: 'email'},
                    {text: 'Phone number', align: 'center', value: 'phoneNumber'},
                    {text: 'City', align: 'center', value: 'city'},
                    {text: 'Rating', align: 'center', value: 'avgRating'}                   
                ];
                let patientHeader = [
                    {text: 'Availability', value: 'availability', sortable: false, align: 'center'},
                    {text: 'Schedule', value: 'schedule', sortable: false, align: 'center'}
                ];
                let adminHeaders = [
                    {text: 'Update', value: 'update', sortable: false, align: 'center'},
                    {text: 'Remove', value: 'remove', sortable: false, align: 'center'}
                ];
                if (this.role === this.adminCode) {
                    regularHeaders = regularHeaders.concat(adminHeaders);
                } else if (this.role === this.patientCode && !!this.searchRequest)
                    regularHeaders = regularHeaders.concat(patientHeader);
                return regularHeaders;
            }
        },
        methods: {
            ...mapActions('doctor/doctor',
                [
                    'getDoctor',
                    'getClinicDoctors',
                    'deleteDoctorApi',
                    'searchApi'
                ]),

            deleteDialog(doctorToDelete) {
                this.doctorToDelete = doctorToDelete;
                this.dialog = !this.dialog;
            },
            availableDialog(doctor) {
                this.doctorAvailability = doctor;
                this.dialogAvailable = !this.dialogAvailable;
            },
            deleteDoctor() {
                this.deleteDoctorApi(this.doctorToDelete);
                this.deleteDialog(null);
            },
            toggleScheduleDialog(doctor) {
                this.doctorToSchedule = doctor;
                this.scheduleDialog = !this.scheduleDialog;
            },
            toProfile(doctor) {
                this.$router.push(`/profile/${doctor.userID}`)
            },
            populate() {
                if(!this.searchRequest)
                    this.getClinicDoctors({
                        clinicID: this.$route.params.clinicID,
                        pageNumber: this.options.page,
                        pageSize: this.options.itemsPerPage,
                        sort: this.options.sortBy,
                        desc: this.options.sortDesc
                    });
                else
                    this.searchApi(
                        {
                            pageNumber: this.options.page,
                            pageSize: this.options.itemsPerPage,
                            sort: this.options.sortBy,
                            desc: this.options.sortDesc,
                            request: this.searchRequest
                        });
            },
            searched(payload) {
                this.scheduleDate = payload.date;
                this.searchRequest = payload;
                this.searchRequest.clinicID = this.$route.params.clinicID;
                this.loading = true;
                this.populate();
            },
            reset() {
                this.searchRequest = null;
                this.loading = true;
                this.populate();
            },
            async setup() {
                try {
                    let {data: clinic} = await this.$axios.get(`/api/clinic/${this.$route.params.clinicID}`,
                        {headers: {"Authorization": 'Bearer ' + this.token}});
                    this.scheduleClinic = clinic;
                    if (this.$route.params.payload) {
                        let payload = this.$route.params.payload;
                        this.scheduleType = payload.interventionType;
                        this.scheduleDate = payload.date;
                        this.searchRequest = {
                            clinicID: payload.clinicID,
                            interventionTypeID: payload.interventionType.id,
                            date: payload.date
                        };
                        this.fromClinic = true;
                    } else this.fromClinic = false;
                    this.populate();
                } catch(err) {
                    console.log("SJEBO SI");
                }
            }
        },
        mounted() {
            this.loading = true;
            this.setup();
        },
        watch: {
            doctors(val) {
                this.loading = false;
                if(this.options.itemsPerPage <= this.length && val.length < this.options.itemsPerPage) {
                    this.loading = true;
                    this.populate();
                }
            },
            options(val) {
                this.loading = true;
                if(!this.searchRequest)
                    this.getClinicDoctors({
                        clinicID: this.$route.params.clinicID,
                        pageNumber: val.page,
                        pageSize: val.itemsPerPage,
                        sort: val.sortBy,
                        desc: val.sortDesc
                    });
                else
                    this.searchApi(
                        {
                            pageNumber: val.page,
                            pageSize: val.itemsPerPage,
                            sort: val.sortBy,
                            desc: val.sortDesc,
                            request: this.searchRequest
                        });
            },
        }
    }
</script>

<style scoped>

</style>
