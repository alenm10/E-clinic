<template>
    <v-app>
        <drawer :mini="miniDrawer" @miniVariant="miniDrawer = !miniDrawer"/>
        <nav-bar/>
        <v-content class="content">
            <router-view/>
        </v-content>
    </v-app>
</template>

<script>
    import NavBar from "../components/NavBar";
    import Drawer from "../components/Drawer";

    export default {
        name: "Main",
        components: {Drawer, NavBar},
        data: () => ({
            miniDrawer: true,
        }),
        computed: {
            model() {

            },
        },
    }
</script>

<style scoped>
    .content {
        background-color: #f0f0f0;
    }
</style>
