<template>
    <div>
        <modify-doctor-dialog v-model="addDialog"/>
        <v-btn
                fab
                dark
                large
                color="red"
                :class="{'fab-br': true, down: scrolledDown}"
                @click.stop="addDialog = true"
        >
            <v-icon> mdi-plus</v-icon>
        </v-btn>
    </div>
</template>

<script>
    import ModifyDoctorDialog from "./ModifyDoctorDialog";
    import {mapState} from "vuex";

    export default {
        name: "AddDoctor",
        components: {ModifyDoctorDialog},
        data: () => ({
            addDialog: false,
            scrolledDown: false

        }),
        computed: {
            ...mapState('doctor/doctor', ['doctors'])
        },
        watch: {
            doctors() {
                this.checkSize();
            }
        },
        methods: {
            scroll () {
                window.onscroll = () => {
                    this.checkSize();
                }
            },
            checkSize() {
                this.scrolledDown = Math.ceil(
                    Math.max(window.pageYOffset,
                        document.documentElement.scrollTop,
                        document.body.scrollTop) + window.innerHeight)
                    >= document.documentElement.offsetHeight;
            }
        },
        mounted() {
            this.scroll();
        }
    }
</script>

<style scoped>

</style>
