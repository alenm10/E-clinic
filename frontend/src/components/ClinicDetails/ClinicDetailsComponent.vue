/* this.$route.params.id*/
<template>
    <div>
        <v-container>
            <v-row justify="center">
                <v-col cols="12">
                    <v-card class="mt-5" outlined height="300px">
                        <map-view
                                v-if="adminClinic"
                                map-id="static-map"
                                v-model="adminClinic.coordinates"
                        ></map-view>
                    </v-card>
                </v-col>

            </v-row>
            <v-row>
                <v-col
                        align-self="center"
                        cols="12"
                        md="4"
                        order-md="2"
                        class="clinic-column">
                    <v-card class="clinic-card" height="16.81em">
                        <v-btn
                                elevation="2"
                                class="clinic-rating"
                                color="primary"
                                fab
                                small
                        >
                            <span class="title">{{adminClinic.rating}}</span>
                        </v-btn>
                        <v-btn v-if="this.role != patientCode"
                                elevation="2"
                                class="edit-clinic"
                                color="primary"
                                fab
                                small
                                @click="openModificationDialog"
                        >
                            <v-icon>
                                mdi-pencil
                            </v-icon>
                        </v-btn>
                        <v-card-title class="justify-center">
                            <span v-if="adminClinic" class="headline text-center">
                                {{ adminClinic.name }}
                            </span>

                        </v-card-title>
                        <v-card-subtitle class="text-center">
                            <span class="subtitle-1 text-center">
                                Thank you for visiting us!
                            </span>
                        </v-card-subtitle>
                        <v-divider></v-divider>
                        <v-container fluid>
                            <v-row justify="space-between">
                                <v-col class="pb-0" cols="12">
                                    <span class="subtitle-1">
                                        You can find us at the address
                                    </span>
                                </v-col>
                                <v-col class="pt-0" cols="12">
                                    <span class="subtitle-2">
                                        {{ adminClinic.address }}
                                    </span>
                                </v-col>
                            </v-row>
                        </v-container>
                    </v-card>
                </v-col>
                <v-col cols="12" md="4" order-md="1">
                    <v-card link hover @click="$router.push('/doctors/'+adminClinic.id)">
                        <div class="d-flex flex-no-wrap justify-space-between align-center">
                            <v-avatar v-if="adminClinic && adminClinic.doctors" tile size="125">
                                <span class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.doctors.length }}
                                </span>
                            </v-avatar>
                            <div class="text-center pa-4">
                                <v-icon x-large>
                                    mdi-doctor
                                </v-icon>
                                <v-card-title
                                        class="headline text-center pa-1"
                                >Doctors</v-card-title>
                            </div>
                        </div>
                    </v-card>
                </v-col>

                <v-col cols="12" md="4" order-md="3">
                    <v-card link hover @click="$router.push('/nurses')">
                        <div class="d-flex flex-no-wrap justify-space-between align-center">
                            <v-avatar v-if="adminClinic && adminClinic.nurses" tile size="125">
                                <span class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.nurses.length }}
                                </span>
                            </v-avatar>
                            <div class="text-center pa-4">
                                <v-icon x-large>
                                    mdi-mother-nurse
                                </v-icon>
                                <v-card-title
                                        class="headline text-center pa-1"
                                >Nurses</v-card-title>
                            </div>
                        </div>
                    </v-card>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12" md="3" order-md="1">
                    <v-card link hover @click="$router.push('/clinic-rooms/clinic='+adminClinic.id)">
                        <div class="d-flex flex-no-wrap justify-space-between align-center">
                            <v-avatar v-if="adminClinic && adminClinic.clinicRoom" tile size="125">
                                <span v-if="adminClinic.clinicRoom.length < 100"
                                      class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.clinicRoom.length }}
                                </span>
                                <span v-else-if="adminClinic.clinicRoom.length < 1000"
                                      class="display-2 grey--text text--darken-2">
                                    {{ adminClinic.clinicRoom.length }}
                                </span>
                                <v-icon v-else x-large>
                                    mdi-infinity
                                </v-icon>
                            </v-avatar>
                            <div class="text-center pa-4">
                                <v-icon x-large>
                                    mdi-bed-empty
                                </v-icon>
                                <v-card-title
                                        class="headline text-center pa-1"
                                >Rooms</v-card-title>
                            </div>
                        </div>
                    </v-card>
                </v-col>
                <v-col cols="12" md="3" order-md="1">
                    <v-card link hover @click="$router.push('/patients')">

                        <div class="d-flex flex-no-wrap justify-space-between align-center">
                            <v-avatar v-if="adminClinic && adminClinic.patients" tile size="125">
                                <span v-if="adminClinic.patients.length < 100"
                                      class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.patients.length }}
                                </span>
                                <span v-else-if="adminClinic.patients.length < 1000"
                                  class="display-1 grey--text text--darken-2">
                                    {{ adminClinic.patients.length }}
                                </span>
                                <v-icon v-else x-large>
                                    mdi-infinity
                                </v-icon>
                            </v-avatar>
                            <div class="text-center pa-4">
                                <v-icon x-large>
                                    mdi-doctor
                                </v-icon>
                                <v-card-title
                                        class="headline text-center pa-1"
                                >Patients</v-card-title>
                            </div>
                        </div>
                    </v-card>
                </v-col>
                <v-col cols="12" md="3" order-md="1">
                    <v-card link hover @click="$router.push('/interventionType')">
                        <div class="d-flex flex-no-wrap justify-space-between align-center">
                            <v-avatar v-if="adminClinic && adminClinic.interventionTypes" tile size="125">
                                <span v-if="adminClinic.interventionTypes.length < 100"
                                      class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.interventionTypes.length }}
                                </span>
                                <span v-else-if="adminClinic.interventionTypes.length < 1000"
                                      class="display-1 grey--text text--darken-2">
                                        {{ adminClinic.interventionTypes.length }}
                                </span>
                                <v-icon v-else x-large>
                                    mdi-infinity
                                </v-icon>
                            </v-avatar>
                            <div class="text-center pa-4">
                                <v-icon x-large>
                                    mdi-currency-eur
                                </v-icon>
                                <v-card-title
                                        class="headline text-center pa-1"
                                >Pricing</v-card-title>
                            </div>
                        </div>
                    </v-card>
                </v-col>
                <v-col cols="12" md="3" order-md="1">
                    <v-card link hover @click="$router.push(`/oc-appointments/clinic=${$route.params.id}`)">
                        <div class="d-flex flex-no-wrap justify-space-between align-center">
                            <v-avatar v-if="adminClinic && adminClinic.oneClicks" tile size="125">
                                <span v-if="adminClinic.oneClicks.length < 100"
                                             class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.oneClicks.length }}
                                </span>
                                <span v-else-if="adminClinic.oneClicks.length < 1000"
                                      class="display-4 grey--text text--darken-2">
                                    {{ adminClinic.oneClicks.length }}
                                </span>
                                <v-icon v-else x-large>
                                    mdi-infinity
                                </v-icon>
                            </v-avatar>
                            <div class="text-center pa-4">
                                <v-icon x-large>
                                    mdi-medical-bag
                                </v-icon>
                                <v-card-title
                                        class="headline text-center pa-1"
                                >1Clicks</v-card-title>
                            </div>
                        </div>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
        <modify-clinic-dialog
                mode="update"
                v-model="modifyDialog"
                @modified="modified"
        ></modify-clinic-dialog>
    </div>
