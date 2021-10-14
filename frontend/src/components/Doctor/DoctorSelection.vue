<template>
    <v-autocomplete
            :value="value"
            @input="$emit('input', $event)"
            hint="Choose clinic room"
            :items="doctors"
            label="Doctor*"
            persistent-hint
            item-text="name"
            :rules="rules"
            :disabled="disabled"
            :loading="loading"
            return-object
            prepend-icon="mdi-hospital-building"
    >
        <template v-slot:no-data>
            <v-list-item>
                <v-list-item-title>
                    There are no doctors
                </v-list-item-title>
            </v-list-item>
        </template>

    </v-autocomplete>
</template>

<script>

    import {mapActions, mapState} from "vuex";
    import {ClinicalAdmin, ClinicalCenterAdmin} from "../../utils/DrawerItems";

    export default {
        name: "DoctorSelection",
        data: () => ({
            rules: [v => !!v || 'Doctor required'],
            loading: false
        }),
        props: {
            value: null,
            disabled: {
                type: Boolean,
                default: false,
            },
            clinicId: {
                type: String
            },
            duration: {
                type: Number,
                default: 30
            },
            dateTime: null,
            typeId: null
        },
        computed: {
        	...mapState('doctor/doctor', ['doctors']),
        	...mapState('auth', ['user']),
        },
        watch: {
            doctors() {
                this.loading = false;
            },
            dateTime(val) {
                this.loading = true;
                if (val && this.duration && this.typeId) {
                    let offsetDate = new Date(val);
                    offsetDate.setMinutes(val.getMinutes() - val.getTimezoneOffset());
                    this.getDoctorsForTimeType(
                        {
                            clinicID: this.clinicId,
                            typeID: this.typeId,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            duration: this.duration,
                            time: offsetDate
                        });
                } else {

                    this.getClinicDoctors({
                        clinicID: this.clinicId,
                        pageNumber: 1,
                        pageSize: -1,
                        sort: [],
                        desc: []
                    });
                }
            },
            typeId(val) {
                this.loading = true;
                if (this.dateTime && this.duration && val) {
                    let offsetDate = new Date(this.dateTime);
                    offsetDate.setMinutes(this.dateTime.getMinutes() - this.dateTime.getTimezoneOffset());
                    this.getDoctorsForTimeType(
                        {
                            clinicID: this.clinicId,
                            typeID: val,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            duration: this.duration,
                            time: offsetDate
                        });
                } else {
                    this.getClinicDoctors({
                        clinicID: this.clinicId,
                        pageNumber: 1,
                        pageSize: -1,
                        sort: [],
                        desc: []
                    });
                }
            },
            duration(val) {
                this.loading = true;
                if (this.dateTime && val && this.typeId) {
                    let offsetDate = new Date(this.dateTime);
                    offsetDate.setMinutes(this.dateTime.getMinutes() - this.dateTime.getTimezoneOffset());
                    this.getDoctorsForTimeType(
                        {
                            clinicID: this.clinicId,
                            typeID: this.typeId,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            duration: val,
                            time: offsetDate
                        });
                } else {
                    this.getClinicDoctors({
                        clinicID: this.clinicId,
                        pageNumber: 1,
                        pageSize: -1,
                        sort: [],
                        desc: []
                    });
                }
            }


        },
        methods: {
            ...mapActions('doctor/doctor', ['getClinicDoctors', 'getDoctorsForTimeType']),
            populate() {
                if (this.dateTime && this.typeId) {
                    let offsetDate = new Date(this.dateTime);
                    offsetDate.setMinutes(this.dateTime.getMinutes() - this.dateTime.getTimezoneOffset());
                    this.getDoctorsForTimeType(
                        {
                            clinicID: this.clinicId,
                            typeID: this.typeId,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            duration: this.duration,
                            time: offsetDate
                        });
                } else {
                    this.getClinicDoctors({
                        clinicID: this.clinicId,
                        pageNumber: 1,
                        pageSize: -1,
                        sort: [],
                        desc: []
                    });
                }
            }
        },
        mounted() {
            this.loading = true;
            this.populate();
        }
    }
</script>

<style scoped>

</style>
