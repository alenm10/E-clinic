<template>
    <div>
        <v-data-table
                :headers="headers"
                :items="unregisteredUsers"
                class="elevation-1"
                :loading="loading"
                loading-text="Gathering all requests..."
        >
            <template v-slot:top>
                <v-toolbar flat color="white">
                    <v-toolbar-title>User requests</v-toolbar-title>
                    <v-divider
                            class="mx-4"
                            inset
                            vertical
                    ></v-divider>
                    <v-spacer></v-spacer>
                </v-toolbar>
            </template>
            <template v-slot:item.verify="{ item }">
                <v-icon
                        @click="sendEmail(item)"
                        color="primary"
                        v-if="!item.emailSent"
                >
                    mdi-email-send-outline
                </v-icon>
                <v-icon
                        @click="sendEmail(item)"
                        color="primary"
                        v-else
                >
                    mdi-email-check
                </v-icon>
            </template>

            <template v-slot:item.remove="{ item }">
                <v-icon
                        @click="removeRequest(item)"
                        color="primary"
                >
                    mdi-email-minus-outline
                </v-icon>
            </template>

            <template v-slot:item.details="{ item }">
                <v-icon
                        color="green"
                >
                    mdi-card-account-details
                </v-icon>
            </template>
            <template v-slot:no-data>
                <p>There are no user requests.</p>
            </template>
        </v-data-table>
    </div>
</template>

<script>
    import {mapActions, mapState} from "vuex";

    export default {
        name: "UnregisteredUsersTable",
        data: () => ({
            loading: false,
            headers: [
                {
                    text: 'Name',
                    align: 'start',
                    value: 'user.name',
                },
                {text: 'Surname', value: 'user.surname'},
                {text: 'Email', value: 'user.email'},
                {text: 'Details', value: 'details', sortable: false, align: 'center'},
                {text: 'Remove', value: 'remove', sortable: false, align: 'center'},
                {text: 'Verify', value: 'verify', sortable: false, align: 'center'},
            ],
        }),
        computed: {
            ...mapState('unregisteredUsers/unregisteredUsers', ['unregisteredUsers']),

        },
        methods: {
            ...mapActions('unregisteredUsers/unregisteredUsers', ['getUnregisteredUsers']),
            ...mapActions('unregisteredUsers/unregisteredUsers', ['sendEmailApi']),
            ...mapActions('unregisteredUsers/unregisteredUsers', ['deleteUnregisteredUserApi']),
            sendEmail(user) {
                this.sendEmailApi(user);
            },
            removeRequest(user) {
                this.deleteUnregisteredUserApi(user);
            }
        },
        created() {
            this.loading = true;
            this.getUnregisteredUsers();
        },
        watch: {
            unregisteredUsers() {
                this.loading = false;
            }
        }

    }
</script>

<style scoped>

</style>
