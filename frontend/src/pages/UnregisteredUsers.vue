<template>
    <v-container fluid>
        <unregistered-users-table></unregistered-users-table>
    </v-container>
</template>

<script>

    import AddClinicAdministrator from "../components/ClinicAdministrator/AddClinicAdministrator";
    import TableClinicAdministrator from "../components/ClinicAdministrator/TableClinicAdministrator";
    import UnregisteredUsersTable from "../components/UnregisteredUsers/UnregisteredUsersTable";

    export default {
        name: "UnregisteredUsers",
        components: {UnregisteredUsersTable},
    }
</script>

<style scoped>

</style>
