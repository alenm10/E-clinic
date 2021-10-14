<template>
    <v-container fluid class="pa-0">
        <v-stepper v-model="registerStep" class="elevation-0 pa-0">
            <v-stepper-items>
                <v-stepper-content step="1" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0"><span class="display-1">Sign up</span></v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">to get a new account</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">First, choose your email:</span>
                        </v-col>
                    </v-row>
                    <v-form ref="emailForm" @submit.prevent="emailSelected" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Email"
                                        name="email"
                                        :rules="[rules.required, rules.email]"
                                        v-model="formElements.email"
                                        :error="emailError.isError"
                                        :error-messages="emailError.errorMessage"
                                        outlined
                                />
                            </v-col>
                        </v-row>
                        <v-row class="ma-4 mt-0">
                            <v-col cols="12" class="pt-2 align-end">
                                <span class="text--secondary d-inline-flex">
                                    Already have an account?
                                </span>
                                <v-btn type="button" text color="primary" @click="$router.push('/auth')" class="px-2"
                                       small>
                                    sign in
                                </v-btn>
                            </v-col>
                        </v-row>
                        <v-row class="ma-4 mt-0">
                            <v-col cols="12" class="text-right">
                                <v-btn type="submit" depressed large color="primary">
                                    Next
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-stepper-content>
                <v-stepper-content step="2" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-2">We are glad that you chose our clinical center!</span>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">{{ newUser.email }}</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">Now, choose your password.</span>
                        </v-col>
                    </v-row>
                    <v-form ref="passwordForm" @submit.prevent="passwordSelected" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Password"
                                        name="password"
                                        :rules="[rules.required]"
                                        :error="passwordError.isError"
                                        :error-messages="passwordError.errorMessage"
                                        v-model="formElements.password"
                                        type="password"
                                        outlined
                                />
                            </v-col>
                        </v-row>

                        <v-row class="ma-4 my-0">
                            <v-col cols="12" class="py-0 align-end">
                                <span class="text--secondary d-inline-flex">
                                    One more time.
                                </span>
                            </v-col>
                        </v-row>

                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Repeat password"
                                        name="repeatPassword"
                                        :rules="[rules.required]"
                                        :error="passwordError.isError"
                                        :error-messages="passwordError.errorMessage"
                                        v-model="formElements.repeatPassword"
                                        type="password"
                                        outlined
                                />
                            </v-col>
                        </v-row>


                        <v-row class="ma-4 mt-0" justify="space-between">
                            <v-col cols="4">
                                <v-btn text type="button" @click="backToEmail" large color="secondary">
                                    Go back
                                </v-btn>
                            </v-col>
                            <v-col cols="4">
                                <v-btn type="submit" depressed large color="primary">
                                    Sign in
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-stepper-content>
                <v-stepper-content step="3" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-2">We are glad that you chose our clinical center!</span>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">{{ newUser.email }}</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">We need some more details as well.</span>
                        </v-col>
                    </v-row>
                    <v-form ref="nameForm" @submit.prevent="nameSelected" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" sm="6" class="pb-2">
                                <v-text-field
                                        label="First name"
                                        name="firstName"
                                        :rules="[rules.required]"
                                        v-model="formElements.firstName"
                                        outlined
                                />
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="Last name"
                                        name="lastName"
                                        :rules="[rules.required]"
                                        v-model="formElements.lastName"
                                        outlined
                                />
                            </v-col>
                        </v-row>
                        <v-row class="ma-4 mt-1 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Personal ID"
                                        name="personalID"
                                        :rules="[rules.required]"
                                        v-model="formElements.personalID"
                                        outlined
                                />
                            </v-col>
                        </v-row>


                        <v-row class="ma-4 mt-0" justify="space-between">
                            <v-col cols="4">
                                <v-btn text type="button" @click="backToPassword" large color="secondary">
                                    Go back
                                </v-btn>
                            </v-col>
                            <v-col cols="4">
                                <v-btn type="submit" depressed large color="primary">
                                    Sign in
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-stepper-content>
                <v-stepper-content step="4" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-2">We are glad that you chose our clinical center!</span>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">{{ newUser.email }}</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">Tell us about your address.</span>
                        </v-col>
                    </v-row>
                    <v-form ref="addressForm" @submit.prevent="addressSelected" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" sm="6" class="pb-2">
                                <v-text-field
                                        label="Country"
                                        name="country"
                                        :rules="[rules.required]"
                                        v-model="formElements.country"
                                        outlined
                                />
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="City"
                                        name="city"
                                        :rules="[rules.required]"
                                        v-model="formElements.city"
                                        outlined
                                />
                            </v-col>
                        </v-row>
                        <v-row class="ma-4 mt-1 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Address"
                                        name="address"
                                        :rules="[rules.required]"
                                        v-model="formElements.address"
                                        outlined
                                />
                            </v-col>
                        </v-row>


                        <v-row class="ma-4 mt-0" justify="space-between">
                            <v-col cols="4">
                                <v-btn text type="button" @click="backToName" large color="secondary">
                                    Go back
                                </v-btn>
                            </v-col>
                            <v-col cols="4">
                                <v-btn type="submit" depressed large color="primary">
                                    Next
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-stepper-content>
                <v-stepper-content step="5" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-2">We are glad that you chose our clinical center!</span>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">{{ newUser.email }}</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">Only the phone number remains.</span>
                        </v-col>
                    </v-row>
                    <v-form ref="phoneForm" @submit.prevent="phoneSelected" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Phone number"
                                        name="phoneNumber"
                                        :rules="[rules.required]"
                                        v-model="formElements.phoneNumber"
                                        ma
                                        outlined
                                />
                            </v-col>
                        </v-row>
                        <v-row class="ma-4 mt-0">
                            <v-col cols="12" class="pt-2 align-end">
                                <span class="text--secondary d-inline-flex">
                                    Uh! This was tiring.
                                </span>
                            </v-col>
                        </v-row>

                        <v-row class="ma-4 mt-0" justify="space-between">
                            <v-col cols="4">
                                <v-btn text type="button" @click="backToAddress" large color="secondary">
                                    Go back
                                </v-btn>
                            </v-col>
                            <v-col cols="4">
                                <v-btn type="submit" depressed large color="primary">
                                    Sign up
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-stepper-content>
                <v-stepper-content step="6" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="display-2">Success!</span>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">{{ this.name }}, your request was sent.</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">Administrators will review it
                                and, if everything is ok, you will soon get the activation mail.</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mt-0">
                        <v-col cols="12" class="text-right">
                            <v-btn @click="$router.push('/auth')" depressed large color="primary">
                                Fine.
                            </v-btn>
                        </v-col>
                    </v-row>
                </v-stepper-content>
            </v-stepper-items>
        </v-stepper>
    </v-container>
