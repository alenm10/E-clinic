<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="600px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add medical record</span>
                <span v-else class="headline">Update medical record</span>
            </v-card-title>
            <v-form @submit.prevent="submit" ref="form">
                <v-card-text>
                    <v-container>
                            <v-row>
                                <v-col cols="6">
                                    <v-text-field
                                            label="Height"
                                            suffix="cm"
                                            v-model="height"
                                            :rules="heightRules"
                                            required
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="6">
                                    <v-text-field
                                            label="Weight"
                                            suffix="kg"
                                            v-model="weight"
                                            :rules="weightRules"
                                            required
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="3">
                                    <v-select
                                            :items="bloodTypes"
                                            v-model="bloodType"
                                            :rules="[v => !!v || 'Blood type is required']"
                                            label="Blood type"
                                    ></v-select>
                                </v-col>
                                <v-col cols="9">
                                    <v-combobox
                                            v-model="allergies"
                                            hide-selected
                                            label="Add allergies"
                                            multiple
                                            small-chips
                                    >
                                        <template v-slot:no-data>
                                            <v-list-item>
                                                <v-list-item-content>
                                                    <v-list-item-title>
                                                        Press <kbd>enter</kbd> to create a new allergy
                                                    </v-list-item-title>
                                                </v-list-item-content>
                                            </v-list-item>
                                        </template>
                                    </v-combobox>
                                </v-col>
                            </v-row>
                    </v-container>
                    <small>*indicates required field</small>
                </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn type="button" color="blue darken-1" text @click="close">Close</v-btn>
                <v-btn type="submit" color="blue darken-1" v-if="mode === `add`" text>Add</v-btn>
                <v-btn type="submit" color="blue darken-1" v-else text>Update</v-btn>
            </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapMutations, mapState} from "vuex";
    import {emptyMedicine} from "../../utils/skeletons";

    export default {
        name: "ModifyMedicalRecord",
        components: {},
        data: () => ({
            height: '',
            weight: '',
            bloodType: '',
            allergies: [],
            medicine: emptyMedicine,
            heightRules: [v => !!v || 'Height is required.'],
            weightRules: [v => !!v || 'Weight is required.'],
            bloodTypes: [
                'A+', 'A-',
                'B+', 'A-',
                'AB+', 'AB-',
                '0+', '0-'
            ]
        }),
        props: {
            patient: null,
            value: false,
            mode: {
                type: String,
                default: 'add'
            }
        },
        computed: {
           ...mapState('auth', ['token']),
        },
        watch: {
            value() {
                if(!this.patient) return;
                this.height = this.patient.medicalRecord.height;
                this.weight = this.patient.medicalRecord.weight;
                this.bloodType = this.patient.medicalRecord.bloodType;
                this.allergies = this.patient.medicalRecord.allergies;
            }
        },
        methods: {
            ...mapMutations('patients', ['updatePatient']),
            async saveMedicalRecord(record) {
                let {data: patient} = await this.$axios.post(`/api/patient/medical-record/${this.patient.userID}`,
                    record,
                    {headers: {"Authorization": 'Bearer ' + this.$store.state.auth.token}});
                this.updatePatient(patient);
            },
            async updateMedicalRecord(record) {
                let {data: patient} = await this.$axios.put(`/api/patient/medical-record/${this.patient.userID}`,
                    record,
                    {headers: {"Authorization": 'Bearer ' + this.$store.state.auth.token}});
                this.updatePatient(patient);
            },
            submit() {
                let fun = this.mode === 'add' ? this.saveMedicalRecord : this.updateMedicineApi;
                if (this.$refs.form.validate()) {
                    let medicalRecord = {
                        height: this.height,
                        weight: this.weight,
                        bloodType: this.bloodType,
                        allergies: this.allergies
                    };
                    fun(medicalRecord);
                    this.close();
                }
            },
            close() {
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            }
        }
    }
</script>

<style scoped>

</style>
