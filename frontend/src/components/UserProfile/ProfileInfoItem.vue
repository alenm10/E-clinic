<template>
    <div>
        <v-divider/>
        <v-list-item @click="$emit('click', $event)" link :disabled="disabled">
            <v-list-item-content>
                <v-list-item-title>
                    {{ content }}
                </v-list-item-title>
                <v-list-item-subtitle>
                    {{ subtitle }}
                </v-list-item-subtitle>
            </v-list-item-content>
            <v-list-item-action>
                <v-icon v-if="disabled">
                    mdi-lock
                </v-icon>
                <v-icon v-else>
                    mdi-chevron-right
                </v-icon>
            </v-list-item-action>
        </v-list-item>
    </div>
</template>

<script>
    export default {
        name: "ProfileInfoItem",
        props: {
            content: {
                type: String,
                default: "CONTENT",
            },
            subtitle: {
                type: String,
                default: "SUBTITLE",
            },
            disabled: {
                type: Boolean,
                default: false
            }
        }
    }
</script>

<style scoped>

</style>
