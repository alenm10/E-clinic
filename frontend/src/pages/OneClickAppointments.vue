<template>
    <v-container fluid>
        <one-clicks-table></one-clicks-table>
        <add-one-click
                v-if="role === adminCode"
        ></add-one-click>
    </v-container>
</template>

<script>

    import OneClicksTable from "../components/OneClickAppointments/OneClicksTable";
    import AddOneClick from "../components/OneClickAppointments/AddOneClick";
    import {ClinicalAdmin} from "../utils/DrawerItems";
    import {mapState} from "vuex";
    export default {
        name: "OneClickAppointments",
        components: {AddOneClick, OneClicksTable},
        data: () => ({
            adminCode: ClinicalAdmin.code,
        }),
        computed: {
            ...mapState('auth', ['role']),
        }
    }
</script>

<style scoped>

</style>

