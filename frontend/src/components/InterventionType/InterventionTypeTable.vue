<template>
    <div>
    	<v-card>
			<v-card-title>
				Intervention types
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
                :items="interventionType"
                :search="search"
                class="elevation-1"
                :loading="loading"
                loading-text="Getting intervention types"
        >
			<template v-slot:item.update="{ item }">
                <v-icon
                        @click="updateDialog(item)"
                        color="amber darken-2"
                >
                    mdi-pencil
                </v-icon>
            </template>
            <template v-slot:item.remove="{ item }">
                <v-icon
                        @click="deleteDialog(item)"
                        color="red"
                >
                    mdi-delete
                </v-icon>
            </template>

            <template v-slot:no-data>
                <p>There are no intervention types</p>
            </template>
        </v-data-table>
        </v-card>
        <delete-dialog
                v-model="dialog"
                :interventionType="interventionTypeToDelete"
                @close="deleteDialog(null)"
                @delete="deleteInterventionType"
        />
        <modify-intervention-type-dialog
                mode="update"
                :edit-intervention-type="editInterventionType"
                v-model="editDialog"/>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDialog from "./DeleteDialog";
    import ModifyInterventionTypeDialog from "./ModifyInterventionTypeDialog";
    import {ClinicalAdmin, ClinicalCenterAdmin, Patient} from "../../utils/DrawerItems";

    export default {
        name: "InterventionTypeTable",
        components: {DeleteDialog, ModifyInterventionTypeDialog},
        data: () => ({
        	search : "",
            loading: false,
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            interventionTypeToDelete: null,
            editInterventionType: null,

        }),
        computed: {
            ...mapState('interventionType/interventionType', ['interventionType']),
            ...mapState('auth', ['role']),
            ...mapState('auth', ['clinic']),
            headers() {
                let regularHeaders = [
                    { text: 'Name', align: 'start', value: 'name' },
                    { text: 'Price', align: 'center', value: 'price' }
                ];
                let adminHeaders = [
                    { text: 'Update', value: 'update', sortable: false, align: 'center' },
                    { text: 'Remove', sortable: false, value: 'remove' }
                ];
                if (this.role !== Patient.code) regularHeaders = regularHeaders.concat(adminHeaders);
                return regularHeaders;
            }
        },
        methods: {
            ...mapActions('interventionType/interventionType', ['getAllInterventionTypeApi']),
            ...mapActions('interventionType/interventionType', ['getClinicInterventionTypeApi']),
            ...mapActions('interventionType/interventionType', ['deleteInterventionTypeApi']),

            deleteDialog(interventionTypeToDelete) {
                this.interventionTypeToDelete = interventionTypeToDelete;
                this.dialog = !this.dialog;
            },
            deleteInterventionType() {
                this.deleteInterventionTypeApi(this.interventionTypeToDelete);
                this.deleteDialog(null);
            },
            updateDialog(interventionType) {
            	console.log("updateDialog id = " + interventionType.id);
                this.editInterventionType = {
                	id : interventionType.id,
					name : interventionType.name,
					price : interventionType.price,
                };
                this.editDialog = true;
            }
        },
        created() {
        	this.loading = true;
        	switch (this.role) {
                case ClinicalCenterAdmin.code:
                case Patient.code:
                	this.getAllInterventionTypeApi();
                    break;
                case ClinicalAdmin.code:
                   	this.getClinicInterventionTypeApi(this.clinic.id);
                    break;
                default:
            }
        },
        watch: {
            interventionType() {
                this.loading = false;
            }
        }
    }
</script>

<style scoped>

</style>
