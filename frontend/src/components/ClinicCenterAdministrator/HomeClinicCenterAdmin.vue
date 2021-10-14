<template>
    <div>
        <v-row>
            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Profile
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="profileDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                           Clinic Center Administrators
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="clinicCenterAdministratorDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>

            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Clinics
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="clinicsDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>

            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Disease
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="diseaseDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>

            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Medicines
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="medicineDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>

            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Clinic Administrators
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="clinicAdministratorsDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Clinic Administrators
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="registeredPatientsDetails()">
                           Patients
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            User requests
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="unregisteredPatientsDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>


    </div>
</template>

<script>
    import {mapState} from "vuex";

    export default {
        name: "HomeClinicCenterAdmin",
        components: {},
        data: () => ({
        }),
        computed: {
            ...mapState('auth', ['user']),
            ...mapState('auth', ['clinic']),
        },
        watch: {
        },
        methods: {
            patientsDetails(){
                this.$router.push(`/patients`)
            },
            clinicCenterAdministratorDetails(){
                this.$router.push(`/clinicCenterAdministrators`)
            },
            clinicsDetails(){
                this.$router.push(`/clinics`)
            },
            diseaseDetails(){
                this.$router.push(`/disease`)
            },
            profileDetails(){
                this.$router.push(`/profile/${this.user.id}`)
            },
            medicineDetails(){
                this.$router.push(`/medicines`)
            },
            clinicAdministratorsDetails(){
                this.$router.push(`/clinicAdministrators`)
            },
            registeredPatientsDetails(){
                this.$router.push(`/patients`)
            },
            unregisteredPatientsDetails(){
                this.$router.push(`/unregisteredUsers`)
            },
        },
        created() {
            console.log(this.user);		//doctor id = user.id
            console.log(this.clinic);	//klinika kojoj ovaj doctor pripada
        }
    }
</script>

<style scoped>

</style>