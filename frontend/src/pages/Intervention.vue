<template>
    <v-container fluid>
    	<intervention-table/>
    </v-container>
</template>

<script>
    import InterventionTable from "../components/Intervention/InterventionTable";

    export default {
        name: "Intervention",
        components: {InterventionTable}
    }
</script>

<style scoped>

</style>

