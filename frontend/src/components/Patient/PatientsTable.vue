<template>
    <div>
        <v-card>
            <patient-search
                @reset="reset"
                @searched="searched"
            ></patient-search>
            <v-data-table
                    :headers="headers"
                    :items="patients"
                    :loading="loading"
                    :options.sync="options"
                    :server-items-length="length"
                    loading-text="Gathering all patients..."
                    class="elevation-1"
            >

                <template v-if="role !== adminCode" v-slot:item.remove="{ item }">
                    <v-icon
                            @click="deleteDialog(item)"
                            color="red"
                    >
                        mdi-delete
                    </v-icon>
                </template>



                <template v-slot:item.remove="{ item }">
                    <v-icon
                            @click="deleteDialog(item)"
                            color="red"
                    >
                        mdi-delete
                    </v-icon>
                </template>
                <template v-slot:item.details="{ item }">
                    <v-icon
                            @click="$router.push(`/profile/${item.userID}`)"
                            color="red"
                    >
                        mdi-account-details
                    </v-icon>
                </template>
                <template v-slot:item.medicalRecord="{ item }">
                    <v-icon
                            v-if="!item.medicalRecord"
                            @click="toggleCreateMedicalRecord(item)"
                            color="amber darken-2"
                    >
                        mdi-clipboard-pulse-outline
                    </v-icon>

                    <v-icon
                            v-else
                            @click=""
                            color="amber darken-2"
                    >
                        mdi-clipboard-pulse
                    </v-icon>

                </template>
                <template v-slot:no-data>
                    <p>This clinical center doesn't have any patients registered.</p>
                </template>
            </v-data-table>
        </v-card>
        <delete-dialog
                v-model="dialog"
                :patient="patientToDelete"
                @close="deleteDialog(null)"
                @delete="deleteClinic"
        />
        <modify-medical-record
                v-model="recordDialog"
                :patient="medicalRecordPatient"

        ></modify-medical-record>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDialog from "./DeleteDialog";
    import {ClinicalCenterAdmin, Doctor} from '../../utils/DrawerItems';
    import PatientSearch from "./PatientSearch";
    import ModifyMedicalRecord from "./ModifyMedicalRecord";

    export default {
        name: "PatientsTable",
        components: {ModifyMedicalRecord, PatientSearch, DeleteDialog},
        data: () => ({
            loading: false,
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            patientToDelete: null,
            medicalRecordPatient: null,
            recordDialog: false,
            adminCode: ClinicalCenterAdmin.code,
            doctorCode: Doctor.code,
            searchRequest: null,
            options: {
                page: 1,
                itemsPerPage: 10
            },
        }),
        computed: {
            ...mapState('patients', ['length']),
            ...mapState('auth', ['role']),
            patients() {
                if (this.options.itemsPerPage > 0)
                    return this.$store.state.patients.patients.slice(0, this.options.itemsPerPage);
                else
                    return this.$store.state.patients.patients;
            },
            headers() {
                let regularHeaders = [
                    {text: 'Name', align: 'start', value: 'name'},
                    {text: 'Surname', align: 'center', value: 'surname'},
                    {text: 'Personal ID', align: 'center', value: 'personalID'},
                ];
                let adminHeaders = [
                    {text: 'Medical record', value: 'medicalRecord', sortable: false},
                    {text: 'Details', value: 'details', sortable: false},
                    {text: 'Remove', value: 'remove', sortable: false},
                ];
                let doctorHeaders = [
                    {text: 'Details', value: 'details', sortable: false},
                ];
                if (this.role === this.adminCode) {
                    regularHeaders = regularHeaders.concat(adminHeaders);
                } else if (this.role === this.doctorCode)
                    regularHeaders = regularHeaders.concat(doctorHeaders);
                return regularHeaders;
            }

        },
        watch: {
            patients(val) {
                this.loading = false;
                if(this.options.itemsPerPage <= this.length && val.length < this.options.itemsPerPage) {
                    this.loading = true;
                    this.populate();
                }
            },
            options(val) {
                this.loading = true;
                if(!this.searchRequest)
                    this.getPatients({
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
        },
        methods: {
            ...mapActions('patients', ['getPatients', 'createRecord', 'searchApi']),

            deleteDialog(patient) {
                this.patientToDelete = patient;
                this.dialog = !this.dialog;
            },
            deleteClinic() {
                this.deletePatient(this.clinicToDelete);
                this.deleteDialog(null);
            },
            toggleCreateMedicalRecord(patient) {
                this.medicalRecordPatient = patient;
                this.recordDialog = !this.recordDialog;
            },
            populate() {
                if(!this.searchRequest)
                    this.getPatients({
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
            reset() {
                this.searchRequest = null;
                this.loading = true;
                this.populate();
            },
            searched(payload) {
                this.searchRequest = payload;
                this.loading = true;
                this.populate();
            },
        },
        mounted() {
            this.loading = true;
            this.populate();
        }

    }
</script>

<style scoped>

</style>
