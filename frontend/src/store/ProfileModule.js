import Vue from 'vue';
import {defaultError} from "../utils/defaultErrorBehavior";
import {emptyUser} from "../utils/skeletons";
import router from '../router/index'
export default {
    namespaced: true,
    state: {
        profile: emptyUser
    },
    mutations: {
        setProfile(state, profile) {
            Vue.set(state, 'profile', profile);
        },
        updateProfile(state, newParts) {
            state.profile = Object.assign({}, state.profile, newParts);
        }
    },
    actions: {
        async getProfileApi({rootState, commit}, id) {
            try {
                let res = await Vue.prototype.$axios.get(`/api/user/${id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setProfile', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateProfileApi({rootState, commit}, profile) {
            try {
                let res = await Vue.prototype.$axios.put(`/api/user/${profile.id}`, profile,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateProfile', res.data);
            } catch (err) {
                router.push("/");
                defaultError(err);
            }
        },

    },
};
