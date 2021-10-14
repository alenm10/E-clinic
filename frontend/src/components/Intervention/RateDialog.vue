<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            @keydown.enter="$emit('close')"
            max-width="290"
    >
        <v-card>
            <v-card-title class="headline">Rate clinic or doctor</v-card-title>
            <v-card-text>
                Rate clinic {{ clinicName }}:
            </v-card-text>
			<v-rating
		      v-model="ratingClinic"
		      background-color="green lighten-3"
		      color="green"
		      large
		    ></v-rating>

		    <v-card-text>
		       Rate doctor {{ doctorName }}:
            </v-card-text>
			<v-rating
		      v-model="ratingDoctor"
		      background-color="green lighten-3"
		      color="green"
		      large
		    ></v-rating>
            <v-card-actions>
                <v-spacer></v-spacer>

                <v-btn
                        ref="btCloseDelete"
                        color="green darken-1"
                        text
                        @click="$emit('closeRate')"
                >
                    No
                </v-btn>

                <v-btn
                        color="green darken-1"
                        text
                        @click="$emit('rate', {ratingClinic, ratingDoctor})"
                >
                    Yes
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script>

    export default {
        name: "RateDialog",
        props: ['intervention', 'value'],
        data: () => ({
	      ratingClinic: 0,
	      ratingDoctor: 0,
	    }),
        watch: {
            async value(val) {
                if(!val) return;
                let {data: rating} = await this.$axios.get(`/api/intervention/rating/${this.$store.state.auth.user.id}/${this.intervention.clinicID}/${this.intervention.doctorID}`,
                    {headers: {"Authorization": 'Bearer ' + this.$store.state.auth.token} });
                this.ratingClinic = rating.clinicRating;
                this.ratingDoctor = rating.doctorRating;
            }
        },
        computed: {
        	clinicName() {
                return this.intervention ? this.intervention.clinicName : '';
            },
            doctorName() {
                return this.intervention ? this.intervention.doctorName : '';
            },
        },
        async mounted() {

        }

    }
</script>

<style scoped>

</style>
