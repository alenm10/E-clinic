<template>
    <div>
    	<v-card>
			<v-card-title>
				Medicines
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
                :items="medicines"
                :search="search"     
                class="elevation-1"
                :loading="loading"
                loading-text="Loading all the medicines..."
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
                <p>There are no existing medicines</p>
            </template>
        </v-data-table>
        <delete-medicine
                v-model="dialog"
                :medicine="medicineToDelete"
                @close="deleteDialog(null)"
                @delete="deleteMedicine"
        />
        <modify-medicine
                mode="update"
                :edit-medicine="editMedicine"
                v-model="editDialog"/>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteMedicine from "./DeleteMedicine";
    import ModifyMedicine from "./ModifyMedicine";

    export default {
        name: "MedicineTable",
        components: {ModifyMedicine, DeleteMedicine},
        data: () => ({
        	search : "",
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            medicineToDelete: null,
            editMedicine: null,
            headers: [
                {text: 'ID', align: 'start', value: 'id'},
                {text: 'Name', value: 'name', align: 'center'},
                {text: 'Manufacturer', value: 'manufacturer'},
                {text: 'Update', value: 'update', sortable: false},
                {text: 'Remove', value: 'remove', sortable: false},

            ],
        }),
        computed: {
            ...mapState('medicines/medicines', ['medicines']),
            // editClinic: {
            //     get() {
            //         return this.$store.state.clinics.addClinic.clinic;
            //     },
            //     set(val) {
            //         this.$store.commit('clinics/addClinic/updateClinic', val);
            //     }
            // }
        },
        methods: {
            ...mapActions('medicines/medicines', ['getMedicines']),
            ...mapActions('medicines/medicines', ['deleteMedicineApi']),

            deleteDialog(medicine) {
                this.medicineToDelete = medicine;
                this.dialog = !this.dialog;
            },
            deleteMedicine() {
                this.deleteMedicineApi(this.medicineToDelete);
                this.deleteDialog(null);
            },
            updateDialog(medicine) {
                this.editMedicine = medicine;
                this.editDialog = true;
            }

        },
        created() {
            this.loading = true;
            this.getMedicines();
        },
        watch: {
            medicines() {
                this.loading = false;
            }
        }
    }
</script>

<style scoped>

</style>
