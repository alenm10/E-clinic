<template>
    <v-container fluid>
        <clinics-table></clinics-table>
        <add-clinic
                v-if="role === adminCode"
        ></add-clinic>
    </v-container>
</template>

<script>
    import AddClinic from "../components/Clinics/AddClinic";
    import ClinicsTable from "../components/Clinics/ClinicsTable";
    import {mapState} from "vuex";
    import {ClinicalCenterAdmin, Patient} from "../utils/DrawerItems";
    import store from '../store/index'
    export default {
        name: "Clinics",
        components: {ClinicsTable, AddClinic},
        data: () => ({
            adminCode: ClinicalCenterAdmin.code,
        }),
        computed: {
            ...mapState('auth', ['role']),
        },
        beforeRouteEnter(to, from, next) {
            switch(store.state.auth.role) {
                case ClinicalCenterAdmin.code:
                case Patient.code:
                    next();
                    break;
                default:
                    next('/');
            }
        }
    }
</script>

<style scoped>

</style>
