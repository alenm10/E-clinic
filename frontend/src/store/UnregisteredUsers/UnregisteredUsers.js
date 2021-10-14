import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        unregisteredUsers: [],
    },
    mutations: {
        setAllUnregisteredUsers(state, unregisteredUsers) {
            Vue.set(state, 'unregisteredUsers', unregisteredUsers);
        },
        deleteRequest(state, id) {
            let index = state.unregisteredUsers.findIndex(r => r.id === id);
            state.unregisteredUsers.splice(index, 1);
        },
        updateRequest(state, req) {
            state.unregisteredUsers = [
                ...state.unregisteredUsers.filter(r => r.id !== req.id),
                req
            ]
        }
    },
    actions: {
        async getUnregisteredUsers({rootState, commit}) {
            try {
                let res = await Vue.prototype.$axios.get('/api/unregisteredusers',
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(res.data);
                commit('setAllUnregisteredUsers', res.data);
            } catch (err) {
                console.log(commit);
                defaultError(err, commit);
            }
        },
        async sendEmailApi({rootState, commit}, unregisteredUser) {
            try {
                let {data: user} = await Vue.prototype.$axios.put(`/api/unregisteredusers/${unregisteredUser.id}`,
                    null, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateRequest', user);
            } catch (err) {
                defaultError(err, commit);
            }
        },
        async deleteUnregisteredUserApi({rootState, commit}, user) {
            try {
                let {data: id} = await Vue.prototype.$axios.delete(`/api/unregisteredusers/${user.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(id);
                commit('deleteRequest', id);
            } catch (err) {
                defaultError(err, commit);
            }
        }

    },
};
