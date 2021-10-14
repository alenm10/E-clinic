<template>
    <div>
        <v-data-table
                :headers="headers"
                :items="diseases"
                class="elevation-1"
                :loading="loading"
                loading-text="Reading all the known diagnoses..."
        >
            <template v-slot:top>
                <v-toolbar flat color="white">
                    <v-toolbar-title>Diseases</v-toolbar-title>
                    <v-divider
                            class="mx-4"
                            inset
                            vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                </v-toolbar>
            </template>

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
                <p>There are no existing diagnosis</p>
            </template>
        </v-data-table>
        <delete-disease
                v-model="dialog"
                :diagnosis="diseaseToDelete"
                @close="deleteDialog(null)"
                @delete="deleteDisease"
        />
        <modify-disease
                mode="update"
                :edit-disease="editDisease"
                v-model="editDialog"/>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";
    import DeleteDisease from "./DeleteDisease";
    import ModifyDisease from "./ModifyDisease";


    export default {
        name: "TableDisease",
        components: {ModifyDisease, DeleteDisease},
        data: () => ({
            loading: false,
            descriptionDialog: false,
            editDialog: false,
            dialog: false,
            diseaseToDelete: null,
            editDisease: null,
            headers: [
                {text: 'ID', align: 'start', value: 'id'},
                {text: 'Name', value: 'name', align: 'center'},
                {text: 'Update', value: 'update', sortable: false},
                {text: 'Remove', value: 'remove', sortable: false},

            ],
        }),
        computed: {
            ...mapState('disease/disease', ['diseases']),
        },
        methods: {
            ...mapActions('disease/disease', ['getDiseases']),
            ...mapActions('disease/disease', ['deleteDiseaseApi']),

            deleteDialog(diagnosis) {
                this.diseaseToDelete = diagnosis;
                this.dialog = !this.dialog;
            },
            deleteDisease() {
                this.deleteDiseaseApi(this.diseaseToDelete);
                this.deleteDialog(null);
            },
            updateDialog(diagnosis) {
            	console.log("updateDialog = ");
            	console.log(diagnosis);
                this.editDisease = diagnosis;
                this.editDialog = true;
            }

        },
        created() {
            console.log("getDiseases");
            this.loading = true;
            this.getDiseases();
        },
        watch: {
            diseases() {
                this.loading = false;
            }
        }
    }
</script>

<style scoped>

</style>
