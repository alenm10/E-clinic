<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="600"
            @keydown.enter="$emit('close')"
    >
        <v-card>
            <v-card-title class="headline">{{ clinicName }}</v-card-title>

            <v-card-text>
                {{ clinicDescription }}
            </v-card-text>

            <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                        color="green darken-1"
                        text
                        @click="$emit('close')"
                >
                    Close
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        name: "DescriptionDialog",
        data: () => ({}),
        computed: {
            clinicName() {
                return this.clinic ? this.clinic.name : '';
            },
            clinicDescription() {
                return this.clinic ? this.clinic.description : '';
            }
        },
        props: ['clinic', 'value'],
        methods: {}
    }
</script>

<style scoped>

</style>