</template>

<script>
    import {emailPattern} from "../../utils/skeletons";
    import Vue from "vue";

    export default {
        name: "RegisterView",
        data: () => ({
            registerStep: 1,
            newUser: {
                email: null,
                password: null,
                name: null,
                surname: null,
                personalID: null,
                country: null,
                city: null,
                address: null,
                phoneNumber: null,
                type: 0
            },
            formElements: {
                email: null,
                password: null,
                repeatPassword: null,
                firstName: null,
                lastName: null,
                personalID: null,
                country: null,
                city: null,
                address: null,
                phoneNumber: null,
            },
            emailError: {
                isError: false,
                errorMessage: ''
            },
            name: null,
            passwordError: {
                isError: false,
                errorMessage: ''
            },
            rules: {
                required: value => !!value || 'Required.',
                email: value => {
                    return emailPattern.test(value) || 'Invalid e-mail.';
                },
            }
        }),
        computed: {
            // ...mapGetters('auth', ['loggedIn']),
        },
        watch: {
            // loggedIn(val) {
            //     if (val) {
            //         this.$router.push('/');
            //     }
            // }
        },
        methods: {
            emailSelected() {
                this.emailError.isError = false;
                this.emailError.errorMessage = '';
                if (this.$refs.emailForm.validate()) this.exists();
            },
            async exists() {
                try {
                    this.name = (await Vue.prototype.$axios.get(`/api/auth/exists/${this.formElements.email}`)).data;
                    this.emailError.isError = true;
                    this.emailError.errorMessage = `That account already exists.`;
                } catch (err) {
                    this.newUser.email = this.formElements.email;
                    this.registerStep = 2;
                }
            },
            passwordSelected() {
                this.passwordError.isError = false;
                this.passwordError.errorMessage = ``;
                if (this.formElements.password !== this.formElements.repeatPassword) {
                    this.passwordError.isError = true;
                    this.passwordError.errorMessage = `Passwords don't match`;
                } else {
                    this.newUser.password = this.formElements.password;
                    this.registerStep = 3;
                    this.$nextTick(() => {
                        this.$refs.passwordForm.reset();
                    });
                }
            },
            backToEmail() {
                this.$refs.emailForm.reset();

                this.registerStep = 1;
                this.$nextTick(() => {
                    this.$refs.passwordForm.reset();
                });
            },
            nameSelected() {
                this.$refs.nameForm.resetValidation();
                if (this.$refs.nameForm.validate()) {
                    this.newUser.name = this.formElements.firstName;
                    this.newUser.surname = this.formElements.lastName;
                    this.newUser.personalID = this.formElements.personalID;
                    this.registerStep = 4;
                }
            },
            backToPassword() {
                this.$refs.passwordForm.reset();

                this.registerStep = 2;
                this.$nextTick(() => {
                    this.$refs.nameForm.reset();
                });
            },
            addressSelected() {
                this.$refs.addressForm.resetValidation();
                if (this.$refs.addressForm.validate()) {
                    this.newUser.country = this.formElements.country;
                    this.newUser.city = this.formElements.city;
                    this.newUser.address = this.formElements.address;
                    this.registerStep = 5;
                }
            },
            backToName() {
                this.$refs.nameForm.reset();

                this.registerStep = 3;
                this.$nextTick(() => {
                    this.$refs.addressForm.reset();
                });
            },
            phoneSelected() {
                this.$refs.phoneForm.resetValidation();
                if (this.$refs.phoneForm.validate()) {
                    this.newUser.phoneNumber = this.formElements.phoneNumber;
                    this.registerApi();
                }
            },
            backToAddress() {
                this.$refs.addressForm.reset();

                this.registerStep = 4;
                this.$nextTick(() => {
                    this.$refs.phoneForm.reset();
                });
            },
            async registerApi() {
                try {
                    let uUser = {
                        user: this.newUser,
                        emailSent: false
                    };
                    let {data: res} = await this.$axios.post('/api/auth/register', uUser);
                    this.name = res.user.name;
                    this.registerStep = 6;
                } catch (err) {

                }
            }
        }
    }
</script>

<style scoped>

</style>
