<template>
    <div>
        <modify-vacation-request-dialog v-model="addDialog"/>
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
    import ModifyVacationRequestDialog from "./ModifyVacationRequestDialog";

    export default {
        name: "AddVacationRequest",
        components: {ModifyVacationRequestDialog},
        data: () => ({
            addDialog: false
        })
    }
</script>

<style scoped>

</style>
