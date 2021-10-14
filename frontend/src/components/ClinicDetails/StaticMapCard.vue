<template>
    <v-card class="mt-5" outlined>
        <div id="mapId"
             style="width: 100%; height: 100%;"
        ></div>
    </v-card>
</template>

<script>
    export default {
        name: "StaticMapCard",
        data: () => ({
            map: null,
        }),
        props: {
            mapId: {
                type: String,
                default: 'map'
            },
            value: null,
            clickable: {
                type: Boolean,
                default: false
            },
            useAddress: {
                type: Boolean,
                default: false
            },
            address: {
                type: String,
                default: null
            },
        },
    }
</script>

<style scoped>

</style>
