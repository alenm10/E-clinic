import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";
import JSOG from 'jsog'

export default {
    namespaced: true,
    state: {
        interventionType: [],
    },
    mutations: {
        setAllInterventionType(state, interventionType){
            Vue.set(state, 'interventionType', JSOG.decode(interventionType));
        },
        addInterventionType(state, interventionType) {
        	console.log("mutations addInterventionType");
        	console.log(interventionType.name);
            state.interventionType.push(interventionType);
        },
        deleteInterventionType(state, interventionType) {
            let index = state.interventionType.findIndex(c => c.id === interventionType.id);
            state.interventionType.splice(index, 1);
        },
        updateInterventionType(state, interventionType) {
        	console.log("mutations updateInterventionType");
            state.interventionType = [
                ...state.interventionType.filter(c => c.id !== interventionType.id),
                interventionType
            ]
        }
    },
    actions: {
    	async getAllInterventionTypeApi({rootState, commit}) {
            try {
                let res = await Vue.prototype.$axios.get('/api/interventionType/',
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token} });
                console.log("actions getAllInterventionTypeApi = " + res.data);
                res.data.forEach(item => console.log(item));
                commit('setAllInterventionType', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async getClinicInterventionTypeApi({rootState, commit}, clinic) {
            try {
                console.log("actions getClinicInterventionTypeApi clinic = " + clinic);
                let res = await Vue.prototype.$axios.get('/api/interventionType/' + clinic,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token} });
                console.log("actions getClinicInterventionTypeApi = " + res.data);
                res.data.forEach(item => console.log(item));
                commit('setAllInterventionType', res.data);
            } catch (err) {
                defaultError(err);
            }
        },
        async addInterventionTypeApi({rootState, commit}, interventionType) {
            try {
            	console.log("addInterventionType");
                let {data: added} = await Vue.prototype.$axios.post('/api/interventionType', interventionType,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token} });
                commit('addInterventionType', added);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteInterventionTypeApi({rootState, commit}, interventionType) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/interventionType/${interventionType.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token} });
                commit('deleteInterventionType', interventionType);
            } catch (err) {
            	alert("This intervention type is used in already created intervention. It can't be deleted");
                defaultError(err);
            }
        },
        async updateInterventionTypeApi({rootState,commit}, interventionType) {
            console.log("updateInterventionTypeApi");
            try {
                let {data: modified} = await Vue.prototype.$axios.put('/api/interventionType', interventionType,
                		{headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateInterventionType', modified);
            } catch (err) {
                defaultError(err);
            }
        },
    },
};
