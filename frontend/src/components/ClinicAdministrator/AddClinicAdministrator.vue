<template>
    <div>
        <modify-clinic-administrator-dialog
                :clinicToUpdate=null
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
    import ModifyClinicAdministratorDialog from "./ModifyClinicAdministratorDialog";

    export default {
        name: "AddClinicAdministrator",
        components: {ModifyClinicAdministratorDialog},
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
