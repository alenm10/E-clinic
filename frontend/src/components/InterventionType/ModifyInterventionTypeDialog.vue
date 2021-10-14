<template>
	<v-dialog
		:value="value"
		@input="$emit('input', $event)"
		persistent
		max-width="600px"
	>
		<v-card>
			<v-card-title>
				<span v-if="mode === 'add'" class="headline">Add intervention type</span>
				<span v-else class="headline">Update intervention type</span>
			</v-card-title>
			<v-card-text>
				<v-container>
					<v-form ref="form">
						<v-row>
							<v-col cols="6">
								<v-text-field
									label="Name*"
									required
									:rules="nameRules"
									v-model="name"
								>
								</v-text-field>
							</v-col>
							<v-col cols="6">
								<v-text-field
									label="Price*"
									required
									:rules="priceRules"
									v-model="price"
								></v-text-field>
							</v-col>
						</v-row>
					</v-form>
				</v-container>
				<small>*indicates required field</small>
			</v-card-text>
			<v-card-actions>
				<v-spacer></v-spacer>
				<v-btn color="blue darken-1" text @click="close">Close</v-btn>
				<v-btn
					color="blue darken-1"
					v-if="mode === `add`"
					text
					@click="submit(addInterventionTypeApi)"
					>Add</v-btn
				>
				<v-btn 
					color="blue darken-1" 
					v-else 
					text 
					@click="submit(updateInterventionTypeApi)"
					>Update</v-btn
				>
				
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script>

import {mapActions, mapState} from "vuex";
import { emptyInterventionType } from "../../utils/skeletons";
export default {
	name: "ModifyInterventionTypeDialog",
	components: {},
	data: () => ({
		id : null,
		name : null,
		price : null,
		
		interventionType: emptyInterventionType,

		showPassword: false,

		nameRules: [v => !!v || "Name is required."],
		priceRules: [v => !!v || "Price is required."],
		
	}),
	props: {
	    editInterventionType: null,
		value: false,
		mode: {
			type: String,
			default: "add"
		}
	},
	computed: {
		...mapState('auth', ['user']),
		...mapState('auth', ['clinic']),
	},
	watch: {
		value() {
        	if(this.editInterventionType) {
            	this.interventionType = this.editInterventionType;
            	console.log("value changed");
            	console.log("InterventionTypeename = " + this.interventionType.name);
            }
            this.id = this.interventionType.id;
            this.name = this.interventionType.name;
            this.price = this.interventionType.price;
            
			console.log("thisname = " + this.name);
        }
	},
	created() {;
	},
	methods: {
		...mapActions("interventionType/interventionType", ["addInterventionTypeApi"]),
		...mapActions("interventionType/interventionType", ["updateInterventionTypeApi"]),
		submit(fun) {
			console.log("addinterventionType or updateinterventionType pressed");
			console.log(this.clinic.id);

			this.interventionType.clinic = this.clinic.id;
			if (this.$refs.form.validate()) {
				this.interventionType.name = this.name;
				this.interventionType.price = this.price;
	            
				console.log(this.interventionType);
                fun(this.interventionType);
                this.close();
			}
		},
		close() {
			console.log("close");
			this.$emit('input', false);
            if (this.mode === 'add') this.$refs.form.reset();
		},
		resetLayout() {
			console.log("resetlayout");
			this.interventionType = emptyInterventionType;
			this.$refs.form.reset();
		}
	}
};
</script>

<style scoped></style>
