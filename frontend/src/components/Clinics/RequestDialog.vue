<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="600"
            @keydown.enter="$emit('close')"
    >
        <v-card>
            <v-card-title class="headline">Appointment</v-card-title>

            <v-card-text class="subtitle-1">
                <div>The appointment will be done for {{typeName}}</div>
                <div>It will cost you ${{typePrice}}.</div>
                <div>The clinic where that appointment will be done is called
                    <span class="text--primary text--darken-2">{{clinicName}}</span>.</div>
                <div class="grey--text text--darken-2">The date of the appointment is {{date | filterDate}}.</div>
                <div class="text--primary text--darken-2">Are you sure that you want to proceed and search for doctors?</div>
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
                        text
                        @click="accept"
                >
                    Sure
                </v-btn>

            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
    export default {
        name: "RequestDialog",
        data: () => ({}),
        computed: {
            clinicName() {
                return this.clinic ? this.clinic.name : '';
            },
            typeName() {
                return this.type ? this.type.name : '';
            },
            typePrice() {
                return this.type ? this.type.price : '';
            }
        },
        props: {
            value: {
                type: Boolean,
                default: false
            },
            clinic: null,
            date: null,
            type: null
        },
        methods: {
            accept() {
                this.$emit('continued', {
                    clinic: this.clinic,
                    clinicID: this.clinic.id,
                    interventionType: this.type,
                    date: this.date
                });
                this.$emit('close');
            }
        },
        filters:{
            filterDate(date) {
                if (!date) return 'never';
                return new Date(date).toLocaleDateString();
            }
        }
    }
</script>

<style scoped>

</style>
