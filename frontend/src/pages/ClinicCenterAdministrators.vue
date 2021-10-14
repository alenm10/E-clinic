<template>
    <v-container fluid>
        <table-clinic-center-administrator/>
        <add-clinic-center-administrator/>
    </v-container>
</template>

<script>
    import AddClinicCenterAdministrator from "../components/ClinicCenterAdministrator/AddClinicCenterAdministrator";
    import TableClinicCenterAdministrator from "../components/ClinicCenterAdministrator/TableClinicCenterAdministrator";
    import store from "../store";
    import {ClinicalCenterAdmin} from "../utils/DrawerItems";

    export default {
        name: "ClinicCenterAdministrators",
        components: {TableClinicCenterAdministrator, AddClinicCenterAdministrator},
        beforeRouteEnter(to, from, next) {
            if(store.state.auth.role === ClinicalCenterAdmin.code) next();
            else next('/');
        }
    }
</script>

<style scoped>

</style>
