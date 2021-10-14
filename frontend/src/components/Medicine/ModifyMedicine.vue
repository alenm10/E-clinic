<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="600px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add medicine</span>
                <span v-else class="headline">Update medicine</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                        <v-row>
                            <v-col cols="6">
                                <v-text-field
                                        label="Name*"
                                        prepend-icon="mdi-form-textbox"
                                        v-model="name"
                                        :rules="nameRules"
                                        required
                                ></v-text-field>
                            </v-col>
                            <v-col cols="6">
                                <v-text-field
                                        label="Manufacturer*"
                                        prepend-icon="mdi-form-textbox"
                                        v-model="manufacturer"
                                        :rules="manufacturerRules"
                                        required
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
                <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(addMedicineApi)">Add</v-btn>
                <v-btn color="blue darken-1" v-else text @click="submit(updateMedicineApi)">Update</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapActions} from "vuex";
    import {emptyMedicine} from "../../utils/skeletons";

    export default {
        name: "ModifyMedicine",
        components: {},
        data: () => ({
            name: null,
            manufacturer: null,
            medicine: emptyMedicine,
            nameRules: [v => !!v || 'Name is required.'],
            manufacturerRules: [v => !!v || 'Manufacturer is required.']
        }),
        props: {
            editMedicine: null,
            value: false,
            mode: {
                type: String,
                default: 'add'
            }
        },
        watch: {
            value() {
                if (this.editMedicine) {
                    this.medicine = this.editMedicine;
                }
                this.name = this.medicine.name;
                this.manufacturer = this.medicine.manufacturer
            }
        },
        methods: {
            ...mapActions('medicines/medicines', ['addMedicineApi']),
            ...mapActions('medicines/medicines', ['updateMedicineApi']),

            submit(fun) {
                if (this.$refs.form.validate()) {
                    this.medicine.name = this.name;
                    this.medicine.manufacturer = this.manufacturer;
                    fun(this.medicine);
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
