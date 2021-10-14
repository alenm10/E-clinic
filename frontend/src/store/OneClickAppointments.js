import Vue from 'vue';
import {defaultError} from "../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        oneClicks: [],
        length: 0
    },
    mutations: {
        setAllOneClicks(state, pagedResponse) {
            Vue.set(state, 'oneClicks', pagedResponse.oneClicks);

            state.length = pagedResponse.totalLength;
        },
        addOneClick(state, oneClick) {
            state.oneClicks.push(oneClick);
            state.length++;
        },
        deleteOneClick(state, oneClick) {
            let index = state.oneClicks.findIndex(c => c.id === oneClick.id);
            state.oneClicks.splice(index, 1);
            state.length--;
        },
        updateOneClick(state, oneClick) {
            state.oneClicks = [
                ...state.oneClicks.filter(c => c.id !== oneClick.id),
                oneClick
            ]
        }
    },
    actions: {
        async getOneClicks({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/oneClick/clinic/${payload.clinicID}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllOneClicks', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async addOneClickApi({rootState, commit}, oneClick) {
            try {
                let {data: added} = await Vue.prototype.$axios.post('/api/oneClick', oneClick,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(oneClick.name);
                commit('addOneClick', added);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteOneClickApi({rootState, commit}, oneClick) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/oneClick/${oneClick.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteOneClick', oneClick);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateOneClickApi({rootState, commit}, oneClick) {
            try {
                let {data: modified} = await Vue.prototype.$axios.put('/api/oneClick', oneClick,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateOneClick', modified);
            } catch (err) {
                defaultError(err);
            }
        },

    },
};
