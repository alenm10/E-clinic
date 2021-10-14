<template>
	<div>

		<v-card>
			<v-data-table
					:headers="headers"
					:items="oneClicks"
					class="elevation-1"
					:loading="loading"
					:options.sync="options"
					:server-items-length="length"
					loading-text="Don't click anywhere! ha-ha"
			>

				<template v-slot:item.doctorName="{ item }">
					{{item.doctorLastName}} {{item.doctorFirstName}}
				</template>
				<template v-slot:item.update="{ item }">
					<v-icon @click="toggleEditDialog(item)" color="amber darken-2">
						mdi-pencil
					</v-icon>
				</template>

				<template v-slot:item.schedule="{ item }">
					<v-icon @click="toggleScheduleDialog(item)" color="amber darken-2">
						mdi-calendar
					</v-icon>
				</template>

				<template v-slot:item.remove="{ item }">
					<v-icon @click="toggleDeleteDialog(item)" color="red">
						mdi-delete
					</v-icon>
				</template>

				<template v-slot:no-data>
					<p>There are no predefined appointments</p>
				</template>
			</v-data-table>
		</v-card>
		<delete-dialog
			v-model="deleteDialog"
			:intervention="oneClickToDelete"
			@close="toggleDeleteDialog(null)"
			@delete="deleteOneClick"
		/>
		<modify-one-click-dialog
				mode="update"
				:edit-one-click="oneClickToEdit"
				v-model="editDialog"
				@close="toggleEditDialog(null)"
		></modify-one-click-dialog>
		<schedule-dialog
				v-model="scheduleDialog"
				@close="toggleScheduleDialog(null)"
				@schedule="scheduleOneClick"/>
	</div>
</template>

<script>
import {mapActions, mapState} from "vuex";
import DeleteDialog from "./DeleteDialog";
// import ModifyInterventionDialog from "./ModifyInterventionDialog";
import {ClinicalAdmin, ClinicalCenterAdmin, Patient} from "../../utils/DrawerItems";
import ModifyOneClickDialog from "./ModifyOneClickDialog";
import ScheduleDialog from "./ScheduleDialog";

export default {
    name: "OneClicksTable",
    components: {ScheduleDialog, ModifyOneClickDialog, DeleteDialog},
    data: () => ({
        loading: false,
        editDialog: false,
        deleteDialog: false,
		scheduleDialog: false,
        oneClickToDelete: null,
        oneClickToEdit: null,
		oneClickToSchedule: null,
		options: {
			page: 1,
			itemsPerPage: 10
		},
		adminCode: ClinicalAdmin.code,
		patientCode: Patient.code,
    }),
    computed: {
		...mapState('oneClicks', ['length']),
		...mapState('auth', ['role']),
		oneClicks() {
			if (this.options.itemsPerPage > 0)
				return this.$store.state.oneClicks.oneClicks.slice(0, this.options.itemsPerPage);
			else
				return this.$store.state.oneClicks.oneClicks;
		},
		headers() {
        	let regularHeaders = [
				{text: 'Type', align: 'start', value: 'typeName'},
				{text: 'Starts at', align: 'start', value: 'dateTime.start'},
				{text: 'Ends at', align: 'start', value: 'dateTime.end'},
				{text: 'Doctor', align: 'center', value: 'doctorName'},
				{text: 'Clinic room', align: 'center', value: 'clinicRoomName'},
				{text: 'Price', align: 'end', value: 'price'},

			];
        	let patientHeaders = [
				{text: 'Schedule', align: 'start', sortable: false, value: 'schedule'},
			];
        	let adminHeaders = [
				{text: 'Remove', sortable: false, value: 'remove'}
			];
			if (this.role === this.adminCode) {
				regularHeaders = regularHeaders.concat(adminHeaders);
			} else if (this.role === this.patientCode)
				regularHeaders = regularHeaders.concat(patientHeaders);
			return regularHeaders;
		}
    },
    methods: {
		...mapActions('oneClicks',
				[
					'getOneClicks',
					'deleteOneClickApi',
					'addOneClickApi',
				]),
		...mapActions('intervention/intervention', ['scheduleOneClickIntervention']),


		toggleDeleteDialog(oneClick) {
            this.oneClickToDelete = oneClick;
            this.deleteDialog = !this.deleteDialog;
        },
        deleteOneClick() {
            this.deleteOneClickApi(this.oneClickToDelete);
            this.toggleDeleteDialog(null);
        },
		toggleScheduleDialog(oneClick) {
			this.oneClickToSchedule = oneClick;
			this.scheduleDialog = !this.scheduleDialog;
		},
		scheduleOneClick() {
			this.scheduleOneClickIntervention(this.oneClickToSchedule);
			this.toggleScheduleDialog(null);
			this.populate();
		},
        toggleEditDialog(oneClick) {
			this.oneClickToEdit = oneClick;
            this.editDialog = true;
        },
		populate() {
			this.getOneClicks({
				clinicID: this.$route.params.clinicID,
				pageNumber: this.options.page,
				pageSize: this.options.itemsPerPage,
				sort: this.options.sortBy,
				desc: this.options.sortDesc
			});
		}
    },
    mounted() {
        this.loading = true;
		this.populate();
    },
    watch: {
		oneClicks(val) {
			this.loading = false;
			if(this.options.itemsPerPage <= this.length && val.length < this.options.itemsPerPage) {
				this.loading = true;
				this.populate();
			}
		},
		options(val) {
			this.loading = true;
			this.getOneClicks({
				clinicID: this.$route.params.clinicID,
				pageNumber: val.page,
				pageSize: val.itemsPerPage,
				sort: val.sortBy,
				desc: val.sortDesc
			});
		},
    }
}
</script>

<style scoped></style>
