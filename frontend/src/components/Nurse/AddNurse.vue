<template>
    <div>
        <modify-nurse-dialog v-model="addDialog"/>
        <v-btn
                fab
                dark
                large
                color="red"
                class="fab-br"
                @click.stop="addDialog = true"
        >
            <v-icon> mdi-plus</v-icon>
        </v-btn>
    </div>
</template>

<script>
    import ModifyNurseDialog from "./ModifyNurseDialog";

    export default {
        name: "AddNurse",
        components: {ModifyNurseDialog},
        data: () => ({
            addDialog: false
        })
    }
</script>

<style scoped>

</style>
