<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="600px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add a clinic room</span>
                <span v-else class="headline">Update clinic room</span>
            </v-card-title>
            <v-card-subtitle v-if="role !== adminCode">
                <span class="subtitle-1">For the clinic {{ clinic.name }}</span>
            </v-card-subtitle>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                        <v-row>
                            <v-col cols="12">
                                <v-text-field
                                        label="Name*"
                                        prepend-icon="mdi-form-textbox"
                                        v-model="name"
                                        :rules="nameRules"
                                        required
                                ></v-text-field>
                            </v-col>
                            <v-col cols="12">
                                <clinic-selection v-if="role === adminCode" v-model="selectedClinic" :disabled="mode === `update`"/>
                            </v-col>
                        </v-row>
                    </v-form>
                </v-container>
                <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Close</v-btn>
                <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(addClinicRoomApi)">Add</v-btn>
                <v-btn color="blue darken-1" v-else text @click="submit(updateClinicRoomApi)">Update</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    import ClinicSelection from "../Clinics/ClinicSelection";
    import {mapActions, mapGetters, mapState} from "vuex";
    import {emptyClinicRoom} from "../../utils/skeletons";
    import {ClinicalAdmin, ClinicalCenterAdmin} from "../../utils/DrawerItems";

    export default {
        name: "ModifyClinicRoomDialog",
        components: {ClinicSelection},
        data: () => ({
            name: null,
            clinicRoom: emptyClinicRoom,
            selectedClinic: null,
            nameRules: [v => !!v || 'Name is required.'],
            adminCode: ClinicalCenterAdmin.code
        }),
        props: {
            editRoom: null,
            value: false,
            mode: {
                type: String,
                default: 'add'
            }
        },
        computed: {
            ...mapState('auth', ['user']),
            ...mapState('auth', ['clinic']),
            ...mapState('auth', ['role']),
            ...mapGetters('clinics/readClinics', ['clinicById']),
        },
        watch: {
            value() {



                console.log("KLINIKAAA")

                if (this.editRoom) {
                    this.clinicRoom = this.editRoom;
                    this.name = this.clinicRoom.name;
                }
                else{

                    this.name = "";
                }
                this.selectedClinic = this.clinicById(this.clinicRoom.clinicId);
            }
        },
        methods: {

            ...mapActions('clinicRooms/clinicRooms', ['addClinicRoomApi']),
            ...mapActions('clinicRooms/clinicRooms', ['updateClinicRoomApi']),

            submit(fun) {
                if (this.$refs.form.validate()) {

                    this.clinicRoom.name = this.name;
                    if(this.role === this.adminCode) this.clinicRoom.clinicId = this.selectedClinic.id;
                    else this.clinicRoom.clinicId = this.clinic.id;
                    fun(this.clinicRoom);
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
