<template>
    <v-container fluid>
        <add-nurse  v-if="role === adminCode"/>
        <nurse-table/>
    </v-container>
</template>

<script>
    import NurseTable from "../components/Nurse/NurseTable";
    import AddNurse from "../components/Nurse/AddNurse";
    import {ClinicalAdmin, ClinicalCenterAdmin} from "../utils/DrawerItems";
    import {mapState} from "vuex";
    
    export default {
        name: "Nurse",
        components: {AddNurse, NurseTable},
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
