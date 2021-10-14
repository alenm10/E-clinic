<template>
    <v-autocomplete
            :value="value"
            @input="$emit('input', $event)"
            hint="Choose medicines"
            :items="medicines"
            label="Medicines"
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
                    There are no defined medicines
                </v-list-item-title>
            </v-list-item>
        </template>

    </v-autocomplete>
</template>

<script>

    import {mapActions} from "vuex";
    import JSOG from 'jsog';

    export default {
        name: "MedicineSelection",
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
            medicines() {
                return JSOG.decode(this.$store.state.medicines.medicines.medicines);
            }
        },
        watch: {
            medicines() {
                this.loading = false;
            }
        },
        methods: {
            ...mapActions('medicines/medicines', ['getMedicines']),
        },
        mounted() {
            this.loading = true;
            console.log("get diagnoses");
            this.getMedicines();
        }
    }
</script>

<style scoped>

</style>
