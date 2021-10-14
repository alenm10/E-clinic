<template>
    <v-app>
        <v-content>
            <v-container
                    class="fill-height"
                    fluid
            >
                <v-row
                        align="center"
                        justify="center"
                        class="mt-0"
                >
                    <v-col
                            cols="12"
                            sm="8"
                            md="4"
                    >
                        <v-card outlined min-height="300">
                            <v-skeleton-loader
                                    :loading="loading"
                                    transition="fade-transition"
                                    height="300px"
                                    type="article"
                            >
                                <v-container v-if="!success">
                                    <v-row align="center" class="mt-9" justify="center">
                                        <v-col cols="12" md="8" class="text-center py-0">
                                            <span class="display-2">We are sorry!</span>
                                        </v-col>
                                    </v-row>
                                    <v-row align="center" justify="center">
                                        <v-col cols="12" md="8" class="text-center py-0">
                                            <span class="subtitle-1">Your verification was unsuccessful.</span>
                                        </v-col>
                                    </v-row>
                                    <v-row class="ma-4 mb-0">
                                        <v-col cols="12" md="8" class="pb-0">
                                            <span class="grey--text text--darken-1">{{ errorMessage }}</span>
                                        </v-col>
                                    </v-row>
                                    <v-row class="ma-4 mt-0">
                                        <v-col cols="12" class="text-right">
                                            <v-btn @click="$router.push('/auth')" depressed large color="primary">
                                                Bye!
                                            </v-btn>
                                        </v-col>
                                    </v-row>
                                </v-container>
                                <v-container v-else>
                                    <v-row align="center" class="mt-9" justify="center">
                                        <v-col cols="12" md="8" class="text-center py-0">
                                            <span class="display-2">Success!</span>
                                        </v-col>
                                    </v-row>
                                    <v-row align="center" justify="center">
                                        <v-col cols="12" md="8" class="text-center py-0">
                                            <span class="subtitle-1">{{ this.name }}, your account was activated.</span>
                                        </v-col>
                                    </v-row>
                                    <v-row class="ma-4 mb-0">
                                        <v-col cols="12" md="8" class="pb-0">
                                            <span class="grey--text text--darken-1">You successfully activated your account.
                                                You can now sign in.
                                            </span>
                                        </v-col>
                                    </v-row>
                                    <v-row class="ma-4 mt-0">
                                        <v-col cols="12" class="text-right">
                                            <v-btn @click="$router.push('/auth')" depressed large color="primary">
                                                Sign in.
                                            </v-btn>
                                        </v-col>
                                    </v-row>
                                </v-container>
                            </v-skeleton-loader>
                        </v-card>
                    </v-col>
                </v-row>

            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    export default {
        name: "ConfirmRegistration",
        data: () => ({
            loading: true,
            success: false,
            errorMessage: ''
        }),
        mounted() {
            this.confirm();
        },
        methods: {
            async confirm() {
                try {
                    let {data: name} = await this.$axios.get(`/api/auth/confirmRegistration/${this.$route.params.token}`);
                    this.name = name;
                    this.success = true;
                } catch (err) {
                    if (err.response.status === 404) {
                        this.errorMessage = 'Make sure the URL is correct.'
                    } else {
                        this.errorMessage = 'This URL has expired. Please contact admins in order to send you another.';
                    }
                } finally {
                    this.loading = false;
                }
            }
        }
    }
</script>

<style scoped>

</style>
