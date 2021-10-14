<template>
    <working-calendar/>
</template>

<script>
    import WorkingCalendar from "../components/WorkingSchedule/WorkingCalendar";
    import {Doctor, Nurse} from "../utils/DrawerItems";
    import {mapState} from "vuex";
    import store from "../store";
    export default {
        name: "WorkingSchedule",
        components: {WorkingCalendar},
        computed: {
            ...mapState('auth', ['role']),
        },
        beforeRouteEnter(to, from, next) {
            switch(store.state.auth.role) {
                case Doctor.code:
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