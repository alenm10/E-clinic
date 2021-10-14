import {emptyClinic} from "../../utils/skeletons";
import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";
import router from "../../router";

export default {
    namespaced: true,
    state: {
        clinic: emptyClinic
    },
    mutations: {
        updateClinic(state, newParts) {
            state.clinic = Object.assign({}, state.clinic, newParts);
        }
    },
    actions: {
        async saveClinic({rootState, commit}, clinic) {
            try {
                let {data: res} = await Vue.prototype.$axios.post('/api/clinic', clinic,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                // tu ce ic update za prikaz
                commit('clinics/readClinics/addClinic', res, {root: true});
            } catch (err) {
                defaultError(err);
                // tu moze ic neki toast
            }
        },
        async modifyClinicApi({rootState, commit}, clinic) {
            try {
                let {data: res} = await Vue.prototype.$axios.put('/api/clinic', clinic,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('clinics/readClinics/updateClinic', res, {root: true});
            } catch (err) {
                console.error(err);
                router.push("/");
            }
        }
    },
};
