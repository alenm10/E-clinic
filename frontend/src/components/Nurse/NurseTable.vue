<template>
    <div>
    	<v-card>
			<v-card-title>
				Nurse
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
            :items="nurse"
       		:search="search"
            class="elevation-1"
            :loading="loading"
            loading-text="Contacting all the nurses to see if they still work here..."
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
                <p>There are no nurses</p>
            </template>
        </v-data-table>
        <delete-dialog
                v-model="dialog"
                :nurse="nurseToDelete"
                @close="deleteDialog(null)"
                @delete="deleteNurse"
        />
        <modify-nurse-dialog
                mode="update"
                :edit-nurse="editNurse"
                v-model="editDialog"/>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDialog from "./DeleteDialog";
    import ModifyNurseDialog from "./ModifyNurseDialog";
    import {ClinicalAdmin, ClinicalCenterAdmin} from "../../utils/DrawerItems";

    export default {
        name: "NurseTable",
        components: {DeleteDialog, ModifyNurseDialog},
        data: () => ({
        	search : "",
            loading: false,
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            nurseToDelete: null,
            editNurse: null,
            headers: [
                {text: 'Name', align: 'start', value: 'name'},
                {text: 'Surname', align: 'center', value: 'surname'},
                {text: 'Email', align: 'center', value: 'email'},
                {text: 'Phone number', align: 'center', value: 'phoneNumber'},
                {text: 'Address', align: 'center', value: 'address'},
                {text: 'Update', value: 'update', sortable: false, align: 'center'},
                {text: 'Remove', sortable: false, value: 'remove'},
            ],
        }),
        computed: {
            ...mapState('nurse/nurse', ['nurse']),
            ...mapState('auth', ['user']),
            ...mapState('auth', ['clinic']),
        },
        methods: {
            ...mapActions('nurse/nurse', ['getNurse']),
            ...mapActions('nurse/nurse', ['getClinicNurse']),
            ...mapActions('nurse/nurse', ['deleteNurseApi']),

            deleteDialog(nurseToDelete) {
                this.nurseToDelete = nurseToDelete;
                this.dialog = !this.dialog;
            },
            deleteNurse() {
                this.deleteNurseApi(this.nurseToDelete);
                this.deleteDialog(null);
            },
            updateDialog(nurse) {
                this.$router.push(`/profile/${nurse.user.id}`)
                // this.editNurse = {
                //     id: nurse.id,
                //     email: nurse.user.email,
                //     name: nurse.user.name,
                //     surname: nurse.user.surname,
                //     phone: nurse.user.phoneNumber,
                //     address: nurse.user.address,
                //     city: nurse.user.city,
                //     country: nurse.user.country,
                //     jmbg: nurse.user.personalID,
                //     position: nurse.position
                // };
                // this.editDialog = true;
            }
        },
        created() {
            this.loading = true;
            console.log("created");
            switch (this.user.type) {
                case ClinicalCenterAdmin.code:
                    console.log("user = ClinicalCenterAdmin")
                    this.getNurse();	//sve sestre
                    break;
                case ClinicalAdmin.code:
                    console.log("user = ClinicalAdmin id = " + this.clinic.id);
                    this.getClinicNurse(this.clinic.id);  //sestre samo za clinic.id
                    break;
                default:
            }
        },
        watch: {
            nurse() {
                this.loading = false;
            }
        }
    }
</script>

<style scoped>

</style>
