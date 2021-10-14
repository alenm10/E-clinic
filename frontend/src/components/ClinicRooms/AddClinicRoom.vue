<template>
    <div>
        <modify-clinic-room-dialog v-model="addDialog"/>
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
    import ModifyClinicRoomDialog from "./ModifyClinicRoomDialog";
    import {mapState} from "vuex";

    export default {
        name: "AddClinicRoom",
        components: {ModifyClinicRoomDialog},
        data: () => ({
            addDialog: false,
            scrolledDown: false

        }),
        computed: {
            ...mapState('clinicRooms/clinicRooms', ['clinicRooms'])
        },
        watch: {
            clinicRooms() {
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
