<template>
    <div>
        <v-dialog
                :value="value"
                @input="$emit('input', false)"
                persistent
                max-width="600px"
        >

            <v-card>
                <v-card-title>
                    <span v-if="mode === 'add'" class="headline">Add a clinic administrator</span>
                    <span v-else class="headline">Update clinic administrator</span>
                </v-card-title>
                <v-card-text>
                    <v-container>

                        <v-form ref="form">
                            <template>
                                <v-row>
                                    <v-col cols="6">
                                        <clinic-selection v-model="selectedClinic"/>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                label="Email*"
                                                required
                                                :rules="[rules.required, rules.email]"
                                                v-model="email"
                                                :error="emailError.isError"
                                                :error-messages="emailError.errorMessage"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                label="Name*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="name"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                label="Surname*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="surname"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                ref="password"
                                                label="Password*"
                                                required
                                                v-model="password"
                                                :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                                :rules="[rules.required]"
                                                :type="showPassword ? 'text' : 'password'"
                                                name="input-10-2"
                                                hint="At least 8 characters"
                                                class="input-group--focused"
                                                :error="passwordError.isError"
                                                :error-messages="passwordError.errorMessage"
                                                type="password"
                                                @click:append="showPassword = !showPassword"
                                        ></v-text-field>
                                    </v-col>

                                    <v-col cols="6">
                                        <v-text-field
                                                label="Confirm password*"
                                                required
                                                v-model="confpass"
                                                :append-icon="showPassword2 ? 'mdi-eye' : 'mdi-eye-off'"
                                                :rules="[rules.required]"
                                                :type="showPassword2 ? 'text' : 'password'"
                                                name="input-10-2"
                                                hint="At least 8 characters"
                                                class="input-group--focused"
                                                :error="passwordError.isError"
                                                :error-messages="passwordError.errorMessage"
                                                type="password"
                                                @click:append="showPassword2 = !showPassword2"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                label="Phone number*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="phoneNumber"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                label="Personal ID*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="personalID"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="3">
                                        <v-text-field
                                                label="City*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="city"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="3">
                                        <v-text-field
                                                label="State*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="country"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-text-field
                                                label="Address*"
                                                required
                                                :rules="[rules.required]"
                                                v-model="address"
                                        ></v-text-field>
                                    </v-col>

                                </v-row>
                            </template>
                        </v-form>
                        <small>*indicates required field</small>
                    </v-container>
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="blue darken-1" text @click="closeAddDialog">Close</v-btn>
                    <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(saveClinicAdmin)">Add</v-btn>
                    <v-btn color="blue darken-1" v-else text @click="submit(modifyClinicApi)">Update</v-btn>

                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
    import {mapActions} from "vuex";
    import {emailPattern, emptyClinicAdmin} from "../../utils/skeletons";
    import ClinicSelection from "../Clinics/ClinicSelection";
    import Vue from "vue";

    export default {
        name: "ModifyClinicAdministratorDialog",
        components: {ClinicSelection},
        data: () => ({
            passwordError: {
                isError: false,
                errorMessage: ''
            },
            emailError: {
                isError: false,
                errorMessage: ''
            },
            selectedClinic: null,
            confpass: '',
            clinicAdmin: emptyClinicAdmin,
            name: '',
            surname: '',
            password: '',
            username: '',
            city: '',
            country: '',
            email: '',
            personalID: '',
            address: '',
            phoneNumber: '',

            showPassword: false,
            showPassword2: false,
            rules: {
                required: value => !!value || 'Required.',
                email: value => {
                    return emailPattern.test(value) || 'Invalid e-mail.';
                },
            }
        }),
        props: {
            editClinicAdmin: null,
            clinicToUpdate: {
                default: null
            },
            value: null,
            mode: {
                type: String,
                default: 'add'
            }
        },
        watch: {
            value() {
                if (this.editClinicAdmin) {
                    this.name = this.editClinicAdmin.name;
                    this.surname = this.editClinicAdmin.surname;
                    this.password = this.editClinicAdmin.password;
                    this.username = this.editClinicAdmin.username;
                    this.confpass = this.editClinicAdmin.password;
                    this.city = this.editClinicAdmin.city;
                    this.email = this.editClinicAdmin.email;
                    this.country = this.editClinicAdmin.country;
                    this.personalID = this.editClinicAdmin.personalID;
                    this.address = this.editClinicAdmin.address;
                    this.selectedClinic = this.editClinicAdmin.clinic;
                    this.clinicAdmin = this.editClinicAdmin;
                    this.phoneNumber = this.editClinicAdmin.phoneNumber;
                }
                // } else {
                //     this.name = "";
                //     this.email = "";
                //     this.surname = "";
                //     this.password = "";
                //     this.username = "";
                //     this.city = "";
                //     this.country = "";
                //     this.personalID = "";
                //     this.address = "";
                //     this.selectedClinic = null;
                //     this.clinicAdmin = emptyClinicAdmin;
                // }

            }
        },
        filters: {
            formatCoords(value) {
                if (!value) return '';
                return `(${value.lat.toFixed(6)}, ${value.lng.toFixed(6)})`;
            }
        },
        methods: {
            ...mapActions('clinicAdmins/readClinicAdmins', ['saveClinicAdmin']),
            ...mapActions('clinicAdmins/readClinicAdmins', ['modifyClinicApi']),
            setAddress(event) {
                if (!event) this.address = null;
                else {
                    this.address = event[0];
                }

            },
            async exists() {
                try {
                    this.name = (await Vue.prototype.$axios.get(`/api/auth/exists/${this.formElements.email}`)).data;
                    this.emailError.isError = true;
                    this.emailError.errorMessage = `That account already exists.`;
                    console.log("Vec postoji.")
                } catch (err) {
                    console.log("Ne postoji.")
                }

            },
            emailSelected() {
                this.emailError.isError = false;
                this.emailError.errorMessage = '';
                if (this.email !== "") {
                    return this.exists();
                }
                return false;
            },
            passwordSelected() {
                this.passwordError.isError = false;
                this.passwordError.errorMessage = ``;
                if (this.password !== this.confpass) {
                    this.passwordError.isError = true;
                    this.passwordError.errorMessage = `Passwords don't match`;
                    return false;
                }
                return true;
            },
            closeAddDialog() {
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            },
            submit(fun) {
                let valid = this.$refs.form.validate();
                this.emailSelected();
                if (valid && this.passwordSelected() === true) {
                    let clinicA = emptyClinicAdmin;
                    this.clinicAdmin.password = this.password;
                    this.clinicAdmin.name = this.name;
                    this.clinicAdmin.email = this.email;
                    this.clinicAdmin.surname = this.surname;
                    this.clinicAdmin.username = this.username;
                    this.clinicAdmin.city = this.city;
                    this.clinicAdmin.country = this.country;
                    this.clinicAdmin.personalID = this.personalID;
                    this.clinicAdmin.address = this.address;
                    this.clinicAdmin.phoneNumber = this.phoneNumber;
                    this.clinicAdmin.clinicID = this.selectedClinic.id;
                    this.clinicAdmin.clinic = null;
                    if (this.mode === "update")
                        clinicA.id = this.editClinicAdmin.id;
                    fun(this.clinicAdmin);
                    this.closeAddDialog();
                }
            }
        }
    }
</script>

<style scoped>
    .card-body {
        margin: auto 0 auto 0;
        height: 91%;
        width: 100%;
        padding: 0 !important;
    }
</style>
