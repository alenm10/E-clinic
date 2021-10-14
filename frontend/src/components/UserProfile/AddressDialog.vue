<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="500px"
    >
        <v-card>
            <v-card-title class="headline">Address</v-card-title>

            <v-card-subtitle>
                Change your address here.
            </v-card-subtitle>

            <v-form @submit.prevent="save">
                <v-container fluid>
                    <v-row>
                        <v-col cols="12">
                            <v-text-field
                                    label="Address"
                                    v-model="address"
                                    outlined
                            >
                            </v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="12" md="6">
                            <v-text-field
                                    label="City"
                                    v-model="city"
                                    outlined
                            >
                            </v-text-field>
                        </v-col>

                        <v-col cols="12" md="6">
                            <v-text-field
                                    label="Country"
                                    v-model="country"
                                    outlined
                            >
                            </v-text-field>

                        </v-col>
                    </v-row>


                </v-container>
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
        name: "AddressDialog",
        data: () => ({
            address: null,
            city: null,
            country: null
        }),
        props: {
            value: {
                type: Boolean,
                default: false
            },
            user: emptyUser

        },
        watch: {
            user(newUser) {
                this.address = newUser.address;
                this.city = newUser.city;
                this.country = newUser.country;
            }
        },
        methods:{
            save() {
                let modifiedUser = this.user;
                modifiedUser.address = this.address;
                modifiedUser.city = this.city;
                modifiedUser.country = this.country;
                this.$emit('input', false);
                this.$emit('modify', modifiedUser);
            }
        }
    }
</script>

<style scoped>

</style>
