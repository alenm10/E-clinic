<template>
    <v-container fluid>
        <appointment-requests-table></appointment-requests-table>
    </v-container>
</template>

<script>
    import DoctorTable from "../components/Doctor/DoctorTable";
    import AddDoctor from "../components/Doctor/AddDoctor";
    import AppointmentRequestsTable from "../components/AppointmentRequests/AppointmentRequestsTable";
    import store from "../store";
    import {ClinicalAdmin, Patient} from "../utils/DrawerItems";
    import axios from "axios";
    export default {
        name: "Doctor",
        components: {AppointmentRequestsTable, AddDoctor, DoctorTable},
        beforeRouteEnter: async (to, from, next) => {
            if(store.state.auth.role === Patient.code) {
                console.log("ODJESAM");
                try {
                    let {data: res} = await axios.get(`/api/patient/user/${store.state.auth.user.id}`,
                        {headers: {"Authorization": 'Bearer ' + store.state.auth.token}});
                    if (res.id === to.params.patientID) {
                        next();
                        return;
                    }
                } catch (err) {
                    console.log(err);
                }

            } else if (store.state.auth.role === ClinicalAdmin.code) {
                if (to.params.clinicID === store.state.auth.clinic.id) {
                    next();
                    return;
                }
            }
            next('/');

        }
    }
</script>

<style scoped>

</style>
