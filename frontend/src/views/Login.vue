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
                        <v-card outlined>
                            <transition mode="out-in" name="fade">
                                <router-view/>
                            </transition>
                        </v-card>
                    </v-col>
                </v-row>

            </v-container>
        </v-content>
    </v-app>
</template>

<script>
    import RegisterView from "../components/Auth/RegisterView";
    import LoginView from "../components/Auth/LoginView";

    export default {
        name: "Login",
        components: {RegisterView, LoginView},
    }
</script>

<style scoped>
    .fade-enter-active, .fade-leave-active {
        transition: all .5s
    }

    .fade-enter, .fade-leave-to {
        opacity: 0;
    }
</style>
