<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="600"
            @keydown.enter="$emit('close')"
    >
        <v-card>
            <v-card-title class="headline">Appointment</v-card-title>
            <v-form ref="form" @submit.prevent="$emit('schedule')">
                <v-card-text class="subtitle-1">
                    Are you sure that you want to schedule this appointment?
                </v-card-text>
                <v-card-actions>
                    <v-btn
                            color="green darken-1"
                            text
                            @click="$emit('close')"
                    >
                        Close
                    </v-btn>
                    <v-spacer></v-spacer>
                    <v-btn
                            color="green darken-1"
                            type="submit"
                            text
                    >
                        Sure
                    </v-btn>

                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
    import {DayOfTheWeek} from "../../utils/DayOfTheWeek";
    import {mapActions, mapState} from "vuex";

    export default {
        name: "ScheduleDialog",

        props: {
            value: {
                type: Boolean,
                default: false
            }
        }
    }
</script>

<style scoped>

</style>
