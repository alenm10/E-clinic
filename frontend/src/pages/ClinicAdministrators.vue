<template>
    <v-container fluid>
        <table-clinic-administrator></table-clinic-administrator>
        <add-clinic-administrator></add-clinic-administrator>
    </v-container>
</template>

<script>

    import AddClinicAdministrator from "../components/ClinicAdministrator/AddClinicAdministrator";
    import TableClinicAdministrator from "../components/ClinicAdministrator/TableClinicAdministrator";
    import store from "../store";
    import {ClinicalCenterAdmin} from "../utils/DrawerItems";

    export default {
        name: "ClinicAdministrators",
        components: {TableClinicAdministrator, AddClinicAdministrator},
        beforeRouteEnter(to, from, next) {
            if(store.state.auth.role === ClinicalCenterAdmin.code) next();
            else next('/');
        }
    }
</script>

<style scoped>

</style>
