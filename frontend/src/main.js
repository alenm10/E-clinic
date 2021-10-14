import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify';
import axios from 'axios'
import DateTimePicker from 'vuetify-datetime-picker';

Vue.config.productionTip = false;

Vue.prototype.$axios = axios;

const ignoreWarnMessage = 'The .native modifier for v-on is only valid on components but it was used on <div>.';
Vue.config.warnHandler = function (msg, vm, trace) {
    if (msg === ignoreWarnMessage) {
        msg = null;
        vm = null;
        trace = null;
    }
};

Vue.use(DateTimePicker);
new Vue({
    router,
    store,
    vuetify,
    render: h => h(App)
}).$mount('#app')
