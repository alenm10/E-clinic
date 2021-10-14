<template>
    <v-container fluid class="ma-2">
            <v-row align="center">
                <v-col cols="12" md="1">
                    <span class="headline">Patients</span>
                </v-col>
                <v-col cols="12" md="11">
                    <v-form ref="form" @submit.prevent="search">

                        <v-row align="center" justify="space-around">
                            <v-col cols="12" sm="4" md="3">
                                <v-text-field
                                        label="First name"
                                        v-model="firstName"
                                >

                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="4" md="3">
                                <v-text-field
                                        label="Last name"
                                        v-model="lastName"
                                >

                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="4" md="3">
                                <v-text-field
                                        label="Personal ID"
                                        v-model="personalID"
                                >

                                </v-text-field>
                            </v-col>



                            <v-col cols="6" sm="2" lg="1" class="text-center">
                                <v-btn color="primary" type="submit">
                                    Search
                                </v-btn>
                            </v-col>
                            <v-col cols="6" sm="2" lg="1">
                                <v-btn text @click="reset">
                                    Reset
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>

                </v-col>

            </v-row>
        <v-card outlined v-if="type">
            <div class="subtitle-1">The price for {{type.name}} is ${{type.price}}.</div>
            <div class="subtitle-1">This appointment will start on {{date}}</div>
        </v-card>
    </v-container>
</template>

<script>
    import InterventionTypeSelection from "../InterventionType/InterventionTypeSelection";
    import JSOG from 'jsog';
    import {mapState} from "vuex";
    import {Patient} from "../../utils/DrawerItems";
    export default {
        name: "PatientSearch",
        components: {InterventionTypeSelection},
        data: () => ({
            menu: false,
            firstName: '',
            lastName: '',
            personalID: '',
            rules: {
                required: fieldName => v => !!v || `${fieldName} is required`,
            },
        }),
        props: {
            value: {
                type: Boolean,
                default: false
            },
            selectedFirstName: {
                type: String,
                default: ''
            },
            selectedLastName: {
                type: String,
                default: ''
            },
            selectedPersonalId: {
                type: String,
                default: ''
            }
        },
        watch: {
            selectedFirstName(val) {
                this.firstName = val;
            },
            selectedLastName(val) {
                this.lastName = val;
            },
            selectedPersonalId(val) {
                this.personalID = val;
            },
        },
        methods: {
            search() {
                if(!this.$refs.form.validate()) return;
                if(!this.firstName) this.firstName = '';
                if(!this.lastName) this.lastName = '';
                if(!this.personalID) this.personalID = '';
                this.$emit('searched', {
                    firstName: this.firstName.trim().toLowerCase(),
                    lastName: this.lastName.trim().toLowerCase(),
                    personalID: this.personalID.trim().toLowerCase()
                });
                this.$emit('update:selectedType', this.type);
            },
            reset() {
                this.$emit('reset');
                this.$refs.form.reset();
            }
        },
        computed: {
            ...mapState('auth', ['role']),
            today() {
                let tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
                return (new Date(Date.now() - tzoffset)).toISOString().slice(0, -1);
            }
        }
    }
</script>

<style scoped>

</style>
