import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        clinics: [],
        length: 0,
    },
    mutations: {
        setAllClinics(state, pagedResponse) {
        	console.log("setAllClinics = ");
        	console.log(pagedResponse.clinics);
            Vue.set(state, 'clinics', pagedResponse.clinics);
            state.length = pagedResponse.totalLength;
        },
        addClinic(state, clinic) {
            state.clinics.push(clinic);
            state.length++;
        },
        deleteClinic(state, clinic) {
            let index = state.clinics.findIndex(c => c.id === clinic.id);
            state.clinics.splice(index, 1);
            state.length--;
        },
        updateClinic(state, clinic) {
            state.clinics = [
                ...state.clinics.filter(c => c.id !== clinic.id),
                clinic
            ]
        }
    },
    getters: {
        clinicById: state => id => {
            return state.clinics.find(clinic => clinic.id === id);
        }
    },
    actions: {
        async getClinics({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/clinic/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllClinics', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteClinicApi({rootState, commit}, clinic) {
            try {
                await Vue.prototype.$axios.delete(`/api/clinic/${clinic.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteClinic', clinic);
            } catch (err) {
                defaultError(err);
            }
        },
        async search({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.post(
                    `/api/clinic/search/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    payload.request,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllClinics', pagedResponse);
            } catch (err) {
                console.log(err);
                defaultError(err);
            }
        },
    },
};
