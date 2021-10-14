<template>
    <div>
        <v-dialog
                :value="value"
                @input="$emit('input', false)"
                fullscreen
                hide-overlay
                transition="dialog-bottom-transition"
        >
            <v-card>
                <v-toolbar dark class="toolbar" color="accent">
                    <v-btn icon dark @click="closeAddDialog">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                    <v-toolbar-title v-if="mode === 'add'">Add a clinic</v-toolbar-title>
                    <v-toolbar-title v-else-if="clinic">Update {{ clinic.name }}</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-toolbar-items>
                        <v-btn v-if="mode === 'add'" dark text @click="submit(saveClinic)">Add</v-btn>
                        <v-btn v-else dark text @click="submit(modifyClinicApi)">Update</v-btn>
                    </v-toolbar-items>
                </v-toolbar>
                <v-container>
                    <v-row>
                        <v-col cols="12" md="6">
                            <v-form ref="form">
                                <add-clinic-form
                                        @mapAddress="mapAddress = $event"
                                        @mapDialog="mapDialog = !mapDialog"
                                />
                            </v-form>
                        </v-col>
                        <v-col cols="6">
                            <map-view
                                    ref="largeMap"
                                    v-model="coordinates"
                                    map-id="map"
                                    class="d-none d-md-flex"
                                    clickable
                                    use-address
                                    :address="mapAddress"
                                    @clickAddress="setAddress"
                            />
                        </v-col>
                    </v-row>
                    <small>*indicates required field</small>
                </v-container>
            </v-card>
        </v-dialog>
        <v-dialog v-model="mapDialog" fullscreen hide-overlay transition="dialog-bottom-transition">
            <v-card height="100%">
                <v-toolbar class="toolbar" dark elevation="2" color="primary">
                    <v-toolbar-title>Select a location</v-toolbar-title>
                    <v-spacer></v-spacer>
                    <v-btn icon dark @click="closeMapDialog">
                        <v-icon>mdi-close</v-icon>
                    </v-btn>
                </v-toolbar>
                <v-card-text class="card-body">
                    <map-view
                            v-model="coordinates"
                            class="d-flex d-md-none"
                            clickable
                            use-address
                            :address="mapAddress"
                            map-id="smallMap"
                            @clickAddress="setAddress"
                    />
                </v-card-text>
                <v-fade-transition>
                    <v-card v-show="!!clinic.address && clinic.address.length > 0"
                            tile
                            outlined
                            class="footer"
                    >
                        <v-card-text>
                            <p class="font-weight-medium">{{ clinic.address }}</p>
                        </v-card-text>
                    </v-card>
                </v-fade-transition>
            </v-card>
        </v-dialog>
    </div>
</template>

<script>
    import {mapActions, mapMutations} from "vuex";
    import {emptyClinic} from "../../utils/skeletons";
    import AddClinicForm from "./AddClinicForm";
    import MapView from "./MapView";

    export default {
        name: "ModifyClinicDialog",
        components: {AddClinicForm, MapView},
        data: () => ({
            mapDialog: false,
            mapAddress: null,
        }),
        props: {
            value: null,
            mode: {
                type: String,
                default: 'add'
            }
        },
        watch: {
            value(val) {
                if (!val) this.resetLayout();
            }
        },
        computed: {
            clinic: {
                get() {
                    return this.$store.state.clinics.addClinic.clinic;
                },
                set(val) {
                    this.updateClinic(val);
                }
            },
            coordinates: {
                get() {
                    return this.clinic.coordinates;
                },
                set(coords) {
                    this.updateClinic({coordinates: coords});
                }
            },
            address: {
                get() {
                    return this.clinic.address;
                },
                set(address) {
                    this.updateClinic({address: address});
                }
            }
        },
        methods: {
            ...mapMutations('clinics/addClinic', ['updateClinic']),
            ...mapActions('clinics/addClinic', ['saveClinic','modifyClinicApi']),

            setAddress(event) {
                if (!event) this.address = null;
                else {
                    this.address = event[0];
                }

            },
            closeAddDialog() {
                this.$emit('input', false);
            },
            submit(fun) {
                let valid = this.$refs.form.validate();
                if (valid) {
                    fun(this.clinic);
                    this.$emit('modified', this.clinic);
                    this.closeAddDialog();
                }
            },
            closeMapDialog() {
                this.mapDialog = false;
                // this.$nextTick(() => {})
            },
            resetLayout() {
                this.clinic = emptyClinic;
                this.$refs.form.reset();
                this.$refs.largeMap.resetMap();
            }
        }
    }
</script>

<style scoped>
    .card-body {
        margin: auto 0 auto 0;
        height: 91%;
        width: 100%;
        padding: 0 !important;
    }

    .footer {
        position: absolute;
        bottom: 0;
        height: fit-content;
        width: 100%;
        z-index: 1000;
        background-color: red;
    }
</style>
