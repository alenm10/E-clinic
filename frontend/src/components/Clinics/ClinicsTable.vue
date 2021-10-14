<template>
    <div>

        <v-card>
            <clinic-search @searched="searched" @selectedType="scheduleType = $event" @reset="reset"/>
            <v-divider></v-divider>
            <v-data-table
                    :headers="headers"
                    :items="clinics"
                    :loading="loading"
                    :options.sync="options"
                    :server-items-length="length"
                    @page-count="pageCount = $event"
                    loading-text="Building all the clinics..."
                    class="elevation-1"
            >
                <template v-slot:item.details="{ item }">
                    <v-icon
                            color="success"
                            @click="toDetailsPage(item)"
                    >mdi-information
                    </v-icon>
                </template>
                <template v-if="role === adminCode" v-slot:item.remove="{ item }">
                    <v-icon
                            @click="deleteDialog(item)"
                            color="red"
                    >
                        mdi-delete
                    </v-icon>
                </template>
                <template v-if="role === adminCode" v-slot:item.update="{ item }">
                    <v-icon
                            @click="updateDialog(item)"
                            color="amber darken-2"
                    >
                        mdi-pencil
                    </v-icon>
                </template>
                <template v-if="role === patientCode" v-slot:item.schedule="{ item }">
                    <v-icon
                            @click="toggleScheduleDialog(item)"
                            color="lime darken-2"
                    >
                        mdi-calendar-check
                    </v-icon>
                </template>
                <template v-slot:no-data>
                    <p>There are no existing clinics</p>
                </template>
            </v-data-table>

        </v-card>
        <delete-dialog
                v-model="dialog"
                :clinic="clinicToDelete"
                @close="deleteDialog(null)"
                @delete="deleteClinic"
        />
        <modify-clinic-dialog
                mode="update"
                v-model="editDialog"/>
        <request-dialog
                v-if="role === patientCode && !!searchRequest"
                v-model="scheduleDialog"
                :clinic="scheduleClinic"
                :date="searchRequest.date"
                :type="scheduleType"
                @close="toggleScheduleDialog(null)"
                @continued="continued"
        ></request-dialog>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DescriptionDialog from "./DescriptionDialog";
    import DeleteDialog from "./DeleteDialog";
    import ModifyClinicDialog from "./ModifyClinicDialog";
    import {ClinicalCenterAdmin, Patient} from '../../utils/DrawerItems';
    import ClinicSearch from "./ClinicSearch";
    import RequestDialog from "./RequestDialog";

    export default {
        name: "ClinicsTable",
        components: {RequestDialog, ClinicSearch, ModifyClinicDialog, DeleteDialog, DescriptionDialog},
        data: () => ({
            loading: false,
            editDialog: false,
            dialog: false,
            clinicToDelete: null,
            scheduleClinic: null,
            scheduleDialog: false,
            scheduleType: null,
            options: {
                page: 1,
                itemsPerPage: 10
            },
            adminCode: ClinicalCenterAdmin.code,
            patientCode: Patient.code,
            searchRequest: null,
            pageCount: 0,
        }),
        computed: {
            ...mapState('clinics/readClinics', ['length']),
            ...mapState('auth', ['role']),

            clinics() {
                console.log(this.$store.state.clinics.readClinics.clinics);
                if (this.options.itemsPerPage > 0)
                    return this.$store.state.clinics.readClinics.clinics.slice(0, this.options.itemsPerPage);
                else
                    return this.$store.state.clinics.readClinics.clinics;

            },
            editClinic: {
                get() {
                    return this.$store.state.clinics.addClinic.clinic;
                },
                set(val) {
                    this.$store.commit('clinics/addClinic/updateClinic', val);
                }
            },

            headers() {
                let regularHeaders = [
                    {text: 'Name', align: 'start', value: 'name'},
                    {text: 'Address', value: 'address'},
                    {text: 'Details', value: 'details', sortable: false, align: 'center'},
                    {text: 'Rating', value: 'rating'},                    
                ];
                let patientHeader = [
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
        watch: {
            clinics(val) {
                this.loading = false;
                if(this.options.itemsPerPage <= this.length
                    && val.length < this.options.itemsPerPage
                    && this.options.page < this.pageCount) {
                    this.loading = true;
                    this.populate();
                }
            },
            options(val) {
                this.loading = true;
                if(!this.searchRequest)
                    this.getClinics({
                        pageNumber: val.page,
                        pageSize: val.itemsPerPage,
                        sort: val.sortBy,
                        desc: val.sortDesc
                    });
                else
                    this.search(
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
            ...mapActions('clinics/readClinics', ['getClinics']),
            ...mapActions('clinics/readClinics', ['deleteClinicApi']),
            ...mapActions('clinics/readClinics', ['search']),
            ...mapActions('appointmentRequests', ['addRequestApi']),

            deleteDialog(clinic) {
                this.clinicToDelete = clinic;
                this.dialog = !this.dialog;
            },
            deleteClinic() {
                this.deleteClinicApi(this.clinicToDelete);
                this.deleteDialog(null);
            },
            toDetailsPage(clinic) {
                this.$router.push(`/clinic/${clinic.id}`)
            },
            updateDialog(clinic) {
                this.editClinic = clinic;
                this.editDialog = true;
            },
            toggleScheduleDialog(clinic) {
                if(this.loading) return;
                this.scheduleClinic = clinic;
                this.scheduleDialog = !this.scheduleDialog;
            },
            continued(payload) {
                this.$router.push({ name: 'doctors', params:
                        {
                            clinicID: payload.clinicID,
                            payload
                        }
                });
            },
            populate() {
                if(!this.searchRequest)
                    this.getClinics({
                        pageNumber: this.options.page,
                        pageSize: this.options.itemsPerPage,
                        sort: this.options.sortBy,
                        desc: this.options.sortDesc
                    });
                else
                    this.search(
                        {
                            pageNumber: this.options.page,
                            pageSize: this.options.itemsPerPage,
                            sort: this.options.sortBy,
                            desc: this.options.sortDesc,
                            request: this.searchRequest
                        });
            },
            searched(payload) {
                this.searchRequest = payload;
                this.loading = true;
                this.populate();
            },
            reset() {
                this.searchRequest = null;
                this.loading = true;
                this.populate();
            }

        },
        mounted() {
            this.loading = true;
            this.populate();

        }
    }
</script>

<style scoped>

</style>
