<template>
    <v-container fluid class="ma-2">
            <v-row align="center">
                <v-col cols="12" md="1">
                    <span class="headline">Doctor</span>
                </v-col>
                <v-col cols="12" md="11" v-if="role === patientCode && value">
                    <v-form ref="form" @submit.prevent="search">

                        <v-row align="center" justify="space-around">
                            <v-col cols="12" sm="4" md="3">
                                <v-text-field
                                        prepend-icon="mdi-magnify"
                                        label="Name"
                                        v-model="searchQuery"
                                >

                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="4" md="3">
                                <v-menu
                                        ref="menu"
                                        v-model="menu"
                                        :close-on-content-click="false"
                                        :return-value.sync="date"
                                        transition="scale-transition"
                                        offset-y
                                        max-width="290px"
                                        min-width="290px"
                                >
                                    <template v-slot:activator="{ on }">
                                        <v-text-field
                                                v-model="date"
                                                label="Appointment date"
                                                prepend-icon="mdi-calendar"
                                                :rules="[rules.required('Date')]"
                                                readonly
                                                v-on="on"
                                        ></v-text-field>
                                    </template>
                                    <v-date-picker
                                            v-model="date"
                                            type="date"
                                            no-title
                                            scrollable
                                            :min="today"
                                    >
                                        <v-spacer></v-spacer>
                                        <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                                        <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
                                    </v-date-picker>
                                </v-menu>
                            </v-col>
                            <v-col cols="12" sm="4" md="3">
                                <intervention-type-selection
                                        v-model="type"
                                        :clinic-id="$route.params.clinicID"
                                ></intervention-type-selection>
                            </v-col>

                            <v-col cols="6" sm="2" lg="1" class="text-center">
                                <v-btn color="primary" type="submit">
                                    Search
                                </v-btn>
                            </v-col>
                            <v-col cols="6" sm="2" lg="1">
                                <v-btn text @click="reset">
                                    Reset
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-form>

                </v-col>

            </v-row>
        <v-card outlined v-if="type">
            <div class="subtitle-1">The price for {{type.name}} is ${{type.price}}.</div>
            <div class="subtitle-1">This appointment will start on {{date}}</div>
        </v-card>
    </v-container>
</template>

<script>
    import InterventionTypeSelection from "../InterventionType/InterventionTypeSelection";
    import JSOG from 'jsog';
    import {mapState} from "vuex";
    import {Patient} from "../../utils/DrawerItems";
    export default {
        name: "DoctorSearch",
        components: {InterventionTypeSelection},
        data: () => ({
            menu: false,
            date: null,
            type: null,
            searchQuery: '',
            patientCode: Patient.code,
            rules: {
                required: fieldName => v => !!v || `${fieldName} is required`,
            },
        }),
        props: {
            value: {
                type: Boolean,
                value: false
            },
            selectedType: null,
            selectedDate: null
        },
        watch: {
            selectedType(val) {
                this.type = val;
            },
            selectedDate(val) {
                this.date = val;
            }
        },
        methods: {
            search() {
                if(!this.$refs.form.validate()) return;
                this.$emit('searched', {
                    date: this.date,
                    interventionTypeID: this.type.id,
                    searchQuery: this.searchQuery.trim().toLowerCase()});
                this.$emit('update:selectedType', this.type);
            },
            reset() {
                this.$emit('reset');
                this.$refs.form.reset();
            }
        },
        computed: {
            ...mapState('auth', ['role']),
            today() {
                let tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
                return (new Date(Date.now() - tzoffset)).toISOString().slice(0, -1);
            }
        }
    }
</script>

<style scoped>

</style>
