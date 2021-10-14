import Vue from 'vue';
import {defaultError} from "../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        patients: [],
        length: 0
    },
    mutations: {
        setAllPatients(state, pagedResponse) {
            Vue.set(state, 'patients', pagedResponse.patients);
            state.length = pagedResponse.totalLength;
        },
        deletePatient(state, patient) {
            let index = state.patients.findIndex(c => c.id === patient.id);
            state.patients.splice(index, 1);
            state.length--;
        },
        updatePatient(state, patient) {
            state.patients = [
                ...state.patients.filter(c => c.id !== patient.id),
                patient
            ]
        }
    },
    actions: {
        async getPatients({rootState, commit}, payload) {
            if(payload.sort?.length === 0) payload.sort = undefined;
            if(payload.desc?.length === 0) payload.desc = undefined;
            try {
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/patient/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});

                commit('setAllPatients', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async deletePatientApi({rootState, commit}, patient) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/patient/${patient.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deletePatient', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async searchApi({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.post(
                    `/api/patient/search/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    payload.request,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllPatients', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
    },
};
