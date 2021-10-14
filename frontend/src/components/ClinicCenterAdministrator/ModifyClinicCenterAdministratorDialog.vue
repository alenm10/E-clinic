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
                    <span v-if="mode === 'add'" class="headline">Add a clinic center administrator</span>
                    <span v-else class="headline">Update clinic center administrator</span>
                </v-card-title>
                <v-card-text>
                    <v-container>

                        <v-form ref="form">
                            <template>
                                <v-row>
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
                                                :rules="[rules.required, rules.email]"
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
                    <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(saveClinicCenterAdmin)">Add</v-btn>
                    <v-btn color="blue darken-1" v-else text @click="submit(modifyClinicCenterApi)">Update</v-btn>

                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
    import {mapActions} from "vuex";
    import {emailPattern, emptyClinicCenterAdmin} from "../../utils/skeletons";
    import Vue from "vue";

    export default {
        name: "ModifyClinicCenterAdministratorDialog",
        components: {},
        data: () => ({
            passwordError: {
                isError: false,
                errorMessage: ''
            },
            emailError: {
                isError: false,
                errorMessage: ''
            },
            confpass: '',
            clinicCenterAdmin: emptyClinicCenterAdmin,
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
            editClinicCenterAdmin: null,
            value: null,
            mode: {
                type: String,
                default: 'add'
            }
        },
        watch: {
            value() {
                if (this.editClinicCenterAdmin) {
                    this.name = this.editClinicCenterAdmin.name;
                    this.surname = this.editClinicCenterAdmin.surname;
                    this.password = this.editClinicCenterAdmin.password;
                    this.username = this.editClinicCenterAdmin.username;
                    this.confpass = this.editClinicCenterAdmin.password;
                    this.city = this.editClinicCenterAdmin.city;
                    this.email = this.editClinicCenterAdmin.email;
                    this.country = this.editClinicCenterAdmin.country;
                    this.personalID = this.editClinicCenterAdmin.personalID;
                    this.address = this.editClinicCenterAdmin.address;
                    this.clinicCenterAdmin = this.editClinicCenterAdmin;
                    this.phoneNumber = this.editClinicCenterAdmin.phoneNumber;
                }

            }
        },
        filters: {
            formatCoords(value) {
                if (!value) return '';
                return `(${value.lat.toFixed(6)}, ${value.lng.toFixed(6)})`;
            }
        },
        methods: {
            ...mapActions('clinicCenterAdmins/clinicCenterAdmins', ['saveClinicCenterAdmin']),
            ...mapActions('clinicCenterAdmins/clinicCenterAdmins', ['modifyClinicCenterApi']),
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
                    let clinicA = emptyClinicCenterAdmin;
                    this.clinicCenterAdmin.password = this.password;
                    this.clinicCenterAdmin.name = this.name;
                    this.clinicCenterAdmin.email = this.email;
                    this.clinicCenterAdmin.surname = this.surname;
                    this.clinicCenterAdmin.username = this.username;
                    this.clinicCenterAdmin.city = this.city;
                    this.clinicCenterAdmin.country = this.country;
                    this.clinicCenterAdmin.personalID = this.personalID;
                    this.clinicCenterAdmin.address = this.address;
                    this.clinicCenterAdmin.phoneNumber = this.phoneNumber;
                    if (this.mode === "update")

                        clinicA.id = this.editClinicCenterAdmin.id;
                    fun(this.clinicCenterAdmin);
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
