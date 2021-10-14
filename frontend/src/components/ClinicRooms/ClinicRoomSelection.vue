<template>
    <v-autocomplete
            :value="value"
            @input="$emit('input', $event)"
            hint="Choose clinic room"
            :items="clinicRooms"
            label="Clinic room*"
            :disabled="disabled"
            persistent-hint
            item-text="name"
            item-value="name"
            :loading="loading"
            :rules="rules"
            return-object
            prepend-icon="mdi-hospital-building"
    >
        <template v-slot:no-data>
            <v-list-item>
                <v-list-item-title>
                    There are no clinic rooms
                </v-list-item-title>
            </v-list-item>
        </template>

    </v-autocomplete>
</template>

<script>

    import {mapActions, mapState} from "vuex";
    import {ClinicalAdmin, ClinicalCenterAdmin} from "../../utils/DrawerItems";

    export default {
        name: "ClinicRoomSelection",
        data: () => ({
            rules: [v => !!v || 'Clinic room is required'],
            loading: false
        }),
        props: {
            value: null,
            disabled: {
                type: Boolean,
                default: false,
            },
            clinicId: {
                type: String,
                default: '',
            },
            duration: {
                type: Number,
                default: 30
            },
            dateTime: null,
        },
        computed: {
            ...mapState('clinicRooms/clinicRooms', ['clinicRooms']),
            ...mapState('auth', ['user']),
        },
        watch: {
            clinicRooms() {
                this.loading = false;
            },
            dateTime(val) {
                if(val) {
                    this.loading = true;
                    let offsetDate = new Date(val);
                    offsetDate.setMinutes(val.getMinutes() - val.getTimezoneOffset());
                    this.searchApi(
                        {
                            clinicID: this.clinicId,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            request: {
                                dateTime: offsetDate,
                                roomName: '',
                                roomID: '',
                                duration: this.duration
                            }
                        }
                    );
                }
            },
            duration(val) {
                if(val) {
                    this.loading = true;
                    let offsetDate = new Date(this.dateTime);
                    offsetDate.setMinutes(this.dateTime.getMinutes() - this.dateTime.getTimezoneOffset());
                    this.searchApi(
                        {
                            clinicID: this.clinicId,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            request: {
                                dateTime: offsetDate,
                                roomName: '',
                                roomID: '',
                                duration: val
                            }
                        }
                    );
                }
            }
        },
        methods: {
            ...mapActions('clinicRooms/clinicRooms', ['getClinicRooms', 'searchApi']),
            populate() {
                this.loading = true;
                if (!this.dateTime) {
                    this.getClinicRooms({
                        clinicID: this.clinicId,
                        pageNumber: 1,
                        pageSize: -1,
                        sort: [],
                        desc: []
                    });
                } else {
                    let offsetDate = new Date(this.dateTime);
                    offsetDate.setMinutes(this.dateTime.getMinutes() - this.dateTime.getTimezoneOffset());
                    this.searchApi(
                        {
                            clinicID: this.$route.params.clinicId,
                            pageNumber: 1,
                            pageSize: -1,
                            sort: [],
                            desc: [],
                            request: {
                                dateTime: offsetDate,
                                roomName: '',
                                roomID: '',
                                duration: this.duration
                            }
                        }
                    );
                }
            }
        },
        mounted() {
            this.populate();
        }
    }
</script>

<style scoped>

</style>
