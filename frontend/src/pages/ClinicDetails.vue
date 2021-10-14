<template>
    <v-container fluid>
        <clinic-details-component/>
    </v-container>
</template>

<script>
    import ClinicDetailsComponent from "../components/ClinicDetails/ClinicDetailsComponent";
    export default {
        name: "ClinicDetails",
        components: {ClinicDetailsComponent}
    }
</script>

<style scoped>

</style>
