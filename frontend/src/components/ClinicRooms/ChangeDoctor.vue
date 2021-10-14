<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            @keydown.enter="$emit('close')"
            max-width="290"
    >
        <v-card>
            <v-card-title class="headline">Change doctor</v-card-title>

            <v-card-text>
                <div class="red--text darken-1">{{ errorMessage }}</div>
                <div class="grey--text">You should choose a new doctor that can perform an appointment on {{ date }}.</div>
            </v-card-text>
            <doctor-selection
                    v-model="doctor"
                    :clinic-id="clinicID"
                    :date-time="new Date(date)"
                    :type-id="request.interventionTypeID"
            ></doctor-selection>
            <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                        ref="btCloseDelete"
                        color="green darken-1"
                        text
                        @click="$emit('close')"
                >
                    No
                </v-btn>

                <v-btn
                        color="green darken-1"
                        text
                        @click="$emit('doctorChanged', doctor)"
                >
                    Yes
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    import DoctorSearch from "../Doctor/DoctorSearch";
    import DoctorSelection from "../Doctor/DoctorSelection";
    export default {
        name: "ChangeDoctor",
        components: {DoctorSelection, DoctorSearch},
        data: () => ({
            doctor: null,
        }),
        props: {
            value: {
                type: Boolean,
                default: false
            },
            request: null,
            room: null,
            errorMessage: {
                type: String,
                default: ''
            }
        },
        computed: {
            roomName() {
                return this.room ? this.room.name : '';
            },
            date() {
                return this.request ? this.request.dateTime : '';
            },
            clinicID() {
                return this.request ? this.request.clinicID : '';
            }
        }

    }
</script>

<style scoped>

</style>
