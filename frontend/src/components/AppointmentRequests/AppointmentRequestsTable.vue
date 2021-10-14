<template>
    <div>

        <v-card>
            <v-card-title>
                Appointment requests
            </v-card-title>
            <v-data-table
                    :headers="headers"
                    :items="requests"
                    class="elevation-1"
                    :loading="loading"
                    :options.sync="options"
                    :server-items-length="length"
                    loading-text="Where are my requestsssss..."
            >

                <template v-slot:item.cancel="{ item }">
                    <v-icon
                            @click="deleteDialog(item)"
                            color="red"
                    >
                        mdi-table-cancel
                    </v-icon>
                </template>

                <template v-slot:item.deny="{ item }">
                    <v-icon
                            @click="deleteDialog(item)"
                            color="primary darken-2"
                    >
                        mdi-table-cancel
                    </v-icon>
                </template>
                <template v-slot:item.approve="{ item }">
                    <v-icon
                            @click="approveDialog(item)"
                            color="amber darken-2"
                    >
                        mdi-check
                    </v-icon>
                </template>
                <template v-slot:no-data>
                    <p>There aren't any requests here :(</p>
                </template>
            </v-data-table>
        </v-card>

        <delete-dialog
                :value="dialogDelete"
                :request="requestToDeny"
                :mode="role === patientCode ? `cancel` : `delete`"
                @close="deleteDialog(null)"
                @delete="deleteRequest"
        ></delete-dialog>
        <approve-dialog
                :value="dialogApprove"
                :request="requestToApprove"
                @close="approveDialog(null)"
                @approve="approveRequest"
        ></approve-dialog>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDialog from "./DeleteDialog";
    import {ClinicalAdmin, Patient} from "../../utils/DrawerItems";
    import ApproveDialog from "./ApproveDialog";

    export default {
        name: "AppointmentRequestsTable",
        components: {DeleteDialog, ApproveDialog},
        data: () => ({
            loading: false,
            dialogDelete: false,
            dialogApprove: false,
            requestToDeny: null,
            options: {
                page: 1,
                itemsPerPage: 10
            },
            adminCode: ClinicalAdmin.code,
            patientCode: Patient.code,
            requestToApprove: null,
        }),
        computed: {
            ...mapState('appointmentRequests', ['length']),
            ...mapState('auth', ['token']),
            ...mapState('auth', ['role']),
            requests() {
                if (this.itemsPerPage > 0)
                    return this.$store.state.appointmentRequests.requests.slice(0, this.options.itemsPerPage);
                else
                    return this.$store.state.appointmentRequests.requests;
            },
            headers() {
                let regularHeaders = [
                    {text: 'Id', align: 'start', value: 'id'},
                    {text: 'Appointment date', align: 'center', value: 'dateTime'},
                    {text: 'Date of creation', align: 'center', value: 'dateOfCreation'},
                ];
                let patientHeader = [
                    {text: 'Cancel', value: 'cancel', sortable: false, align: 'center'},
                ];
                let adminHeaders = [
                    {text: 'Deny', value: 'deny', sortable: false, align: 'center'},
                    {text: 'Approve', value: 'approve', sortable: false, align: 'center'}
                ];
                if (this.role === this.adminCode) {
                    regularHeaders = regularHeaders.concat(adminHeaders);
                } else if (this.role === this.patientCode)
                    regularHeaders = regularHeaders.concat(patientHeader);
                return regularHeaders;
            }
        },
        methods: {
            ...mapActions('appointmentRequests',
                ['getRequestsByClinic',
                'getRequestsByPatient',
                'deleteRequestApi',
                'approveRequestApi']),

            deleteDialog(request) {
                this.requestToDeny = request;
                this.dialogDelete = !this.dialogDelete;
            },
            deleteRequest() {
                let dateNow = new Date();
                let dateTime = new Date(this.requestToDeny.dateTime);
                if (dateTime.getTime() - dateNow.getTime()< 86400000) return;
                this.deleteRequestApi(this.requestToDeny);
                this.deleteDialog(null);
            },
            approveDialog(request) {
                this.requestToApprove = request;
                this.dialogApprove = !this.dialogApprove;

            },
            approveRequest() {
                this.$router.push(`/choose-clinic-room/request=${this.requestToApprove.id}/clinic=${this.$route.params.clinicID}`);
            },

            populate() {
                if(this.$route.path.startsWith("/appointmentRequests/clinic="))
                    this.getRequestsByClinic({
                        clinicID: this.$route.params.clinicID,
                        pageNumber: this.options.page,
                        pageSize: this.options.itemsPerPage,
                        sort: this.options.sortBy,
                        desc: this.options.sortDesc
                    });
                else
                    this.getRequestsByPatient(
                        {
                            patientID: this.$route.params.patientID,
                            pageNumber: this.options.page,
                            pageSize: this.options.itemsPerPage,
                            sort: this.options.sortBy,
                            desc: this.options.sortDesc
                        }
                    );
            },

        },
        mounted() {
            this.loading = true;
            this.populate();
        },
        watch: {
            requests(val) {
                this.loading = false;
                if(this.options.itemsPerPage <= this.length && val.length < this.options.itemsPerPage) {
                    this.loading = true;
                    this.populate();
                }
            },
            options(val) {
                this.loading = true;
                if(this.$route.path.startsWith("/appointmentRequests/clinic="))
                    this.getRequestsByClinic({
                        clinicID: this.$route.params.clinicID,
                        pageNumber: val.page,
                        pageSize: val.itemsPerPage,
                        sort: val.sortBy,
                        desc: val.sortDesc
                    });
                else
                    this.getRequestsByPatient(
                        {
                            patientID: this.$route.params.patientID,
                            pageNumber: val.page,
                            pageSize: val.itemsPerPage,
                            sort: val.sortBy,
                            desc: val.sortDesc
                        }
                    );
            },
        },

    }
</script>

<style scoped>

</style>
