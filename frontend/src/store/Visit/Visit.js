import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";
import JSOG from 'jsog'
import router from "../../router";

export default {
    namespaced: true,
    state: {
        visits: [],
    },
    mutations: {
        setAllVisits(state, visits) {
            Vue.set(state, 'visits', visits);
        },
        addVisit(state, visit) {
            state.visits.push(visit);
        },
        updateVisit(state, visit) {
            state.visits = [
                ...state.visits.filter(c => c.id !== visit.id),
                visit
            ]
        }
    },
    actions: {
        async getVisits({rootState, commit}) {
            try {
                console.log("getVisits apicall");
                let res = await Vue.prototype.$axios.get('/api/visit', {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllVisits', res.data);
            } catch (err) {
                console.error(err);
            }
        },
        async addVisitApi({rootState, commit}, visit) {
            try {
                console.log("visitadd");
                console.log(visit);
                let {data: added} = await Vue.prototype.$axios.post('/api/visit', visit, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(visit.name);
                commit('addVisit', added);
            } catch (err) {
                console.error(err);
            }
        },
        async updateVisitApi({rootState, commit}, toSend) {
            try {
                console.log("updateVisitApi");

                let {data: modified} = await Vue.prototype.$axios.put(`/api/visit/certified/${toSend.visit.id}`, toSend.user, {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateVisit', modified);
            } catch (err) {

                console.error(err);
                router.push("/");
            }
        },
    },
};
