<template>
    <div>
        <modify-disease v-model="addDialog"/>
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
    import ModifyDisease from "./ModifyDisease";

    export default {
        name: "AddDisease",
        components: {ModifyDisease},
        data: () => ({
            addDialog: false
        })
    }
</script>

<style scoped>

</style>
