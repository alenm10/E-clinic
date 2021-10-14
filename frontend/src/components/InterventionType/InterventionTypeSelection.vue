<template>
    <v-autocomplete
            :value="value"
            @input="$emit('input', $event)"
            hint="Choose intervention type"
            :items="interventionType"
            label="Intervention type*"
            persistent-hint
            item-text="name"
            item-value="name"
            :rules="rules"
            return-object
            :loading="loading"
            prepend-icon="mdi-hospital-building"
            :multiple="multiple"
            :chips="multiple"
    >
        <template v-slot:no-data>
            <v-list-item>
                <v-list-item-title>
                    There are no defined intervention types
                </v-list-item-title>
            </v-list-item>
        </template>

    </v-autocomplete>
</template>

<script>

    import {mapActions, mapState} from "vuex";

    export default {
        name: "InterventionTypeSelection",
        data: () => ({
            rules: [v => !!v || 'Intervention type is required'],
            loading: false,
        }),
        props: {
            value: null,
            disabled: {
                type: Boolean,
                value: false
            },
            multiple: {
                type: Boolean,
                value: false
            },
            clinicId: {
                type: String,
                value: ''
            }
        },
        computed: {
            ...mapState('interventionType/interventionType', ['interventionType']),

        },
        watch: {
            interventionType() {
                this.loading = false;
            }
        },
        methods: {
            ...mapActions('interventionType/interventionType', ['getAllInterventionTypeApi']),
            ...mapActions('interventionType/interventionType', ['getClinicInterventionTypeApi']),
        },
        mounted() {
            this.loading = true;
            if (this.clinicId) {
                this.getClinicInterventionTypeApi(this.clinicId);
            } else {
                this.getAllInterventionTypeApi();
            }
        }
    }
</script>

<style scoped>

</style>
