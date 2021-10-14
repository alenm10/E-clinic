<template>
    <v-container fluid>
        <doctor-home-component/>
    </v-container>
</template>

<script>
    import DoctorHomeComponent from "../components/DoctorHome/DoctorHomeComponent";
    export default {
        name: "DoctorHome",
        components: { DoctorHomeComponent }
    }
</script>

<style scoped>

</style>
