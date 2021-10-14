<template>
    <v-autocomplete
            :value="value"
            @input="$emit('input', $event)"
            hint="Choose diagnoses"
            :items="diseases"
            label="Diagnoses"
            persistent-hint
			item-text="name"
            item-value="name"
            return-object
            :loading="loading"
            :multiple="multiple"
            :chips="multiple"
    >
        <template v-slot:no-data>
            <v-list-item>
                <v-list-item-title>
                    There are no defined diagnoses
                </v-list-item-title>
            </v-list-item>
        </template>

    </v-autocomplete>
</template>

<script>

    import {mapActions} from "vuex";
    import JSOG from 'jsog';

    export default {
        name: "DiseaseSelection",
        data: () => ({
            loading: false,
        }),
        props: {
            value: null,
            multiple: {
                type: Boolean,
                value: true
            }
        },
        computed: {

            diseases(){
                return JSOG.decode(this.$store.state.disease.disease.diseases);
            }
        },
        watch: {
            diseases() {
                this.loading = false;
            }
        },
        methods: {
            ...mapActions('disease/disease', ['getDiseases']),
        },
        mounted() {
            this.loading = true;
            console.log("getDiseases");
            this.getDiseases();
        }
    }
</script>

<style scoped>

</style>
