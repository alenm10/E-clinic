<template>
    <router-view/>
</template>

<script>

    import Main from "./views/Main";

    export default {
        name: 'App',

        components: {
            Main,
        },

        data: () => ({
            //
        }),
    };
</script>

<style>
    ::-webkit-scrollbar {
        width: 0 !important;
        background: transparent !important;
        overflow: hidden !important;
    }

    .fab-br {
        position: fixed;
        right: 2em;
        bottom: 3em;
        z-index: 11;
        transition: all .5s linear;

    }
    .down {
        right: 50%;
        opacity: 0.6;
    }
</style>
