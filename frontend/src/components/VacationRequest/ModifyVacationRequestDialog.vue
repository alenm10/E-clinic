<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            persistent max-width="700px">
        <v-card>
            <v-card-title>
                <span v-if="mode === 'add'" class="headline">Add vacation request</span>
                <span v-else class="headline">Update vacation request</span>
            </v-card-title>
            <v-card-text>
                <v-container>
                    <v-form ref="form">
                        <v-row>
                           <v-col cols="6">
							   <v-date-picker v-model="startDate" color="green lighten-1"></v-date-picker>
                           </v-col>
                           <v-col cols="6">
							   <v-date-picker v-model="endDate" color="green lighten-1"></v-date-picker>
                           </v-col>
                        </v-row>
                    </v-form>
                </v-container>
                <small>*indicates required field</small>
            </v-card-text>
            <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="blue darken-1" text @click="close">Close</v-btn>
                <v-btn color="blue darken-1" v-if="mode === `add`" text @click="submit(addVacationRequestApi)">Add</v-btn>
                <v-btn color="blue darken-1" v-else text @click="submit(updateVacationRequestApi)">Update</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>
	import {mapActions, mapState} from "vuex";
	import {emptyVacationRequest} from "../../utils/skeletons";
	
	export default {
        name: "ModifyVacationRequestDialog",
        components: {},
        data: () => ({
            vacationRequest: emptyVacationRequest,
			startDate : '',
        	endDate : '',           
        }),
        props: {
            editVacationRequest: null,
            value: false,
            mode: {
                type: String,
                default: 'add'
            }
        },
        computed: {
            ...mapState('auth', ['user']),
            ...mapState('auth', ['clinic']),
        },
        watch: {
            value() {
                if (this.editVacationRequest) {
                    this.vacationRequest = this.editVacationRequest;
                }
                
                this.startDate = this.vacationRequest.startDate;
                this.endDate = this.vacationRequest.endDate;
                
            }
        },
        methods: {
        	...mapActions('clinics/vacationRequest', ['addVacationRequestApi']),
            ...mapActions('clinics/vacationRequest', ['updateVacationRequestApi']),

            submit(fun) {

                if (this.$refs.form.validate()) {
                	console.log("add pressed");

                    var start = new Date(this.startDate);
	        		var end = new Date(this.endDate)
	        		if(dates.compare(start,end) == 0){
	        			console.log("dates are equal");
	        			alert("dates are equal");
	        		}else if(dates.compare(start,end) == 1){
	        			console.log("start > end");
	        			alert("start > end");
	        		}else if(dates.compare(start,end) == -1){
	        			console.log("start < end");
	        			this.vacationRequest.user = this.user;
	        			this.vacationRequest.clinic = this.clinic;
	        			this.vacationRequest.startDate = start;
	        			this.vacationRequest.endDate = end;
	        			this.vacationRequest.status = "pending";
	        			console.log("created vacation request = ");
	        			console.log(this.vacationRequest);
	        			fun(this.vacationRequest);
                    	this.close();
	        		}
                }
            },
            close() {
                this.$emit('input', false);
                if (this.mode === 'add') this.$refs.form.reset();
            }
        }
    }
    var dates = {
	    convert:function(d) {
	        return (
	            d.constructor === Date ? d :
	            d.constructor === Array ? new Date(d[0],d[1],d[2]) :
	            d.constructor === Number ? new Date(d) :
	            d.constructor === String ? new Date(d) :
	            typeof d === "object" ? new Date(d.year,d.month,d.date) :
	            NaN
	        );
	    },
	    compare:function(a,b) {
	        return (isFinite(a=this.convert(a).valueOf()) &&isFinite(b=this.convert(b).valueOf()) ? (a>b)-(a<b) : NaN
	        );
	    }
	}
</script>

<style scoped>

</style>
