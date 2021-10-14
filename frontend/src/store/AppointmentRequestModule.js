import Vue from 'vue';
import {defaultError} from "../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        requests: [],
        length: 0,
    },
    mutations: {
        setAllRequests(state, pagedResponse) {
            Vue.set(state, 'requests', pagedResponse.requests);
            state.length = pagedResponse.totalLength;
        },
        addRequest(state, request) {
            state.requests.push(request);
            state.length++;
        },
        deleteRequest(state, request) {
            let index = state.requests.findIndex(c => c.id === request.id);
            state.requests.splice(index, 1);
            state.length--;
        }
    },
    actions: {
        async getRequestsByClinic({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/appointment-requests/clinic/${payload.clinicID}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllRequests', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async getRequestsByPatient({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/appointment-requests/patient/${payload.patientID}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllRequests', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteRequestApi({rootState, commit}, request) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/appointment-requests/${request.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteRequest', request);
            } catch (err) {
                defaultError(err);
            }
        },
        async approveRequestApi({rootState, commit}, payload) {
            try {
                let res = await Vue.prototype.$axios.post(`/api/appointment-requests/approve`,
                    payload,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteRequest', payload.request);
            } catch (err) {
                defaultError(err);
            }
        },
        async addRequestApi({rootState, commit}, request) {
            try {
                let {data: res} = await Vue.prototype.$axios.post('/api/appointment-requests', request,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('addRequest', res);
            } catch (err) {
                defaultError(err);
                // tu moze ic neki toast
            }
        },
    },
};
