import {emptyClinicAdmin} from "../../utils/skeletons";
import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        clinicAdmins: [],
        clinicAdminToUpdate: emptyClinicAdmin
    },
    mutations: {
        setAllClinicAdmins(state, clinicAdmins) {
            Vue.set(state, 'clinicAdmins', clinicAdmins);
        },
        addClinicAdmin(state, clinicAdmin) {
            state.clinicAdmins.push(clinicAdmin);
        },
        updateClinicAdmin(state, clinic) {
            state.clinicAdmins = [
                ...state.clinicAdmins.filter(c => c.id !== clinic.id),
                clinic
            ]
        },

        deleteClinicAdmins(state, clinicAdmin) {
            let index = state.clinicAdmins.findIndex(c => c.id === clinicAdmin.id);
            state.clinicAdmins.splice(index, 1);
        },
    },
    actions: {
        async saveClinicAdmin({rootState, commit}, clinicAdmin) {
            try {
                console.log(clinicAdmin);
                let {data: res} = await Vue.prototype.$axios.post('/api/clinicadministrator', clinicAdmin,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                // tu ce ic update za prikaz
                commit('addClinicAdmin', res);
            } catch (err) {
                defaultError(err);
                // tu moze ic neki toast
            }
        },
        async getClinicAdmins({rootState, commit}) {
            try {
                let res = await Vue.prototype.$axios.get('/api/clinicadministrator/getAll',
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(res.data);
                commit('setAllClinicAdmins', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async modifyClinicApi({rootState, commit}, clinic) {
            try {
                let {data: res} = await Vue.prototype.$axios.put(`/api/clinicadministrator/${clinic.id}`, clinic,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateClinicAdmin', res);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteClinicAdminApi({rootState, commit}, clinicAdministrator) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/clinicadministrator/${clinicAdministrator.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteClinicAdmins', clinicAdministrator);
            } catch (err) {
                defaultError(err);
            }
        }

    },
};
