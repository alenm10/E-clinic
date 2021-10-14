<template>
    <v-container fluid>  	
    	<v-row>
		    <v-col cols="12" sm="6">
		      <v-date-picker v-model="dates" range></v-date-picker>
		    </v-col>		   
		</v-row>
		<v-container>
	    	Total interventions: {{report.totalInterventions}}
	    </v-container>
	    	
	    <v-container>
	    	Past interventions: {{report.totalPastInterventions}}
	    </v-container>
	    
	    <v-container>
	    	Upcoming interventions: {{report.totalFutureInterventions}}
	    </v-container>
	    
	    <v-container>
	    	Total revenue: {{report.totalRevenue}} $
	    </v-container>  
	    
	     <div class="my-2">
	     	<v-btn 
	     		@click="getReport"
	     		depressed
	     		color="primary">Select</v-btn>
	     </div>
      
    </v-container>
</template>

<script>
	import {mapActions, mapState} from "vuex";
    import {ClinicalAdmin, Doctor, Nurse} from "../utils/DrawerItems";
    import store from "../store";

    export default {
        name: "Report",
        components: {},
        data: () => ({
      		dates: ['2020-01-01', '2020-12-31'],
   	 	}),
        computed: {
	        ...mapState('intervention/intervention', ['report']),
	        ...mapState('auth', ['clinic']),
	        ...mapState('auth', ['role']),
	        
	        dateFormat () {
		        var d1 = Date.parse(this.dates[0]);
				var d2 = Date.parse(this.dates[1]);
				console.log("d1 = ");
				console.log(d1);
				console.log("d2 = ");
				console.log(d2);
				if (d1 < d2) {
					return this.dates;
				}else{
					return this.dates.reverse();
				}
		    },
        },
        methods: {
        	...mapActions('intervention/intervention', ['getClinicReportApi']),
        	getReport() {
				console.log("getreport");
				console.log(this.dateFormat);
				if(this.dateFormat.length == 1){
					var obj = {
		        		"clinicId" : this.clinic.id,
		        		"startDate" : this.dateFormat[0],
		        		"endDate" : "none"
		        	}
		        	this.getClinicReportApi(obj);
				}else{
					var obj = {
		        		"clinicId" : this.clinic.id,
		        		"startDate" : this.dateFormat[0],
		        		"endDate" : this.dateFormat[1],
		        	}
		        	this.getClinicReportApi(obj);
				}
	        },
        },
        created() {
        	console.log("report created");
        	var obj = {
        		"clinicId" : this.clinic.id,
        		"startDate" : "all",
        		"endDate" : "all"
        	}
        	this.getClinicReportApi(obj);
        },
        beforeRouteEnter(to, from, next) {
        	console.log("role = " + store.state.auth.role);
            if(store.state.auth.role === ClinicalAdmin.code){
           		next();
            }else{
            	next('/');
            }
        }
    }
</script>

<style scoped>

</style>

