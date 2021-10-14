import Vue from 'vue';

export default {
    namespaced: true,
    state: {
        diseases: [],
    },
    mutations: {
        setAllDiseases(state, diseases) {
            Vue.set(state, 'diseases', diseases);
        },
        addDisease(state, disease) {
            state.diseases.push(disease);
        },
        deleteDisease(state, disease) {
            let index = state.diseases.findIndex(c => c.id === disease.id);
            state.diseases.splice(index, 1);
        },
        updateDisease(state, disease) {
            state.diseases = [
                ...state.diseases.filter(c => c.id !== disease.id),
                disease
            ]
        }
    },
    actions: {
        async getDiseases({rootState, commit}) {
            try {
            	console.log("getDiseases apicall");
                let res = await Vue.prototype.$axios.get('/api/disease', {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllDiseases', res.data);
            } catch (err) {
                console.error(err);
            }
        },
        async addDiseaseApi({rootState, commit}, disease) {
            try {
            	console.log("diseaseadd");
            	console.log(disease);
                let {data: added} = await Vue.prototype.$axios.post('/api/disease', disease, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(disease.name);
                commit('addDisease', added);
            } catch (err) {
                console.error(err);
            }
        },
        async deleteDiseaseApi({rootState, commit}, disease) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/disease/${disease.id}`, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteDisease', disease);
            } catch (err) {
                console.error(err);
            }
        },
        async updateDiseaseApi({rootState, commit}, disease) {
            try {
            	console.log("updateDiseaseApi");
            	console.log(disease);
                let {data: modified} = await Vue.prototype.$axios.put('/api/disease', disease, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateDisease', modified);
            } catch (err) {
                console.error(err);
            }
        },
    },
};
