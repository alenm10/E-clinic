import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        medicines: [],
    },
    mutations: {
        setAllMedicines(state, medicines) {
            Vue.set(state, 'medicines', medicines);
        },
        addMedicine(state, medicine) {
            state.medicines.push(medicine);
        },
        deleteMedicine(state, medicine) {
            let index = state.medicines.findIndex(c => c.id === medicine.id);
            state.medicines.splice(index, 1);
        },
        updateMedicine(state, medicine) {
            state.medicines = [
                ...state.medicines.filter(c => c.id !== medicine.id),
                medicine
            ]
        }
    },
    actions: {
        async getMedicines({rootState, commit}) {
            try {
                let res = await Vue.prototype.$axios.get('/api/medicine', {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllMedicines', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async addMedicineApi({rootState, commit}, medicine) {
            try {
                let {data: added} = await Vue.prototype.$axios.post('/api/medicine', medicine, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(medicine.name);
                commit('addMedicine', added);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteMedicineApi({rootState, commit}, medicine) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/medicine/${medicine.id}`, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteMedicine', medicine);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateMedicineApi({rootState, commit}, medicine) {
            console.log(medicine);
            try {
                let {data: modified} = await Vue.prototype.$axios.put('/api/medicine', medicine, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateMedicine', modified);
            } catch (err) {
                defaultError(err);
            }
        },
    },
};
