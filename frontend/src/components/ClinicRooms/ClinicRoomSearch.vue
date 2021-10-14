<template>
    <v-container fluid class="ma-2">
            <v-row align="center">
                <v-col cols="12" md="1">
                    <span class="headline">Clinic rooms</span>
                </v-col>
                <v-col cols="12" md="11" v-if="value">
                    <v-form ref="form" @submit.prevent="search">

                        <v-row align="center" justify="space-around">
                            <v-col cols="12" sm="6" md="3">
                                <v-text-field
                                        prepend-icon="mdi-magnify"
                                        label="Name"
                                        v-model="roomName"
                                >

                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="3">
                                <v-text-field
                                        prepend-icon="mdi-magnify"
                                        label="ID"
                                        v-model="roomID"
                                >

                                </v-text-field>
                            </v-col>
                            <v-col cols="12" sm="6" md="2">
                                <v-datetime-picker
                                        v-model="dateTime"
                                        :date-picker-props="{min: tomorrow}"
                                        :time-picker-props="{format: '24h'}"
                                >
                                    <template v-slot:dateIcon>
                                        <v-icon>
                                            mdi-calendar
                                        </v-icon>
                                    </template>
                                    <template v-slot:timeIcon>
                                        <v-icon>
                                            mdi-clock
                                        </v-icon>
                                    </template>
                                </v-datetime-picker>
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
        name: "ClinicRoomSearch",
        components: {InterventionTypeSelection},
        data: () => ({
            menu: false,
            dateTime: null,
            roomName: '',
            roomID: '',
            patientCode: Patient.code,
            rules: {
                required: fieldName => v => !!v || `${fieldName} is required`,
            },
            selectedDate: null,
        }),
        props: {
            value: {
                type: Boolean,
                value: true
            },
            selectedDateTime: null
        },
        watch: {
            selectedDateTime(val) {
                this.dateTime = val;
            }
        },
        methods: {
            setDate(e) {
                this.selectedDate = e;
            },
            search() {
                if(!this.$refs.form.validate()) return;
                this.$emit('searched', {
                        dateTime: this.dateTime,
                        roomName: this.roomName.trim().toLowerCase(),
                        roomID: this.roomID.trim().toLowerCase(),
                    }
                );
            },
            reset() {
                this.$emit('reset');
                this.$refs.form.reset();
            },
        },
        computed: {
            ...mapState('auth', ['role']),
            tomorrow() {
                let now = new Date();
                now.setDate(now.getDate() + 1);
                return now.toISOString().slice(0, -1);
            },
        }
    }
</script>

<style scoped>

</style>
