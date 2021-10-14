<template>
    <v-autocomplete
            :value="value"
            @input="$emit('input', $event)"
            hint="Choose a clinic"
            :items="clinics"
            label="Clinic*"
            persistent-hint
            item-text="name"
            item-value="name"
            :rules="rules"
            return-object
            prepend-icon="mdi-hospital-building"
    >
        <template v-slot:no-data>
            <v-list-item>
                <v-list-item-title>
                    There are no existing clinics
                </v-list-item-title>
            </v-list-item>
        </template>

    </v-autocomplete>
</template>

<script>
    // ****************************
    // *  OVO URADITI ASINHRONO   *
    // ****************************
    // MOGUCE PRIMENITI:
    // V-MODEL koji je izabrana klinika.

    import {mapActions, mapState} from "vuex";

    export default {
        name: "ClinicSelection",
        data: () => ({
            rules: [v => !!v || 'Clinic is required'],
        }),
        props: {
            value: null,
            disabled: {
                type: Boolean,
                value: false,
            },
        },
        computed: {
            ...mapState('clinics/readClinics', ['clinics']),
        },
        methods: {
            ...mapActions('clinics/readClinics', ['getClinics']),
        },
        mounted() {
            this.getClinics({
                pageNumber: 1,
                pageSize: -1,
            });
        }
    }
</script>

<style scoped>

</style>
