<template>
    <div>
        <v-card>
            <v-card-title>
                Medical Prescription
                <v-spacer></v-spacer>
                <v-text-field
                        v-model="search"
                        append-icon="mdi-magnify"
                        label="Search"
                        single-line
                        hide-details
                ></v-text-field>
            </v-card-title>
        </v-card>

        <v-data-table
                :headers="headers"
                :items="visits"
                :search="search"
                class="elevation-1"
                :loading="loading"
                loading-text="Loading all the medical prescription..."
        >
            <template v-slot:item.update="{ item }">
                <v-icon
                        @click="updateDialog(item)"
                        color="amber darken-2"
                        v-if="!item.certified"
                >
                    mdi-file-hidden
                </v-icon>
                <v-icon
                        @click="updateDialog(item)"
                        color="amber darken-2"
                        v-else
                >
                    mdi-file-check
                </v-icon>
            </template>


            <template v-slot:no-data>
                <p>There are no existing medical prescription</p>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "MedicalPrescriptionTable",
        data: () => ({
            search : "",
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            medicineToDelete: null,
            editMedicine: null,
            headers: [
                {text: 'ID', align: 'start', value: 'id'},
                {text: 'Patient Name', value: 'patientName', align: 'center'},
                {text: 'Doctor Name', value: 'doctorName', align: 'center'},
                {text: 'Certified', value: 'update', sortable: false},
                {text: 'Certified by', value: 'nurseId', sortable: false}

            ],
        }),
        computed: {
            ...mapState('auth', ['user']),
            ...mapState('visit/visit', ['visits']),
            // editClinic: {
            //     get() {
            //         return this.$store.state.clinics.addClinic.clinic;
            //     },
            //     set(val) {
            //         this.$store.commit('clinics/addClinic/updateClinic', val);
            //     }
            // }
        },
        methods: {
            ...mapActions('visit/visit', ['getVisits']),
            ...mapActions('visit/visit', ['updateVisitApi']),

            updateDialog(visit) {
                //visit.certified = true;
                this.editMedicine = visit;
                let toSend = {
                    visit : visit,
                    user : this.user,
                }
                this.updateVisitApi(toSend, this.user);

            }

        },
        created() {
            this.loading = true;
            this.getVisits();
        },
        watch: {
            visits() {
                this.loading = false;
            }
        }
    }
</script>

<style scoped>

</style>