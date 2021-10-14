<template>
    <v-dialog
            :value="value"
            @input="$emit('input', $event)"
            max-width="500px"
    >
        <v-card>
            <v-progress-linear
                    :active="loading"
                    :indeterminate="loading"
                    absolute
                    top
                    height="6px"
                    color="primary"
            ></v-progress-linear>
            <v-card-title class="headline">Password</v-card-title>

            <v-card-subtitle>
                You can change your password here.
            </v-card-subtitle>

            <v-form ref="form" @submit.prevent="save">
                <v-card-text>
                    <v-text-field
                            label="Current password"
                            v-model="currentPassword"
                            :append-icon="showCurrent ? 'mdi-eye' : 'mdi-eye-off'"
                            :type="showCurrent ? 'text' : 'password'"
                            @click:append="showCurrent = !showCurrent"
                            :rules="[rules.required]"
                            :error="oldPasswordError.isError"
                            :error-messages="oldPasswordError.errorMessage"
                            outlined
                            required
                    >
                    </v-text-field>
                    <v-text-field
                            label="Password"
                            v-model="password"
                            :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                            :type="showPassword ? 'text' : 'password'"
                            @click:append="showPassword = !showPassword"
                            :rules="[rules.required, rules.min8]"
                            outlined
                            required
                    >
                    </v-text-field>
                    <v-text-field
                            label="Repeat password"
                            v-model="repeatedPassword"
                            :type="showRepeated ? 'text' : 'password'"
                            :append-icon="showRepeated ? 'mdi-eye' : 'mdi-eye-off'"
                            @click:append="showRepeated = !showRepeated"
                            :rules="[rules.required, repeatedRule]"
                            outlined
                            required
                    >
                    </v-text-field>

                </v-card-text>
                <v-card-actions>

                    <v-btn
                            color="primary"
                            text
                            @click="$emit('input', false)"
                    >
                        Close
                    </v-btn>
                    <v-spacer></v-spacer>

                    <v-btn
                            color="primary"
                            type="submit"
                    >
                        Save
                    </v-btn>
                </v-card-actions>
            </v-form>
        </v-card>
    </v-dialog>
</template>

<script>
    import {emptyUser} from "../../utils/skeletons";

    export default {
        name: "PasswordDialog",
        data: () => ({
            currentPassword: null,
            showCurrent: false,
            password: null,
            showPassword: false,
            repeatedPassword: null,
            showRepeated: false,
            oldPasswordError: {
                isError: false,
                errorMessage: '',
            },
            loading: false,
            rules: {
                required: v => !!v || 'Required!',
                min8: v=> !!v && v.length >= 8 || 'At least 8 characters',
            }
        }),
        computed: {
            repeatedRule() {
                return () => this.password === this.repeatedPassword || `Doesn't match password`;
            }
        },
        props: {
            value: {
                type: Boolean,
                default: false
            },
            user: emptyUser,
            personal: {
                type: Boolean,
                default: false
            }
        },
        methods:{
            save() {
                this.oldPasswordError.isError = false;
                this.oldPasswordError.errorMessage = '';
                if(!this.$refs.form.validate()) return;
                this.loading = true;
                this.changePassword();
            },
            async changePassword() {
                try {
                    let {data: res} = await this.$axios.put(`/api/auth/changepassword/${this.user.id}`,
                        {
                            oldPassword: this.currentPassword,
                            newPassword: this.password,
                            personal: this.personal
                        });
                    this.$store.commit('auth/updateResponse', res);
                    this.$emit('passwordChange');
                    this.$emit('input', false);

                } catch (err) {
                    if(err.response.status === 400) {
                        this.oldPasswordError.isError = true;
                        this.oldPasswordError.errorMessage = 'The old password isn\'t correct';
                    }
                } finally {
                    this.loading = false;
                }
            }
        },
        watch: {
            value(val) {
                if(!val) {
                    this.$refs.form.reset();
                }
            }
        }
    }
</script>

<style scoped>

</style>
