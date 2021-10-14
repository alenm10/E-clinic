<template>
    <v-app-bar
            absolute
            color="white"
            flat
            app
    >
        <v-toolbar-title>eClinic</v-toolbar-title>

        <v-spacer></v-spacer>
        <v-btn icon>
            <v-icon dense>mdi-apps</v-icon>
        </v-btn>
        <avatar-menu :menu-active="menuActive"></avatar-menu>
    </v-app-bar>
</template>

<script>
    import AvatarMenu from "./Auth/AvatarMenu";

    export default {
        name: "NavBar",
        components: {AvatarMenu},
        data: () => ({
            menuActive: false
        }),
    }
</script>

<style scoped>

</style>
