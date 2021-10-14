<template>
    <v-row align="center">
        <v-col cols="12">
            <v-card>
                <div class="pa-10 display-2 text-center">
                    Thank you for choosing our clinical center!
                </div>
            </v-card>
        </v-col>
        <v-col cols="12" sm="4">
            <v-card link hover @click="$router.push('/clinics/')">
                <div class="d-flex flex-no-wrap justify-space-between align-center">
                    <v-icon class="ma-4" size="6em">
                        mdi-hospital-building
                    </v-icon>
                    <div class="text-center pa-4">
                        <v-card-title
                                class="headline text-center pa-1"
                        >Clinics</v-card-title>
                    </div>
                </div>
            </v-card>
        </v-col>
        <v-col cols="12" sm="4">
            <v-card link hover @click="$router.push('/interventionType')">
                <div class="d-flex flex-no-wrap justify-space-between align-center">
                    <v-icon class="ma-4" size="6em">
                        mdi-currency-eur
                    </v-icon>
                    <div class="text-center pa-4">
                        <v-card-title
                                class="headline text-center pa-1"
                        >Pricing</v-card-title>
                    </div>
                </div>
            </v-card>
        </v-col>
        <v-col cols="12" sm="4">
            <v-card link hover @click="$router.push('/history')">
                <div class="d-flex flex-no-wrap justify-space-between align-center">
                    <v-icon class="ma-4" size="6em">
                        mdi-history
                    </v-icon>
                    <div class="text-center pa-4">
                        <v-card-title
                                class="headline text-center pa-1"
                        >History</v-card-title>
                    </div>
                </div>
            </v-card>
        </v-col>
        <v-col cols="12" sm="6">
            <v-card link hover @click="$router.push(`/medical-record/${$store.state.auth.user.id}`)">
                <div class="d-flex flex-no-wrap justify-space-between align-center">
                    <v-icon class="ma-4" size="6em">
                        mdi-view-list
                    </v-icon>
                    <div class="text-center pa-4">
                        <v-card-title
                                class="headline text-center pa-1"
                        >Medical record</v-card-title>
                    </div>
                </div>
            </v-card>
        </v-col>
        <v-col cols="12" sm="6">
            <v-card link hover @click="$router.push(`/appointmentRequests/patient=${patient.id}`)">
                <div class="d-flex flex-no-wrap justify-space-between align-center">
                    <v-icon class="ma-4" size="6em">
                        mdi-calendar
                    </v-icon>
                    <div class="text-center pa-4">
                        <v-card-title
                                class="headline text-center pa-1"
                        >Requests</v-card-title>
                    </div>
                </div>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
    export default {
        name: "PatientHome",
        data: () => ({
            patient: {
                id: null
            },
        }),
        async mounted() {
            await this.getPatient();
        },
        methods: {
            async getPatient() {
                try {
                    let {data: res} = await this.$axios.get(`/api/patient/user/${this.$store.state.auth.user.id}`,
                        {headers: {"Authorization": 'Bearer ' + this.$store.state.auth.token}});
                    this.patient = res;
                } catch (err) {
                    console.log(err);
                }
            }
        }
    }
</script>

<style scoped>

</style>