</template>

<script>
    import {mapState} from "vuex";
    import MapView from "../Clinics/MapView";
    import ModifyClinicDialog from "../Clinics/ModifyClinicDialog";
    import {emptyClinic} from "../../utils/skeletons";
    import {Patient} from "../../utils/DrawerItems";

    export default {
        name: "ClinicDetailsComponent",
        components: {ModifyClinicDialog, MapView},
        data: () => ({
            modifyDialog: false,
            adminClinic: emptyClinic,
            patientCode: Patient.code
        }),
        computed: {
            ...mapState('auth', ['token']),
            ...mapState('auth', ['role']),         
            editClinic: {
                get() {
                    return this.$store.state.clinics.addClinic.clinic;
                },
                set(val) {
                    this.$store.commit('clinics/addClinic/updateClinic', val);
                }
            },
        },

        methods: {
            modified(clinic) {
                this.adminClinic = clinic;
            },
            openModificationDialog() {
                this.modifyDialog = true;
                this.editClinic = this.adminClinic;
            },
        },
        async mounted() {
            let {data: clinic} = await this.$axios.get('/api/clinic/' + this.$route.params.id,
                {headers: {"Authorization": 'Bearer ' + this.token}});
            this.adminClinic = clinic;
            console.log("adminClinic = ");
            console.log(this.adminClinic);
            console.log("-------------------------- my role =");
            console.log(this.role);
        }
    };

</script>
<style scoped>
    .clinic-card {
        z-index: 10;
    }
    .clinic-column {
        margin-top: -9em;
    }
    @media only screen and (max-width: 960px) {
        .clinic-column {
            margin-top: 2em;
        }
    }
    .edit-clinic {
        position: absolute;
        right: -1.5em;
        top: -1.5em;
        z-index: 10;
    }
    .clinic-rating {
        position: absolute;
        left: -1.5em;
        top: -1.5em;
        z-index: 10;
    }
</style>
