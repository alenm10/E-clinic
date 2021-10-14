<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent
            max-width="900px"
    >
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add a doctor</span>
                <span v-else class="headline">Update doctor</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                        <v-row>
                            <v-col cols="12">
                                <intervention-type-selection
                                        v-model="modifiableDoctor.specialties"
                                        :clinic-id="$route.params.clinicID"
                                        multiple
                                ></intervention-type-selection>
                            </v-col>
                            <v-col cols="12" sm="6" lg="8">
                                <v-text-field
                                        label="Email*"
                                        required
                                        :rules="[rules.required('Email')]"
                                        v-model="modifiableDoctor.email"
                                >
                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="4">
                                   <v-text-field
                                          ref="password"
                                          label="Password*"
                                          required
                                          v-model="modifiableDoctor.password"
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
                                        :rules="[rules.required('Name')]"
                                        v-model="modifiableDoctor.name"
                                >
                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6">
                                <v-text-field
                                        label="Last name*"
                                        required
                                        :rules="[rules.required('Last name')]"
                                        v-model="modifiableDoctor.surname"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="4">
                                <v-text-field
                                        label="Phone number"
                                        required
                                        :rules="[rules.required('Phone number')]"
                                        v-model="modifiableDoctor.phoneNumber"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="8">
                                <v-text-field
                                        label="Personal ID"
                                        required
                                        :rules="[rules.required('Personal ID')]"
                                        v-model="modifiableDoctor.personalID"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="7">
                                <v-text-field
                                        label="Address"
                                        required
                                        :rules="[rules.required('Address')]"
                                        v-model="modifiableDoctor.address"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="3">
                                <v-text-field
                                        label="City"
                                        required
                                        :rules="[rules.required('City')]"
                                        v-model="modifiableDoctor.city"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" lg="2">
                                <v-text-field
                                        label="Country"
                                        required
                                        :rules="[rules.required('Country')]"
                                        v-model="modifiableDoctor.country"
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
                        @click="submit(addDoctorApi)"
                >Add
                </v-btn
                >
                <v-btn
                        color="blue darken-1"
                        v-else
                        text
                        @click="submit(updateDoctorApi)"
                >Update
                </v-btn
                >

            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    import {mapActions, mapState} from "vuex";
    import {emptyDoctor} from "../../utils/skeletons";
    import InterventionTypeSelection from "../InterventionType/InterventionTypeSelection";
    import JSOG from 'jsog';
    export default {
        name: "ModifyDoctorDialog",
        components: {InterventionTypeSelection},
        data: () => ({
            modifiableDoctor: emptyDoctor,

            doctor: emptyDoctor,

            showPassword: false,

            rules: {
                required: fieldName => v => !!v || `${fieldName} is required`,
            },

        }),
        props: {
            editDoctor: null,
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
                if (this.editDoctor) {
                    this.doctor = this.editDoctor;
                }
                this.modifiableDoctor = emptyDoctor;
            }
        },
        created() {
        },
        methods: {
            ...mapActions("doctor/doctor", ["addDoctorApi"]),
            ...mapActions("doctor/doctor", ["updateDoctorApi"]),
            submit(fun) {
                console.log(this.clinic);
                if (!this.clinic) return;
                if (this.$refs.form.validate()) {
                    this.doctor = JSOG.parse(JSOG.stringify(this.modifiableDoctor));
                    this.doctor.clinicID = this.clinic.id;
                    this.doctor.specialties = this.doctor.specialties.map(type => type.id);
                    fun(this.doctor);
                    this.close();
                }
            },
            close() {
                console.log("close");
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            }
        }
    };
</script>

<style scoped></style>
