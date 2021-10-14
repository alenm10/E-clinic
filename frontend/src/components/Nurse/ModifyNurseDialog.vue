<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent
            max-width="900px"
    >
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add a nurse</span>
                <span v-else class="headline">Update nurse</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                        <v-row>
                            <v-col cols="12" sm="6" lg="8">
                                <v-text-field
                                        label="Email*"
                                        required
                                        :rules="[rules.required('Email')]"
                                        v-model="modifiableNurse.email"
                                >
                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="4">
                                   <v-text-field
                                          ref="password"
                                          label="Password*"
                                          required
                                          v-model="modifiableNurse.password"
                                          :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                                          :rules="[rules.required('Password')]"
                                          :type="showPassword ? 'text' : 'password'"
                                          name="input-10-2"
                                          hint="At least 8 characters"
                                          class="input-group--focused"
                                          @click:append="showPassword = !showPassword"
                                  ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="First name*"
                                        required
                                        :rules="[rules.required('First name')]"
                                        v-model="modifiableNurse.name"
                                >
                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="Last name*"
                                        required
                                        :rules="[rules.required('Last name')]"
                                        v-model="modifiableNurse.surname"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="4">
                                <v-text-field
                                        label="Phone number"
                                        required
                                        :rules="[rules.required('Phone number')]"
                                        v-model="modifiableNurse.phoneNumber"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="8">
                                <v-text-field
                                        label="Address"
                                        required
                                        :rules="[rules.required('Address')]"
                                        v-model="modifiableNurse.address"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="City"
                                        required
                                        :rules="[rules.required('City')]"
                                        v-model="modifiableNurse.city"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="Country"
                                        required
                                        :rules="[rules.required('Country')]"
                                        v-model="modifiableNurse.country"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12">
                                <v-text-field
                                        label="Personal ID"
                                        required
                                        :rules="[rules.required('Personal ID')]"
                                        v-model="modifiableNurse.personalID"
                                ></v-text-field>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-container>
                <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Close</v-btn>
                <v-btn
                        color="blue darken-1"
                        v-if="mode === `add`"
                        text
                        @click="submit(addNurseApi)"
                >Add
                </v-btn
                >
                <v-btn
                        color="blue darken-1"
                        v-else
                        text
                        @click="submit(updateNurseApi)"
                >Update
                </v-btn
                >
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    import {mapActions, mapState} from "vuex";
    import {emptyNurse} from "../../utils/skeletons";
    import JSOG from 'jsog'
    export default {
        name: "ModifyNurseDialog",
        components: {},
        data: () => ({
            modifiableNurse: emptyNurse,

            nurse: emptyNurse,

            showPassword: false,

            rules: {
                required: fieldName => v => !!v || `${fieldName} is required`,
            },

        }),
        props: {
            editNurse: null,
            value: false,
            mode: {
                type: String,
                default: "add"
            }
        },
        computed: {
            ...mapState('auth', ['user']),
            ...mapState('auth', ['clinic']),
        },
        watch: {
            value() {
                // if (this.editNurse) {
                //     this.nurse = this.editNurse;
                //     console.log("value changed");
                //     console.log("nurseename = " + this.nurse.name);
                // }
                // this.nurse = this.modifiableNurse;
                // console.log("thisname = " + this.name);
            }
        },
        methods: {
            ...mapActions("nurse/nurse", ["addNurseApi"]),
            ...mapActions("nurse/nurse", ["updateNurseApi"]),
            submit(fun) {
                this.nurse.clinic = this.clinic.id;
                if (this.$refs.form.validate()) {
                    this.nurse.clinicID = this.clinic.id;
                    this.nurse = JSOG.parse(JSOG.stringify(this.modifiableNurse));
                    fun(this.nurse);
                    this.close();
                }
            },
            close() {
                console.log("close");
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            },
            resetLayout() {
                console.log("resetlayout");
                this.nurse = emptyNurse;
                this.$refs.form.reset();
            }
        }
    };
</script>

<style scoped></style>
