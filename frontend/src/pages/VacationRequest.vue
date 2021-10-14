<template>
	<v-container fluid>
    	<vacation-request-table/>
    	<template  v-if="role === doctorCode || role== nurseCode">
    		<add-vacation-request/>
    	</template>
    </v-container>
</template>

<script>
    import {mapState} from "vuex";
	import VacationRequestTable from "../components/VacationRequest/VacationRequestTable";
    import AddVacationRequest from "../components/VacationRequest/AddVacationRequest";
    import store from "../store";
    import {ClinicalAdmin, Doctor, Nurse} from "../utils/DrawerItems";
    
    export default {
        name: "VacationRequest",
        components: {VacationRequestTable, AddVacationRequest},
        data: () => ({
	        doctorCode : Doctor.code,
			nurseCode : Nurse.code
	    }),
        computed: {
        	...mapState('auth', ['role']),
        },
        beforeRouteEnter(to, from, next) {
        	console.log("role = " + store.state.auth.role);
            if(store.state.auth.role === ClinicalAdmin.code 
            	|| store.state.auth.role === Doctor.code
            	|| store.state.auth.role === Nurse.code)
            {
           		next();
            }else{
            	next('/');
            }
        }
    }
    
</script>

<style scoped>

</style>
