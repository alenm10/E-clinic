import Vue from 'vue';
import {defaultError} from "../utils/defaultErrorBehavior";
import {emptyMedicalRecord} from "../utils/skeletons";

export default {
    namespaced: true,
    state: {
        medicalRecord: emptyMedicalRecord
    },
    mutations: {
        setMedicalRecord(state, medicalRecord) {
            Vue.set(state, 'medicalRecord', medicalRecord);
        },
        updateMedicalRecord(state, newParts) {
            state.profile = Object.assign({}, state.profile, newParts);
        }
    },
    actions: {
        async getMedicalRecordApi({rootState, commit}, id) {
            try {
                let res = await Vue.prototype.$axios.get(`/api/patient/medical-record/${id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setMedicalRecord', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateMedicalRecordApi({rootState, commit}, payload) {
            try {
                let res = await Vue.prototype.$axios.put(`/api/patient/medical-record/${payload.id}`,
                    payload.medicalRecord,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateMedicalRecord', res.data);
            } catch (err) {
                defaultError(err);
            }
        },

    },
};
