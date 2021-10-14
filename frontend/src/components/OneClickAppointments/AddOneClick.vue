<template>
    <div>
        <modify-one-click-dialog v-model="addDialog"></modify-one-click-dialog>
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

    import {mapState} from "vuex";
    import ModifyOneClickDialog from "./ModifyOneClickDialog";

    export default {
        name: "AddOneClick",
        components: {ModifyOneClickDialog},
        data: () => ({
            addDialog: false,
            scrolledDown: false
        }),
        computed: {
            ...mapState('oneClicks', ['oneClicks'])
        },
        watch: {
            oneClicks() {
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
