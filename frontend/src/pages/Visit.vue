<template>
    <v-container>
		<v-card>
			<v-card-title>
				Ongoing intervention for {{patient.name}} {{patient.surname}}
				<v-icon @click="$router.push(`/profile/${patient.userID}`)">mdi-account-outline</v-icon>
			</v-card-title>
			<v-card-text>
				<v-container>
					<v-row>
						<v-col cols="12">
							<v-textarea v-model="anamnesis" outlined>
								<template v-slot:label>
									<div>
										Anamnesis
									</div>
								</template>
							</v-textarea>
						</v-col>
					</v-row>
					<v-row>
						<v-col cols="12">
							<disease-selection
									v-model="diagnoses"
									multiple/>
						</v-col>
					</v-row>
					<v-row>
						<v-col cols="12">
							<medicine-selection
									v-model="medicines"
									multiple/>

						</v-col>
					</v-row>
					<v-row>
						<v-col cols="12">
							<v-textarea v-model="usage" outlined>
								<template v-slot:label>
									<div>
										Usage
									</div>
								</template>
							</v-textarea>
						</v-col>
					</v-row>
				</v-container>
			</v-card-text>

			<v-card-actions>
				<v-spacer></v-spacer>
				<v-btn x-large color="primary" @click="finish">
					Finish
				</v-btn>
				<v-spacer>

				</v-spacer>

			</v-card-actions>
		</v-card>
    </v-container>
</template>

<script>

	import DiseaseSelection from "../components/Disease/DiseaseSelection";
	import MedicineSelection from "../components/Medicine/MedicineSelection";
	import {mapState} from "vuex";
	import {emptyUser} from "../utils/skeletons";

    export default {
        name: "Visit",
        components: { DiseaseSelection, MedicineSelection },
        data: () => ({
			anamnesis: '',
			usage: '',
			intervention: null,
			patient: emptyUser,
        	diagnoses: [],
        	medicines: [],
        }),
		computed: {
        	...mapState('auth', ['token']),
		},
		methods: {
        	async finish() {
        		let visit = {
					anamnesis: this.anamnesis,
					usageMethod: this.usage,
					diagnoses: this.diagnoses,
					medicines: this.medicines
				};
        		try {
					await this.$axios.post(`/api/intervention/finish/${this.intervention.id}`,
							visit,
							{headers: {"Authorization": 'Bearer ' + this.token}});
					this.$router.push('/patients');
				} catch (e) {
        			console.log("An error occurred while saving this visit");
				}
			},
        	checkUnsaved() {
        		alert("cek");
				if(this.anamnesis || this.diagnoses || this.medicines) {
					alert("UNSAVED");
				}
			}
		},
		ready() {
			window.addEventListener('beforeunload', this.checkUnsaved);
		},
		async mounted() {
			let {data: intervention} = await this.$axios.get(`/api/intervention/${this.$route.params.id}`,
					{headers: {"Authorization": 'Bearer ' + this.token}});
			this.intervention = intervention;
			let {data: patient} = await this.$axios.get(`/api/patient/${this.intervention.patientID}`,
					{headers: {"Authorization": 'Bearer ' + this.token}});
			this.patient = patient;
		}

	}
</script>

<style scoped>

</style>

