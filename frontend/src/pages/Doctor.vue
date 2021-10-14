<template>
    <v-container fluid>
        <add-doctor
                v-if="role === adminCode"
        />
        <doctor-table/>
    </v-container>
</template>

<script>
    import DoctorTable from "../components/Doctor/DoctorTable";
    import AddDoctor from "../components/Doctor/AddDoctor";
    import {ClinicalAdmin, ClinicalCenterAdmin} from "../utils/DrawerItems";
    import {mapState} from "vuex";
    export default {
        name: "Doctor",
        components: {AddDoctor, DoctorTable},
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
