<template>
    <v-navigation-drawer
            dark
            class="primary elevation-5"
            :mini-variant="mini"
            permanent
            app
    >
        <v-list>
            <v-tooltip :disabled="!mini" right>
                <template v-slot:activator="{ on }">
                    <v-list-item link v-on="on" @click.stop="$emit('miniVariant')">
                        <v-list-item-icon>
                            <v-icon>mdi-menu</v-icon>
                        </v-list-item-icon>
                        <v-list-item-content>
                            <v-list-item-title>Collapse</v-list-item-title>
                        </v-list-item-content>
                    </v-list-item>
                </template>
                <span>Expand</span>
            </v-tooltip>
            <v-list-item-group>
                <v-tooltip
                        v-for="(item, i) in items"
                        :key="i"
                        right
                        :disabled="!mini"
                >
                    <template v-slot:activator="{ on }">
                        <v-list-item :to="item.path" link v-on="on">
                            <v-list-item-icon>
                                <v-icon>{{ item.icon }}</v-icon>
                            </v-list-item-icon>

                            <v-list-item-content>
                                <v-list-item-title>{{ item.label }}</v-list-item-title>
                            </v-list-item-content>
                        </v-list-item>
                    </template>
                    <span>{{ item.label }}</span>
                </v-tooltip>
            </v-list-item-group>

        </v-list>
    </v-navigation-drawer>
</template>

<script>
    import {ClinicalAdmin, ClinicalCenterAdmin, Patient, Doctor, Nurse} from "../utils/DrawerItems";
    import {mapState} from "vuex";

    export default {
        name: "Drawer",
        data: () => ({
            items: Array,
            patient: null
        }),
        props: {
            mini: {
                type: Boolean,
                default: true
            },
        },
        computed: {
            ...mapState('auth', ['role', 'user', 'clinic']),

        },
        async mounted() {
            if (this.role === Patient.code) await this.getPatient();
            switch (this.role) {
                case ClinicalCenterAdmin.code:
                    this.items = ClinicalCenterAdmin.items;
                    break;
                case ClinicalAdmin.code:
                    this.items = (new ClinicalAdmin(this.clinic.id)).items;
                    break;
                case Patient.code:
                    this.items = (new Patient(this.patient.id, this.user.id)).items;
                    break;
                case Doctor.code:
                    this.items = (new Doctor()).items;
                    break;
                case Nurse.code:
                    this.items = (new Nurse()).items;
                    break;
                default:

            }
        },
        methods: {
            async getPatient() {
                try {
                    let {data: res} = await this.$axios.get(`/api/patient/user/${this.user.id}`,
                        {headers: {"Authorization": 'Bearer ' + this.$store.state.auth.token}});
                    this.patient = res;
                } catch (err) {
                    console.log(err);
                }
            }
        }
    }
</script>

<style scoped>
</style>
