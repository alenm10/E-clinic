<template>
    <v-container fluid>
        <add-intervention-type/>
        <intervention-type-table/>
    </v-container>
</template>

<script>
    import InterventionTypeTable from "../components/InterventionType/InterventionTypeTable";
    import AddInterventionType from "../components/InterventionType/AddInterventionType";
    export default {
        name: "InterventionType",
        components: {InterventionTypeTable, AddInterventionType}
    }
</script>

<style scoped>

</style>
