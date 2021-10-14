<template>
    <div @scroll="scroll">
        <modify-clinic-dialog
            v-model="addDialog"/>
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
    import ModifyClinicDialog from "./ModifyClinicDialog";
    import {mapState} from "vuex";
    import {ClinicalCenterAdmin} from "../../utils/DrawerItems";

    export default {
        name: "AddClinic",
        components: {ModifyClinicDialog},
        data: () => ({
            addDialog: false,
            scrolledDown: false
        }),
        computed: {
            ...mapState('clinics/readClinics', ['clinics']),
        },
        watch: {
            clinics() {
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
