<template>
    <v-container fluid>
        <add-medicine/>
        <medicine-table/>
    </v-container>
</template>

<script>
    import AddMedicine from "../components/Medicine/AddMenicine";
    import MedicineTable from "../components/Medicine/MedicineTable";
    import store from "../store";
    import {ClinicalCenterAdmin} from "../utils/DrawerItems";
    export default {
        name: "Medicine",
        components: {MedicineTable, AddMedicine},
        beforeRouteEnter(to, from, next) {
            if(store.state.auth.role === ClinicalCenterAdmin.code) next();
            else next('/');
        }
    }
</script>

<style scoped>

</style>