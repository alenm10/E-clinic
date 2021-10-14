<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="500px"
    >
        <v-card>
            <v-card-title class="headline">Name</v-card-title>

            <v-card-subtitle>
                Change your name here.
            </v-card-subtitle>

            <v-form @submit.prevent="save">
                <v-card-text>
                    <v-text-field
                            label="First name"
                            v-model="firstName"
                            outlined
                    >
                    </v-text-field>
                    <v-text-field
                            label="Last name"
                            v-model="lastName"
                            outlined
                    >
                    </v-text-field>

                </v-card-text>
                <v-card-actions>

                    <v-btn
                            color="primary"
                            text
                            @click="$emit('input', false)"
                    >
                        Close
                    </v-btn>
                    <v-spacer></v-spacer>

                    <v-btn
                            color="primary"
                            type="submit"
                    >
                        Save
                    </v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
    import {emptyUser} from "../../utils/skeletons";

    export default {
        name: "NameDialog",
        data: () => ({
            firstName: null,
            lastName: null
        }),
        watch: {
            user(newUser) {
                this.firstName = newUser.name;
                this.lastName = newUser.surname;
            }
        },
        props: {
            value: {
                type: Boolean,
                default: false
            },
            user: emptyUser
        },
        methods:{
            save() {
                let modifiedUser = this.user;
                modifiedUser.name = this.firstName;
                modifiedUser.surname = this.lastName;
                this.$emit('input', false);
                this.$emit('modify', modifiedUser);
            }
        },
    }
</script>

<style scoped>

</style>
