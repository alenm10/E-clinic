<template>
    <v-container fluid>
        <patients-table></patients-table>
    </v-container>
</template>

<script>
    import PatientsTable from "../components/Patient/PatientsTable";

    export default {
        name: "Patients",
        components: {PatientsTable},
    }
</script>

<style scoped>

</style>
