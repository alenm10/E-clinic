<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            @keydown.enter="$emit('close')"
            max-width="290"
    >
        <v-card>
            <v-card-title class="headline">Schedule</v-card-title>

            <v-card-text>
                Are you sure that you want to schedule <span class="text--primary">{{ roomName }}</span>?
                It will be scheduled for date {{ date }}.
            </v-card-text>

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
                        @click="$emit('schedule')"
                >
                    Yes
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    export default {
        name: "ScheduleDialog",
        props: ['room', 'request', 'value'],
        computed: {
            roomName() {
                return this.room ? this.room.name : '';
            },
            date() {
                return this.request ? this.request.dateTime : '';
            }
        }

    }
</script>

<style scoped>

</style>
