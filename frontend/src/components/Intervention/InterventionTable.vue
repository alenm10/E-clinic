<template>
	<div>
		<v-card>
			<v-card-title>
				Interventions
				<v-spacer></v-spacer>
				<v-text-field
					v-model="search"
					append-icon="mdi-magnify"
					label="Search"
					single-line
					hide-details
				></v-text-field>
			</v-card-title>

		<v-data-table
			:headers="headers"
			:items="intervention"
			:search="search"
			class="elevation-1"
			:loading="loading"
			loading-text="Loading interventions..."
		>
			<template v-slot:item.update="{ item }">
				<v-icon @click="updateDialog(item)" color="amber darken-2">
					mdi-pencil
				</v-icon>
			</template>
			<template v-slot:item.remove="{ item }">
				<v-icon @click="deleteDialog(item)" color="red">
					mdi-delete
				</v-icon>
			</template>
			<template v-slot:item.rate="{ item }">
				<v-icon @click="rateDialog(item)" color="red">
					mdi-pencil
				</v-icon>
			</template>

			<template v-slot:no-data>
				<p>There are no interventions</p>
			</template>
		</v-data-table>
		</v-card>

		<delete-dialog
			v-model="dialog"
			:intervention="interventionToDelete"
			@close="deleteDialog(null)"
			@delete="deleteIntervention"
		/>
		<rate-dialog
			v-model="rateDialogShow"
			:intervention="interventionToRate"
			@closeRate="rateDialog(null)"
			@rate="rateIntervention"
		/>
		<modify-intervention-dialog
			mode="update"
			:edit-intervention="editIntervention"
			v-model="editDialog"
		/>
	</div>
</template>

<script>
import {mapActions, mapState} from "vuex";
import DeleteDialog from "./DeleteDialog";
import ModifyInterventionDialog from "./ModifyInterventionDialog";
import RateDialog from "./RateDialog";
import {ClinicalAdmin, ClinicalCenterAdmin, Patient} from "../../utils/DrawerItems";

export default {
    name: "InterventionTable",
    components: {DeleteDialog, ModifyInterventionDialog, RateDialog},
    data: () => ({
    	search : "",
        loading: false,
        descriptionDialog: false,
        editDialog: false,
        dialog: false,
        rateDialogShow: false,
        interventionToDelete: null,
        interventionToRate: null,
        editIntervention: null,
    }),
    computed: {
        ...mapState('intervention/intervention', ['intervention']),
        ...mapState('auth', ['role']),
        ...mapState('auth', ['user']),
        ...mapState('auth', ['clinic']),
    	headers() {
                let regularHeaders = [
                    {text: 'Date and time', align: 'start', value: 'dateTime.start'},
		            {text: 'Doctor name', align: 'center', value: 'doctorName'},
		            {text: 'Clinic room', align: 'center', value: 'clinicRoomName'},
		            {text: 'Intervention type', align: 'center', value: 'interventionTypeName'},
					{text: 'Clinic', align: 'center', value: 'clinicName'},
					{text: 'Intervention price (in $)', align: 'center', value: 'interventionTypePrice'},
		            {text: 'Update', value: 'update', sortable: false, align: 'center'},
		            {text: 'Remove', sortable: false, value: 'remove'},
                ];
                let patientHeaders = [
					{text: 'Date and time', align: 'start', value: 'dateTime.start'},
					{text: 'Doctor name', align: 'center', value: 'doctorName'},
					{text: 'Clinic room', align: 'center', value: 'clinicRoomName'},
					{text: 'Intervention type', align: 'center', value: 'interventionTypeName'},
					{text: 'Clinic', align: 'center', value: 'clinicName'},
					{text: 'Intervention price (in $)', align: 'center', value: 'interventionTypePrice'},
		            {text: 'Price (in $)', align: 'center', value: 'price'},
		            {text: 'Rate', value: 'rate', sortable: false, align: 'center'},
		        ];
                if (this.role !== Patient.code){
                	return regularHeaders;
                }else{
                	return patientHeaders;
                }
        },
    },
    methods: {
        ...mapActions('intervention/intervention', ['getClinicInterventionApi']),
        ...mapActions('intervention/intervention', ['getAllInterventionApi']),
        ...mapActions('intervention/intervention', ['deleteInterventionApi']),
        ...mapActions('intervention/intervention', ['getPatientInterventionApi']),
        ...mapActions('intervention/intervention', ['rateClinicInterventionApi']),

        deleteDialog(interventionToDelete) {
            this.interventionToDelete = interventionToDelete;
            this.dialog = !this.dialog;
        },
        deleteIntervention() {
            this.deleteInterventionApi(this.interventionToDelete);
            this.deleteDialog(null);
        },
        updateDialog(intervention) {
            console.log("updateDialog id = " + intervention.id);
            console.log(intervention);
            this.editIntervention = {
                id : intervention.id,
                dateTime : intervention.dateTime,
                selectedClinicRoom : intervention.clinicRoom,
                selectedDoctor : intervention.doctor,
                selectedInterventionType : intervention.interventionType,
                duration : intervention.duration,
                price : intervention.price,
            };
            this.editDialog = true;
        },
        rateDialog(intervention) {
			this.interventionToRate = intervention;
            this.rateDialogShow = !this.rateDialogShow;
        },
        rateIntervention(rating) {
        	let payload = {
        		userId: this.user.id,
        		clinicId : this.interventionToRate.clinicID,
        		clinicRating : rating.ratingClinic,
        		doctorId : this.interventionToRate.doctorID,
        		doctorRating : rating.ratingDoctor,
        	};
			this.rateClinicInterventionApi(payload);
        	this.rateDialog(null);
        },
    },
    created() {
        this.loading = true;
        switch (this.user.type) {
            case ClinicalCenterAdmin.code:
                console.log("user = ClinicalCenterAdmin");
                break;
            case ClinicalAdmin.code:
                console.log("user = ClinicalAdmin id = " + this.clinic.id);
                console.log("callapi");
                this.getClinicInterventionApi(this.clinic.id);
                break;
            case Patient.code:
				console.log("user == patient");
				this.getPatientInterventionApi(this.user.id);
            	break;
            default:
        }
    },
    watch: {
        intervention() {
            this.loading = false;
        }
    }
}
</script>

<style scoped></style>
