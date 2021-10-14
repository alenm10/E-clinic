import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        clinicRooms: [],
        length: 0
    },
    mutations: {
        setAllClinicRooms(state, pagedResponse) {
            Vue.set(state, 'clinicRooms', pagedResponse.clinicRooms);
            state.length = pagedResponse.totalLength;
        },
        addClinicRoom(state, clinicRoom) {
            state.clinicRooms.push(clinicRoom);
            state.length++;
        },
        deleteClinicRoom(state, clinicRoom) {
            let index = state.clinicRooms.findIndex(c => c.id === clinicRoom.id);
            state.clinicRooms.splice(index, 1);
            state.length--;
        },
        updateClinicRoom(state, clinicRoom) {
            state.clinicRooms = [
                ...state.clinicRooms.filter(c => c.id !== clinicRoom.id),
                clinicRoom
            ]
        }
    },
    actions: {
        async getClinicRooms({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/clinicroom/${payload.clinicID}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllClinicRooms', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async addClinicRoomApi({rootState, commit}, clinicRoom) {
            try {
                let {data: added} = await Vue.prototype.$axios.post('/api/clinicroom', clinicRoom,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                console.log(clinicRoom.name);
                commit('addClinicRoom', added);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteRoomApi({rootState, commit}, clinicRoom) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/clinicroom/${clinicRoom.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteClinicRoom', clinicRoom);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateClinicRoomApi({rootState, commit}, clinicRoom) {
            console.log(clinicRoom);
            try {
                let {data: modified} = await Vue.prototype.$axios.put('/api/clinicroom', clinicRoom,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateClinicRoom', modified);
            } catch (err) {
                defaultError(err);
            }
        },
        async searchApi({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                if(!payload.request.duration) payload.request.duration = 30;

                let {data: pagedResponse} = await Vue.prototype.$axios.post(
                    `/api/clinicroom/search/${payload.clinicID}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    payload.request,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllClinicRooms', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        }
    },
};
