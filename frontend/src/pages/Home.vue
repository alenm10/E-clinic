<template>
	<v-container>
		<doctor-home-component v-if="role === doctorCode"></doctor-home-component>
		<patient-home v-else-if="role === patientCode"></patient-home>
		<home-clinic-center-admin v-else-if="role === clinicCenterAdminCode"></home-clinic-center-admin>
		<nurse-home v-else-if="role === nurseCode"></nurse-home>
		<v-dialog v-model="dialog" persistent max-width="600px">
			<v-card>
				<v-card-title>
					<span class="headline">You must update your password</span>
				</v-card-title>
				<v-card-text>
					<v-container>
						<v-row>
							<v-col cols="12">
								<v-text-field
									v-model="currentPassword"
									label="Current password*"
									:append-icon="showCurrent ? 'mdi-eye' : 'mdi-eye-off'"
									:type="showCurrent ? 'text' : 'password'"
									:error="oldPasswordError.isError"
									:error-messages="oldPasswordError.errorMessage"
									@click:append="showCurrent = !showCurrent"
									:rules="[rules.required]"
									outlined
									required
								>
								</v-text-field>
							</v-col>
							<v-col cols="12">
								<v-text-field
									v-model="password"
									label="Password*"
									:append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
									:type="showPassword ? 'text' : 'password'"
									@click:append="showPassword = !showPassword"
									:rules="[rules.required]"
									outlined
									required
								>
								</v-text-field>
							</v-col>
							<v-col cols="12">
								<v-text-field
									v-model="repeatedPassword"
									label="Repeat Password*"
									:type="showRepeated ? 'text' : 'password'"
									:append-icon="showRepeated ? 'mdi-eye' : 'mdi-eye-off'"
									@click:append="showRepeated = !showRepeated"
									:rules="[rules.required, repeatedRule]"
									outlined
									required
								>
								</v-text-field>
							</v-col>
						</v-row>
					</v-container>
					<small>*indicates required field</small>
				</v-card-text>
				<v-card-actions>
					<v-spacer></v-spacer>
					<v-btn color="blue darken-1" text @click="save()">Save</v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</v-container>
</template>

<script>
import { mapState } from "vuex";
import DoctorHomeComponent from "../components/DoctorHome/DoctorHomeComponent";
import {ClinicalAdmin, Doctor, Patient, Nurse, ClinicalCenterAdminClass} from "../utils/DrawerItems";
import ClinicDetailsComponent from "../components/ClinicDetails/ClinicDetailsComponent";
import store from '../store/index'
import NurseHome from "../components/Nurse/NurseHome";
import PatientHome from "../components/PatientHome/PatientHome";
import HomeClinicCenterAdmin from "../components/ClinicCenterAdministrator/HomeClinicCenterAdmin";
export default {
	name: "Home",
	components: {PatientHome, ClinicDetailsComponent, DoctorHomeComponent, NurseHome, HomeClinicCenterAdmin},
	data: () => ({
		dialog: false,
		password: "",
		repeatedPassword: "",
		currentPassword: "",
		showRepeated: false,
		showPassword: false,
		showCurrent: false,
		oldPasswordError: {
			isError: false,
			errorMessage: ""
		},
		rules: {
			required: v => !!v || "Required!",
			min8: v => (!!v && v.length >= 8) || "At least 8 characters"
		},
		doctorCode: Doctor.code,
		patientCode: Patient.code,
		nurseCode: Nurse.code,
		clinicAdminCode: ClinicalAdmin.code,
		clinicCenterAdminCode: ClinicalCenterAdminClass.code
	}),
	computed: {
		...mapState("auth", ["user"]),
		...mapState('auth', ["role"]),
		repeatedRule() {
			return () =>
				this.password === this.repeatedPassword || `Doesn't match password`;
		}
	},
	props: {
		personal: {
			type: Boolean,
			default: false
		}
	},
	methods: {
		save() {
			this.oldPasswordError.isError = false;
			this.oldPasswordError.errorMessage = "";
			this.changePassword();
		},
		async changePassword() {
			try {
				let { data: res } = await this.$axios.put(
					`/api/auth/changepassword/${this.user.id}`,
					{
						oldPassword: this.currentPassword,
						newPassword: this.password,
						personal: this.personal
					}
				);
				console.log("commit");
				console.log(res);
				this.$store.commit("auth/updateResponse", res);
				this.dialog = false;
			} catch (err) {
				if (err.response.status === 400) {
					this.oldPasswordError.isError = true;
					this.oldPasswordError.errorMessage = "The old password isn't correct";
				}
			}
		}
	},
	mounted() {
		console.log(this.user);
		this.dialog = this.user.lastPasswordResetDate == null;
	},
	beforeRouteEnter(to, from, next) {
		if(store.state.auth.role === ClinicalAdmin.code)
			next(`/clinic/${store.state.auth.clinic.id}`);
		else next();
	}
};
</script>

<style scoped></style>
