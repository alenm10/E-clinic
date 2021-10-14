<template>
    <medical-prescription-table></medical-prescription-table>
</template>

<script>
    import MedicalPrescriptionTable from "../components/MedicalPrescriptionTable";
    import {Nurse, Patient} from "../utils/DrawerItems";
    import {mapState} from "vuex";
    import store from "../store";
    export default {
        name: "MedicalPrescription",
        components: {MedicalPrescriptionTable},
        computed: {
            ...mapState('auth', ['role']),
        },
        beforeRouteEnter(to, from, next) {
            switch(store.state.auth.role) {
                case Nurse.code:
                    next();
                    break;
                default:
                    next('/');
            }
        }
    }
</script>

<style scoped>

</style>