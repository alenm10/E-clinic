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
                            Patients : {{this.clinic.patients.length}}
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="patientsDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>

            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Calendar
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="calendarDetails()">
                            Details
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>

            <v-col cols="12" sm="6">
                <v-card class="mx-auto" max-width="344">
                    <v-card-text>
                        <p class="display-1 text--primary">
                            Vacation request
                        </p>
                    </v-card-text>
                    <v-card-actions>
                        <v-btn text color="deep-purple accent-4" @click="vacationDetails()">
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
        name: "NurseHome",
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
            calendarDetails(){
                this.$router.push(`/workingSchedule`)
            },
            vacationDetails(){
                console.log("vacationDetails");
                this.$router.push(`/vacationRequest`)
            },
            profileDetails(){
                console.log("profileDetails");
                this.$router.push(`/profile/${this.user.id}`)
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