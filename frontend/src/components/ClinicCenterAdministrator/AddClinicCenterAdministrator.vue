<template>
    <div>
        <modify-clinic-center-administrator-dialog
                v-model="addDialog"/>
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
    import ModifyClinicCenterAdministratorDialog from "./ModifyClinicCenterAdministratorDialog";

    export default {
        name: "AddClinicCenterAdministrator",
        components: {ModifyClinicCenterAdministratorDialog},
        data: () => ({
            addDialog: false
        })

    }
</script>

<style scoped>
    .fab-br {
        position: absolute;
        right: 2em;
        bottom: 3em;
    }

</style>
