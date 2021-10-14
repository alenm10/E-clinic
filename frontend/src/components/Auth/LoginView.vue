<template>
    <v-container fluid class="pa-0">
        <v-progress-linear
                :active="loading"
                :indeterminate="loading"
                absolute
                top
                height="3px"
                color="primary"
        ></v-progress-linear>
        <v-stepper v-model="loginStep" class="elevation-0 pa-0">
            <v-stepper-items>
                <v-stepper-content step="1" class="pa-0">
                    <v-row align="center" class="mt-9" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0"><span class="display-1">Sign in</span></v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0"><span
                                class="subtitle-1">to continue to eClinic</span></v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0"><span class="grey--text text--darken-1">First, tell us your email:</span>
                        </v-col>
                    </v-row>
                    <v-form ref="emailForm" @submit.prevent="checkEmail" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Email"
                                        name="email"
                                        :rules="[rules.required, rules.email]"
                                        :error="emailError.isError"
                                        :error-messages="emailError.errorMessage"
                                        v-model="formElements.email"
                                        outlined
                                />
                            </v-col>
                        </v-row>

                        <v-row class="ma-4 mt-0">
                            <v-col cols="12" class="pt-2 align-end">
                                <span class="text--secondary d-inline-flex">
                                    Don't have an account?
                                </span>
                                <v-btn type="button" text color="primary" @click="$router.push('/auth/register')"
                                       class="px-2" small>
                                    apply here
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
                            <span v-if="name" class="headline">Hello, {{ name }}</span>
                        </v-col>
                    </v-row>
                    <v-row align="center" justify="center">
                        <v-col cols="12" md="8" class="text-center py-0">
                            <span class="subtitle-1">{{ authRequest.email }}</span>
                        </v-col>
                    </v-row>
                    <v-row class="ma-4 mb-0">
                        <v-col cols="12" md="8" class="pb-0">
                            <span class="grey--text text--darken-1">Now, tell us your password.</span>
                        </v-col>
                    </v-row>
                    <v-form ref="passwordForm" @submit.prevent="postLogin" lazy-validation>
                        <v-row class="ma-4 mt-0 mb-0">
                            <v-col cols="12" class="pb-2">
                                <v-text-field
                                        label="Password"
                                        name="password"
                                        :rules="[rules.required]"
                                        :error="loginError.isError"
                                        :error-messages="loginError.errorMessage"
                                        v-model="formElements.password"
                                        type="password"
                                        outlined
                                />
                            </v-col>
                        </v-row>

                        <v-row class="ma-4 mt-0">
                            <v-col cols="12" class="pt-2 align-end">
                                <span class="text--secondary d-inline-flex">
                                    Don't worry. We won't tell anyone.
                                </span>
                            </v-col>
                        </v-row>
                        <v-row class="ma-4 mt-0" justify="space-between">
                            <v-col cols="4">
                                <v-btn text type="button" large color="secondary" @click="returnToEmail">
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
            </v-stepper-items>
        </v-stepper>
    </v-container>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import {emailPattern} from "../../utils/skeletons";

    import Vue from "vue";
    import {loginState} from "../../store/AuthModule";

    export default {
        name: "LoginView",
        data: () => ({
            loginStep: 1,
            loading: false,
            emailError: {
                isError: false,
                errorMessage: ''
            },
            loginError: {
                isError: false,
                errorMessage: ''
            },
            authRequest: {
                email: null,
                password: null,
            },
            formElements: {
                email: null,
                password: null,
            },
            name: null,
            rules: {
                required: value => !!value || 'Required.',
                email: value => {
                    return emailPattern.test(value) || 'Invalid e-mail.';
                },
            }
        }),
        computed: {
            ...mapState('auth', ['loggedIn']),
        },
        watch: {
            loginStep(val) {
            },
            loggedIn(val) {
                this.loading = false;

                if (val === loginState.LOGGED) {
                    this.$router.push('/');
                } else if (val === loginState.NOT_LOGGED) {
                    this.loginError.isError = true;
                    this.loginError.errorMessage = 'Incorrect password';
                } else if (val === loginState.LOGGING) {
                    this.loading = true;
                }
            }
        },
        methods: {
            ...mapActions('auth', ['login']),
            postLogin() {
                if (this.$refs.passwordForm.validate()) {
                    this.authRequest.password = this.formElements.password;
                    this.login(this.authRequest);
                }
            },
            checkEmail() {
                this.emailError.isError = false;
                this.emailError.errorMessage = '';
                let valid = this.$refs.emailForm.validate();
                if (valid) {
                    this.loading = true;
                    this.exists()
                }
            },
            async exists() {
                try {
                    this.name = (await Vue.prototype.$axios.get(`/api/auth/exists/${this.formElements.email}`)).data;
                    this.authRequest.email = this.formElements.email;
                    this.loginStep = 2;
                } catch (err) {
                    this.emailError.isError = true;
                    this.emailError.errorMessage = `Couldn't find your account`;
                } finally {
                    this.loading = false;
                }
            },
            returnToEmail() {
                this.$refs.emailForm.reset();

                this.loginStep = 1;
                this.$nextTick(() => {
                    this.$refs.passwordForm.reset();
                });
            },
            goBack(event) {
                alert('GOBACK');
                if (this.loginStep === 2) {
                    event.stop();
                    this.returnToEmail();
                }
            }
        },
        mounted() {
            document.addEventListener("backbutton", this.goBack, false);
        },
        destroyed() {
            document.removeEventListener("backbutton", this.goBack);
        }
    }
</script>

<style scoped>

</style>
