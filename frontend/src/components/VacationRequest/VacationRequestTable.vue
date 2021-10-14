<template>
	<div>
		<v-card>
			<v-card-title>
				Vacation requests
				<v-spacer></v-spacer>
				<v-text-field
					v-model="search"
					append-icon="mdi-magnify"
					label="Search"
					single-line
					hide-details
				></v-text-field>
			</v-card-title>
		</v-card>
		
		<v-data-table
			:headers="headers"
			:items="vacationRequest"
			:search="search"   
			class="elevation-1"
			:loading="loading"
			loading-text="Loading vacation requests..."
		>
			<template  v-if="role === adminCode" v-slot:item.approve="{ item }">
				<v-icon  v-if="item.status === 'pending'" @click="approveVacation(item)" color="amber darken-2">
					mdi-email-check
				</v-icon>
				<v-icon  v-else  color="amber darken-2">
					mdi-email-send-outline
				</v-icon>
			</template>
			
			<template  v-if="role === adminCode" v-slot:item.disapprove="{ item }">
				<v-icon v-if="item.status === 'pending'" @click="disapproveVacationDialog(item)" color="amber darken-2">
					mdi-email-check
				</v-icon>
				<v-icon  v-else  color="amber darken-2">
					mdi-email-send-outline
				</v-icon>
			</template>
			<template v-if="role === doctorCode" v-slot:item.remove="{ item }">
				<v-icon @click="deleteDialog(item)" color="red">
					mdi-delete
				</v-icon>
			</template>

			<template v-slot:no-data>
				<p>There are no vacation requests</p>
			</template>
		</v-data-table>
		<delete-dialog
			v-model="dialog"
			:vacationRequest="vacationRequestToDelete"
			@close="deleteDialog(null)"
			@delete="deleteVacationRequest"
		/>
		<disapprove-dialog
			v-model="disapproveDialog"
			:vacationRequest="vacationRequestToDisapprove"
			@close="disapproveVacationDialog(null)"
			@delete="disapproveVacation"
		/>
	</div>
</template>

<script>
import {mapActions, mapState} from "vuex";
import DeleteDialog from "./DeleteDialog";
import DisapproveDialog from "./DisapproveDialog";
import {ClinicalAdmin, Doctor, Nurse} from '../../utils/DrawerItems';

export default {
    name: "VacationRequestTable",
    components: {DeleteDialog, DisapproveDialog},
    data: () => ({
    	search : "",
        loading: false,
        descriptionDialog: false,
        dialog: false,
        disapproveDialog : false,
        vacationRequestToDelete: null,
        vacationRequestToDisapprove: null,
        adminCode: ClinicalAdmin.code,
        doctorCode : Doctor.code,
    }),
    computed: {
        ...mapState('auth', ['user']),
        ...mapState('auth', ['clinic']),
        ...mapState('clinics/vacationRequest', ['vacationRequest']),
        ...mapState('auth', ['role']),
        headers() {
        	let regularHeaders = [
            	{text: 'Start date', align: 'start', value: 'startDate'},
            	{text: 'End date', align: 'center', value: 'endDate'},
            	{text: 'Status', align: 'center', value: 'status'},
    	        {text: 'Remove', value: 'remove', align: 'center', sortable: false},
            ];
            let adminHeaders = [
                {text: 'Staff name', align: 'start', value: 'user.name'},
                {text: 'Staff email', align: 'center', value: 'user.email'},                
                {text: 'Start date', align: 'center', value: 'startDate'},
            	{text: 'End date', align: 'center', value: 'endDate'},
            	{text: 'Status', align: 'center', value: 'status'},
	            {text: 'Approve', align: 'center', value: 'approve', sortable: false },
    	        {text: 'Disapprove', align: 'center', value: 'disapprove', sortable: false},
            ];   
            if (this.role === this.adminCode) {
                    return adminHeaders;
            }
            return regularHeaders;
        },
    },
    methods: {
        ...mapActions('clinics/vacationRequest', ['getUserVacationRequestApi']),   
        ...mapActions('clinics/vacationRequest', ['getClinicVacationRequestApi']),        
        ...mapActions('clinics/vacationRequest', ['deleteVacationRequestApi']),
        ...mapActions('clinics/vacationRequest', ['approveVacationRequestApi']),
        ...mapActions('clinics/vacationRequest', ['disapproveVacationRequestApi']),
        ...mapActions('clinics/vacationRequest', ['updateVacationRequestApi']),
        
        deleteDialog(vacationRequestToDelete) {
            this.vacationRequestToDelete = vacationRequestToDelete;
            this.dialog = !this.dialog;
        },
        deleteVacationRequest() {
            this.deleteVacationRequestApi(this.vacationRequestToDelete);
            this.deleteDialog(null);
        },
        disapproveVacationDialog(vacationRequestToDisapprove) {
            this.vacationRequestToDisapprove = vacationRequestToDisapprove;
            this.disapproveDialog = !this.disapproveDialog;
        },
        disapproveVacation(reason){
        	console.log("disapproveVacation");
        	console.log("reason = " + reason);
        	console.log(this.vacationRequestToDisapprove.id);
        	var vacationRequestObj = {
        		'id' : this.vacationRequestToDisapprove.id,
        		'reason' : reason
        	}
        	this.disapproveVacationRequestApi(vacationRequestObj);
            this.disapproveVacationDialog(null);
        },
        approveVacation(item){
        	console.log("approveVacation");
        	console.log(item.id);
        	this.approveVacationRequestApi(item.id);
        },
    },
    created() {
        this.loading = true;
        switch (this.user.type) {
			case Nurse.code:
            case Doctor.code:
                console.log("user = Doctor user.id = " + this.user.id);
				this.getUserVacationRequestApi(this.user.id);
                break;
            case ClinicalAdmin.code:
                console.log("user = ClinicalAdmin id = " + this.clinic.id);
                this.getClinicVacationRequestApi(this.clinic.id);
                break;
            default:
        }
        
    },
    watch: {
        vacationRequest() {
            this.loading = false;
        }
    }
}
</script>

<style scoped></style>