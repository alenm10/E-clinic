import {emptyClinicAdmin, emptyClinicCenterAdmin} from "../../utils/skeletons";
import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        clinicCenterAdmins: [],
        clinicCenterAdminToUpdate: emptyClinicCenterAdmin
    },
    mutations: {
        setAllClinicCenterAdmins(state, clinicCenterAdmins) {
            Vue.set(state, 'clinicCenterAdmins', clinicCenterAdmins);
        },
        addClinicCenterAdmin(state, clinicCenterAdmin) {
            state.clinicCenterAdmins.push(clinicCenterAdmin);
        },
        updateClinicCenterAdmin(state, clinicCenterAdmin) {
            state.clinicCenterAdmins = [
                ...state.clinicCenterAdmins.filter(c => c.id !== clinicCenterAdmin.id),
                clinicCenterAdmin
            ]
        },

        deleteClinicCenterAdmins(state, clinicCenterAdmin) {
            let index = state.clinicCenterAdmins.findIndex(c => c.id === clinicCenterAdmin.id);
            state.clinicCenterAdmins.splice(index, 1);
        },
    },
    actions: {
        async saveClinicCenterAdmin({rootState, commit}, clinicCenterAdmin) {
            try {
                console.log(clinicCenterAdmin);
                let {data: res} = await Vue.prototype.$axios.post('/api/clinicalcenteradministrator', clinicCenterAdmin,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                // tu ce ic update za prikaz
                commit('addClinicCenterAdmin', res);
            } catch (err) {
                defaultError(err);
                // tu moze ic neki toast
            }
        },
        async getClinicCenterAdmins({rootState, commit}) {
            try {
                let res = await Vue.prototype.$axios.get('/api/clinicalcenteradministrator',
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(res.data);
                commit('setAllClinicCenterAdmins', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async modifyClinicCenterApi({rootState, commit}, clinicCenterAdmin) {
            try {
                let {data: res} = await Vue.prototype.$axios.put(`/api/clinicalcenteradministrator/${clinicCenterAdmin.id}`, clinicCenterAdmin,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateClinicCenterAdmin', res);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteClinicCenterAdminApi({rootState, commit}, clinicCenterAdmin) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/clinicalcenteradministrator/${clinicCenterAdmin.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteClinicCenterAdmins', clinicCenterAdmin);
            } catch (err) {
                defaultError(err);
            }
        }

    },
};
