<template>
    <div>
        <v-data-table
                :headers="headers"
                :items="clinicAdmins"
                :loading="loading"
                loading-text="Gathering all the clinic administrators..."
                class="elevation-1"
        >
            <template v-slot:top>
                <v-toolbar flat color="white">
                    <v-toolbar-title>Clinic Administrators</v-toolbar-title>
                    <v-divider
                            class="mx-4"
                            inset
                            vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                </v-toolbar>
            </template>
            <template v-slot:item.remove="{ item }">
                <v-icon
                        @click="deleteDialog(item)"
                        color="red"
                >
                    mdi-delete
                </v-icon>
            </template>
            <template v-slot:item.update="{ item }">
                <v-icon
                        @click="updateDialog(item)"
                        color="amber darken-2"
                >
                    mdi-pencil
                </v-icon>
            </template>
            <template v-slot:no-data>
                <p>There are no existing clinic administrators.</p>
            </template>
        </v-data-table>
        <clinic-administrator-delete-dialog
                v-model="dialog"
                :clinic="clinicToDelete"
                @close="deleteDialog(null)"
                @delete="deleteClinic"
        />

        <modify-clinic-administrator-dialog
                mode="update"
                :edit-clinic-admin="editClinicAdmin"
                :clinicToUpdate="clinicToUpdate"
                v-model="editDialog"/>
    </div>
</template>

<script>
    import {mapActions} from "vuex";
    import ClinicAdministratorDeleteDialog from "./ClinicAdministratorDeleteDialog";
    import ModifyClinicAdministratorDialog from "./ModifyClinicAdministratorDialog";
    import JSOG from 'jsog';

    export default {
        name: "TableClinicAdministrator",
        components: {ModifyClinicAdministratorDialog, ClinicAdministratorDeleteDialog},
        data: () => ({
            loading: false,
            editDialog: false,
            editClinicAdmin: null,
            dialog: false,
            clinicToDelete: null,
            clinicToUpdate: null,
            clinicWithDescription: null,
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'name',
                },
                {text: 'Surname', value: 'surname'},
                {text: 'Email', value: 'email'},
                {text: 'Clinic name', value: 'clinic.name'},
                {text: 'Update', value: 'update', sortable: false, align: 'center'},
                {text: 'Remove', value: 'remove', sortable: false, align: 'center'}
            ],
        }),
        computed: {
            clinicAdmins: {
                get() {
                    return JSOG.decode(this.$store.state.clinicAdmins.readClinicAdmins.clinicAdmins);
                }
            },
            editClinic: {
                get() {
                    return this.$store.state.clinicAdmins.readClinicAdmins.clinicAdmins;
                },
                set(val) {
                    this.$store.commit('clinicAdmins/readClinicAdmins/updateClinicAdmin', val);
                }
            }
        },
        watch: {
            clinicAdmins(arr) {
                this.loading = false;
            }
        },
        methods: {
            ...mapActions('clinicAdmins/readClinicAdmins', ['getClinicAdmins']),
            ...mapActions('clinicAdmins/readClinicAdmins', ['deleteClinicAdminApi']),
            // ...mapActions('clinics/readClinics', ['deleteClinicApi']),
            deleteDialog(clinic) {
                this.clinicToDelete = clinic;
                this.dialog = !this.dialog;
            },
            deleteClinic() {
                this.deleteClinicAdminApi(this.clinicToDelete);
                this.deleteDialog(null);
            },
            toggleClinicDescription(clinic) {
                this.clinicWithDescription = clinic;
                this.descriptionDialog = !this.descriptionDialog;
            },
            updateDialog(clinicAdmin) {
                // this.editClinicAdmin = clinicAdmin;
                // this.clinicToUpdate = clinicAdmin;
                // this.editDialog = true;
                this.$router.push(`/profile/${clinicAdmin.userID}`)
            }

        },
        created() {
            this.loading = true;
            this.getClinicAdmins();
        }

    }
</script>

<style scoped>

</style>
