<template>
    <div>
        <v-data-table
                :headers="headers"
                :items="clinicCenterAdmins"
                :loading="loading"
                loading-text="Gathering all the clinic administrators..."
                class="elevation-1"
        >
            <template v-slot:top>
                <v-toolbar flat color="white">
                    <v-toolbar-title>Clinic Center Administrators</v-toolbar-title>
                    <v-divider
                            class="mx-4"
                            inset
                            vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                </v-toolbar>
            </template>
            <template v-slot:no-data>
                <p>There are no existing clinic center administrators.</p>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import {mapActions} from "vuex";
    import ModifyClinicAdministratorDialog from "./ModifyClinicCenterAdministratorDialog";
    import JSOG from 'jsog';

    export default {
        name: "TableClinicCenterAdministrator",
        components: {ModifyClinicAdministratorDialog},
        data: () => ({
            loading: false,
            clinicWithDescription: null,
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'name',
                },
                {text: 'Surname', value: 'surname'},
                {text: 'Email', value: 'email'}
            ],
        }),
        computed: {
            clinicCenterAdmins: {
                get() {
                    return JSOG.decode(this.$store.state.clinicCenterAdmins.clinicCenterAdmins.clinicCenterAdmins);
                }
            },
        },
        watch: {
            clinicCenterAdmins(arr) {
                this.loading = false;
            }
        },
        methods: {
            ...mapActions('clinicCenterAdmins/clinicCenterAdmins', ['getClinicCenterAdmins']),

        },
        created() {
            this.loading = true;
            this.getClinicCenterAdmins();
        }

    }
</script>

<style scoped>

</style>
