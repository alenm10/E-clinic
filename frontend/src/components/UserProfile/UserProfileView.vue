<template>
    <div>
        <v-container>
            <v-row>
                <v-col>
                    <v-card class="mt-5" outlined>
                        <v-row>
                            <v-col cols="12" class="pt-10 text-center">
                                <v-avatar class="ma-auto" color="primary" size="110">
                                    <span class="white--text display-3">{{ avatarInitials }}</span>
                                </v-avatar>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="12" class="text-center pb-0">
                                <span class="display-1">
                                    {{ greetingMessages.welcome }}
                                </span>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="12" class="text-center pt-0">
                                <span class="text-center subtitle-1 grey--text text--darken-3">
                                    {{ greetingMessages.subtitle }}
                                </span>
                            </v-col>
                        </v-row>
                    </v-card>
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12" sm="6" md="8">
                    <v-card outlined min-height="370">
                        <v-row align="center" no-gutters>
                            <v-col cols="9">
                                <v-card-title>
                                    <span class="headline">Personal info</span>
                                </v-card-title>
                                <v-card-subtitle>
                                    <span class="subtitle-1">Here's what we know about you.</span>
                                </v-card-subtitle>
                            </v-col>
                            <v-col cols="3" v-if="outsideAccess && medicalRecordAllowed">
                                <v-btn @click="medicalRecord" outlined color="primary">
                                    Medical record
                                </v-btn>
                            </v-col>
                        </v-row>
                        <v-list two-line>
                            <profile-info-item
                                    :content="`${profile.name} ${profile.surname}`"
                                    subtitle="Name"
                                    @click="nameDialog = true"
                                    :disabled="outsideAccess"
                            ></profile-info-item>
                            <profile-info-item
                                    :content="profile.personalID"
                                    subtitle="Personal ID"
                                    disabled
                            ></profile-info-item>
                            <profile-info-item
                                    :content="`${profile.address}, ${profile.city}, ${profile.country}`"
                                    subtitle="Address, city and country"
                                    @click="addressDialog = true"
                                    :disabled="outsideAccess"
                            ></profile-info-item>
                            <profile-info-item
                                    :content="profile.phoneNumber"
                                    subtitle="Phone number"
                                    @click="phoneNumberDialog = true"
                                    :disabled="outsideAccess"
                            ></profile-info-item>

                        </v-list>
                    </v-card>
                </v-col>
                <v-col cols="12" sm="6" md="4">
                    <v-card outlined min-height="370">
                        <v-card-title>
                            <span class="headline">Security</span>
                        </v-card-title>
                        <v-card-subtitle>
                            <v-row justify="center">
                                <v-col cols="3" align-self="center">
                                    <v-icon class="d-flex" x-large>
                                        mdi-key-variant
                                    </v-icon>
                                </v-col>
                                <v-col cols="9">
                                    <span class="subtitle-1">
                                        You can view your email and change your password here.
                                    </span>
                                </v-col>
                            </v-row>
                        </v-card-subtitle>
                        <v-list two-line>
                            <profile-info-item
                                    :content="profile.email"
                                    subtitle="Email"
                                    disabled
                            ></profile-info-item>
                            <v-divider></v-divider>
                            <v-list-item :disabled="outsideAccess" link @click="passwordDialog = true">
                                <v-list-item-content>
                                    <v-list-item-title>
                                        ••••••••
                                    </v-list-item-title>
                                    <v-list-item-subtitle>
                                        Password
                                    </v-list-item-subtitle>
                                </v-list-item-content>
                                <v-list-item-action>
                                    <v-icon v-if="outsideAccess">
                                        mdi-lock
                                    </v-icon>
                                    <v-icon v-else>
                                        mdi-chevron-right
                                    </v-icon>
                                </v-list-item-action>
                            </v-list-item>

                            <profile-info-item
                                    :content="lastChanged"
                                    subtitle="Last time the password was changed"
                                    disabled
                            ></profile-info-item>
                        </v-list>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
        <name-dialog v-model="nameDialog" :user="profile" @modify="updateProfile"></name-dialog>
        <address-dialog v-model="addressDialog" :user="profile" @modify="updateProfile"></address-dialog>
        <phone-number-dialog v-model="phoneNumberDialog" :user="profile" @modify="updateProfile"></phone-number-dialog>
        <password-dialog
                v-model="passwordDialog"
                :user="profile"
                @passwordChange="passwordChanged"
                :personal="profile.id === $store.state.auth.user.id"
        ></password-dialog>
        <v-scale-transition>
            <v-card class="upcoming-card" v-show="upcomingIntervention">
                <v-card-title>There is an upcoming intervention</v-card-title>
                <v-card-text>
                    There is an upcoming intervention with this patient {{upcomingDifference}}.
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn text color="primary" @click="$router.push(`/visit/${upcomingIntervention.id}`)">
                        Start
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-scale-transition>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import ProfileInfoItem from "./ProfileInfoItem";
    import NameDialog from "./NameDialog";
    import AddressDialog from "./AddressDialog";
    import PhoneNumberDialog from "./PhoneNumberDialog";
    import {ClinicalAdmin, ClinicalCenterAdmin, Doctor, Patient} from "../../utils/DrawerItems";
    import store from '../../store/index'
    import PasswordDialog from "./PasswordDialog";

    export default {
        name: "UserProfileView",
        components: {PasswordDialog, PhoneNumberDialog, AddressDialog, NameDialog, ProfileInfoItem},
        data: () => ({
            nameDialog: false,
            addressDialog: false,
            phoneNumberDialog: false,
            passwordDialog: false,
            outsideAccess: false,
            upcomingIntervention: false,
            medicalRecordAllowed: false,
        }),
        computed: {
            ...mapState('profile', ['profile']),
            ...mapState('auth', ['user', 'role', 'token']),
            avatarInitials() {
                if (!this.profile) return '';
                let firstName = this.profile.name;
                let firstNameInitial = !!firstName ? firstName[0].toUpperCase() : 'A';
                return `${firstNameInitial}`;
            },
            lastChanged() {
                if (!this.profile.lastPasswordResetDate) return 'a long time ago';
                let lastChange = new Date(this.profile.lastPasswordResetDate);
                let today = new Date();
                let difference = today - lastChange;
                let days = Math.round(difference / (1000 * 60 * 60 * 24));
                if (days === 1) return `${days} day ago`;
                if (days !== 0) return `${days} days ago`;
                let hours = Math.round(difference / (1000 * 60 * 60));
                if (hours === 1) return `${hours} hour ago`;
                if (hours !== 0) return `${hours} hours ago`;
                let minutes = Math.round(difference / (1000 * 60));
                if (minutes <= 1) return '1 minute ago';
                else return `${minutes} minutes ago`;
            },
            greetingMessages() {

                return this.$store.state.auth.user.id === this.$route.params.id ? {
                    welcome: `Welcome, ${this.profile.name} ${this.profile.surname}`,
                    subtitle: `Manage your info and security to make eClinic work better for you.`
                } : {
                    welcome: `You're viewing the profile of user ${this.profile.name} ${this.profile.surname}`,
                    subtitle: `Don't do anything mean. After all, this is eClinic.`
                };
            },
            upcomingDifference() {
                if(!this.upcomingIntervention) return '';

                let now = new Date();
                let difference = new Date(this.upcomingIntervention.dateTime.start) - now;
                let diffMins = Math.round(((difference % 86400000) % 3600000) / 60000); // minutes
                console.log(this.upcomingIntervention.dateTime.start);
                if (diffMins > 0) return `in ${diffMins} minutes`;
                else return `right now`;
            }

        },
        watch: {
            async '$route.params.id'(){
                await this.populate();
                await this.checkIntervention();
            }
        },
        async mounted() {
            await this.populate();
            await this.checkIntervention();
        },
        methods: {
            ...mapActions('profile', ['getProfileApi', 'updateProfileApi']),
            updateProfile(profile) {
                this.updateProfileApi(profile);
            },
            passwordChanged() {
                this.getProfileApi(this.$route.params.id);
            },
            medicalRecord() {
                this.$router.push(`/medical-record/${this.profile.id}`);
            },
            async populate() {
                try {
                    await this.getProfileApi(this.$route.params.id);
                    if(this.profile.type === Patient.code && this.role === Doctor.code) {
                        this.outsideAccess = true;

                    } else {
                        this.outsideAccess = false;
                    }
                } catch (e) {
                    this.$router.push('/');
                }
            },
            async checkIntervention() {
                console.log(this.profile);
                try {
                    let {data: res} = await this.$axios.get(`/api/intervention/upcoming/${this.user.id}/${this.profile.id}`,
                        {headers: {"Authorization": 'Bearer ' + this.token} });
                    this.upcomingIntervention = res;
                    if(res) {
                        this.medicalRecordAllowed = true;
                        return;
                    }
                    let {data: exists} = await this.$axios.get(`/api/intervention/${this.user.id}/${this.patientID}`,
                        {headers: {"Authorization": 'Bearer ' + this.token} });

                    if(exists) {
                        this.medicalRecordAllowed = true;
                        return;
                    }
                    this.medicalRecordAllowed = false;
                } catch(e) {
                    console.log(e);
                    this.medicalRecordAllowed = false;

                    console.log("There aren't any upcoming interventions.")
                }
            }

        },
        beforeRouteEnter(to, from, next) {
            let loggedUser = store.state.auth.user;
            let id = to.params.id;
            let role = store.state.auth.role;
            console.log(to);
            if (role !== ClinicalCenterAdmin.code && role !== ClinicalAdmin.code && role !== Doctor.code && loggedUser.id !== id) {
                next('/')
            }
            next();
        },
    }
</script>

<style scoped>
    .upcoming-card {
        position: fixed;
        bottom: 3em;
        right: 3em;
    }
</style>
