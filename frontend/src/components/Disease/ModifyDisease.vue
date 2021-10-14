<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="600px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add diagnosis</span>
                <span v-else class="headline">Update diagnosis</span>
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
                        </v-row>
                    </v-form>
                </v-container>
                <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Close</v-btn>
                <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(addDiseaseApi)">Add</v-btn>
                <v-btn color="blue darken-1" v-else text @click="submit(updateDiseaseApi)">Update</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import {mapActions} from "vuex";
    import {emptyDisease} from "../../utils/skeletons";

    export default {
        name: "ModifyDisease",
        components: {},
        data: () => ({
            name: null,
            id: "",
            diagnosis: emptyDisease,
            nameRules: [v => !!v || 'Name is required.']
        }),
        props: {
            editDisease: null,
            value: false,
            mode: {
                type: String,
                default: 'add'
            }
        },
        watch: {
            value() {
                if (this.editDisease) {
                    this.diagnosis = this.editDisease;
                }
                this.name = this.diagnosis.name;
                this.id = this.diagnosis.id;
            }
        },
        methods: {
            ...mapActions('disease/disease', ['addDiseaseApi']),
            ...mapActions('disease/disease', ['updateDiseaseApi']),

            submit(fun) {
                if (this.$refs.form.validate()) {
                    this.diagnosis.name = this.name;
					this.diagnosis.id = this.id;            
                    fun(this.diagnosis);
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
