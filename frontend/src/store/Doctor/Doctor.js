import Vue from 'vue';
import {defaultError} from "../../utils/defaultErrorBehavior";

export default {
    namespaced: true,
    state: {
        doctors: [],
        length: 0
    },
    mutations: {
        setAllDoctor(state, pagedResponse) {
        	console.log("setAllDoctor = ");
        	console.log(pagedResponse.doctors);
            Vue.set(state, 'doctors', pagedResponse.doctors);
            state.length = pagedResponse.totalLength;
        },
        addDoctor(state, doctor) {
            state.doctors.push(doctor);
            state.length++;
        },
        deleteDoctor(state, doctor) {
            let index = state.doctors.findIndex(c => c.id === doctor.id);
            state.doctors.splice(index, 1);
            state.length--;
        },
        updateDoctor(state, doctor) {

            state.doctors = [
                ...state.doctors.filter(c => c.id !== doctor.id),
                doctor
            ]
        }
    },
    actions: {
        async getDoctor({rootState, commit}, payload) {
            if(payload.sort?.length === 0) payload.sort = undefined;
            if(payload.desc?.length === 0) payload.desc = undefined;
            try {
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/doctor/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});

                commit('setAllDoctor', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async getClinicDoctors({rootState, commit}, payload) {
            try {
            	console.log("getClinicDoctorsApi");
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.get(
                    `/api/doctor/clinic/${payload.clinicID}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllDoctor', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async getDoctorsForTimeType({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                if(!payload.duration) payload.duration = 30;
                let {data: pagedResponse} = await Vue.prototype.$axios.post(
                    `/api/doctor/clinic/time/${payload.clinicID}/${payload.typeID}/${payload.duration}/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    payload.time,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllDoctor', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
        async addDoctorApi({rootState, commit}, doctor) {
            try {
                let {data: added} = await Vue.prototype.$axios.post('/api/doctor', doctor,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('addDoctor', added);
            } catch (err) {
                defaultError(err);
            }
        },
        async deleteDoctorApi({rootState, commit}, doctor) {
            try {
                let res = await Vue.prototype.$axios.delete(`/api/doctor/${doctor.id}`,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('deleteDoctor', doctor);
            } catch (err) {
                defaultError(err);
            }
        },
        async updateDoctorApi({rootState, commit}, doctor) {
            try {
                let {data: modified} = await Vue.prototype.$axios.put('/api/doctor', doctor,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('updateDoctor', modified);
            } catch (err) {
                defaultError(err);
            }
        },
        async searchApi({rootState, commit}, payload) {
            try {
                if(payload.sort?.length === 0) payload.sort = undefined;
                if(payload.desc?.length === 0) payload.desc = undefined;
                let {data: pagedResponse} = await Vue.prototype.$axios.post(
                    `/api/doctor/search/${payload.pageNumber}/${payload.pageSize}/${payload.sort}/${payload.desc}`,
                    payload.request,
                    {headers: {"Authorization": 'Bearer ' + rootState.auth.token}});
                commit('setAllDoctor', pagedResponse);
            } catch (err) {
                defaultError(err);
            }
        },
    },
};
