<template>
    <v-container fluid>
        <table-disease></table-disease>
        <add-disease></add-disease>
    </v-container>
</template>

<script>
    import TableDisease from "../components/Disease/TableDisease";
    import AddDisease from "../components/Disease/AddDisease";
    import store from "../store";
    import {ClinicalCenterAdmin} from "../utils/DrawerItems";
    export default {
        name: "Disease",
        components: {AddDisease, TableDisease},
        beforeRouteEnter(to, from, next) {
            if(store.state.auth.role === ClinicalCenterAdmin.code) next();
            else next('/');
        }
    }
</script>

<style scoped>

</style>
