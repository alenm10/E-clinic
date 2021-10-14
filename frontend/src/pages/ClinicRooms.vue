<template>
    <v-container fluid>
        <add-clinic-room
                v-if="role === adminCode && !$route.params.requestID"
        />
        <clinic-rooms-table/>
    </v-container>
</template>

<script>
    import ClinicRoomsTable from "../components/ClinicRooms/ClinicRoomsTable";
    import AddClinicRoom from "../components/ClinicRooms/AddClinicRoom";
    import {mapState} from "vuex";
    import {ClinicalAdmin} from "../utils/DrawerItems";
    export default {
        name: "ClinicRooms",
        components: {AddClinicRoom, ClinicRoomsTable},
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
