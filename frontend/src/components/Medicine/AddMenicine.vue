<template>
    <div>
        <modify-medicine v-model="addDialog"/>
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
    import ModifyMedicine from "./ModifyMedicine";

    export default {
        name: "AddMedicine",
        components: {ModifyMedicine},
        data: () => ({
            addDialog: false
        })
    }
</script>

<style scoped>

</style>
