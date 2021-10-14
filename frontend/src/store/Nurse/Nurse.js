import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";
import JSOG from 'jsog'

export default {
    namespaced: true,
    state: {
        nurse: [],
    },
    mutations: {
        setAllNurse(state, nurse) {
            console.log("mutations getall nurse = " + nurse);
            Vue.set(state, 'nurse', JSOG.decode(nurse));
        },
        addNurse(state, nurse) {
            console.log("mutations addNurse");
            console.log(nurse.name);
            state.nurse.push(nurse);
        },
        deleteNurse(state, nurse) {
            let index = state.nurse.findIndex(c => c.id === nurse.id);
            state.nurse.splice(index, 1);
        },
        updateNurse(state, nurse) {
            console.log("updateNurse");
            state.nurse = [
                ...state.nurse.filter(c => c.id !== nurse.id),
                nurse
            ]
        }
    },
    actions: {
        async getNurse({rootState, commit}) {
            try {
                let res = await Vue.prototype.$axios.get('/api/nurse/',
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log("actions getall nurse = " + res.data);
                console.log(res.data);
                commit('setAllNurse', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async getClinicNurse({rootState, commit}, clinicId) {
            try {
                console.log("getClinicNurse = " + clinicId);
                let res = await Vue.prototype.$axios.get('/api/nurse/getNurseForClinic/' + clinicId,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log("actions getClinicNurse nurse = " + res.data);
                console.log(res.data);
                commit('setAllNurse', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async addNurseApi({rootState, commit}, nurse) {
            try {
                console.log("addnurseapi");
                let {data: added} = await Vue.prototype.$axios.post('/api/nurse', nurse,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('addNurse', added);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteNurseApi({rootState, commit}, nurse) {
            try {
                console.log("delete nurse = ");
                console.log(nurse.id);
                let res = await Vue.prototype.$axios.delete(`/api/nurse/${nurse.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteNurse', nurse);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateNurseApi({rootState, commit}, nurse) {
            console.log("updateNurseApi");
            try {
                let {data: modified} = await Vue.prototype.$axios.put('/api/nurse', nurse, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateNurse', modified);
            } catch (err) {
                defaultError(err);
            }
        },
    },
};
