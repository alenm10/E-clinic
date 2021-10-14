import Vue from 'vue';
import router from '../router/index'

const tokenResponse = JSON.parse(localStorage.getItem('tokenResponse'));
const loginState = {
    NOT_LOGGED: 0,
    LOGGING: 1,
    LOGGED: 2
};
let initialToken = '';
let initialUser = null;
let initialClinic = null;
let initialRole = '';
let initialLoggedIn = loginState.NOT_LOGGED;
if (tokenResponse) {
    initialToken = tokenResponse.accessToken;
    initialUser = tokenResponse.loggedUser;
    initialRole = initialUser.type;
    initialLoggedIn = loginState.LOGGED;
    initialClinic = tokenResponse.clinic;
}


const AuthModule = {
    namespaced: true,
    state: {
        clinic: initialClinic,
        token: initialToken,
        user: initialUser,
        role: initialRole,
        loggedIn: initialLoggedIn
    },
    mutations: {
        updateResponse(state, tokenResponse) {
            state.token = tokenResponse.accessToken;
            state.user = tokenResponse.loggedUser;
            state.role = tokenResponse.loggedUser.type;
            state.loggedIn = loginState.LOGGED;
            state.clinic = tokenResponse.clinic;
        },
        clearResponse(state) {
            state.token = '';
            state.user = null;
            state.role = '';
            state.loggedIn = loginState.NOT_LOGGED;
            state.clinic = null;
        },
        logging(state, currentState) {
            state.loggedIn = currentState;
        }
    },
    actions: {
        login({state, commit}, authRequest) {
            commit('logging', loginState.LOGGING);
            Vue.prototype.$axios.post('/api/auth/login', authRequest).then(res => {
                localStorage.setItem('tokenResponse', JSON.stringify(res.data));
                commit('updateResponse', res.data);
            }).catch(err => {
                commit('logging', loginState.NOT_LOGGED);
            })

        },
        logout({commit}) {
            localStorage.removeItem('tokenResponse');
            commit('clearResponse');
            router.push('/auth');
        }
    }
};

export default AuthModule;

export function redirectToLogin(commit) {
    localStorage.removeItem('tokenResponse');
    commit('auth/clearResponse', {root: true});
    router.push('/auth');
}

export {loginState};
