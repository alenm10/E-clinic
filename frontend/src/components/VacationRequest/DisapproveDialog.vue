<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            @keydown.enter="$emit('close')"
            max-width="290"
    >
        <v-card>
            <v-card-title class="headline">Disapprove</v-card-title>
			
            <v-card-text>
                Are you sure that you want to disapprove <span class="text--primary">{{ deleteName }}</span>?
            </v-card-text>

			<v-col cols="12">
	            <v-textarea
	                    counter="256"
	                    outlined
	                    label="Reson:"
	                    rows="10"
	                    no-resize
	                    v-model="reason"
	                    :rules="reasonRules"
            	></v-textarea>
        	</v-col>
            <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                        ref="btCloseDelete"
                        color="green darken-1"
                        text
                        @click="$emit('close')"
                >
                    No
                </v-btn>

                <v-btn
                        color="green darken-1"
                        text
                        @click="$emit('delete', reason)"
                >
                    Yes
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    export default {
        name: "DisapproveDialog",
        props: ['vacationRequest', 'value'],
        data: () => ({
        	reason: '',
            reasonRules: [v => !!v || "* Required.", v => v?.length <= 255 || 'Max 256 characters.'],
        }),
        computed: {
            deleteName() {
                return this.vacationRequest ? this.vacationRequest.id : '';
            }
        }

    }
</script>

<style scoped>

</style>
