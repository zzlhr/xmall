// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import {AjaxPlugin} from 'vux'
import router from './router'
import {TransferDom} from 'vux'
import {XHeader} from 'vux'
import {XButton, Divider, Tabbar, TabbarItem,} from 'vux'
import Vuex from 'vuex'
import {Toast} from 'vux'
import {XNumber} from 'vux'
import VueScroller from 'vue-scroller'
import {ToastPlugin} from 'vux'
import axios from 'axios';
import VueAxios from 'vue-axios';
//font awesome依赖
import {library} from '@fortawesome/fontawesome-svg-core'
import {fas} from '@fortawesome/free-solid-svg-icons'
import {far} from '@fortawesome/free-regular-svg-icons'
import {fab} from '@fortawesome/free-brands-svg-icons'
import {FontAwesomeIcon, FontAwesomeLayers, FontAwesomeLayersText}
    from '@fortawesome/vue-fontawesome'
// import './styles/xshop.scss'
library.add(fas, far, fab);

Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.component('font-awesome-layers', FontAwesomeLayers);
Vue.component('font-awesome-layers-text', FontAwesomeLayersText);

Vue.use(axios, VueAxios);
Vue.use(VueScroller);
Vue.use(ToastPlugin);
Vue.use(ToastPlugin, {position: 'bottom'});

// Vue.use(vuxToastPlugin)
Vue.directive('transfer-dom', TransferDom);
Vue.component('x-header', XHeader);
Vue.component('x-button', XButton);
Vue.component('divider', Divider);
Vue.component('tabbar', Tabbar);
Vue.component('tabbar-item', TabbarItem);
Vue.component('toast', Toast);
Vue.component('x-number', XNumber);


Vue.use(AjaxPlugin);
Vue.use(VueRouter);
Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        login: 0,
        user: {},
        buyCars: {},
    },
    mutations: {
        loginSuccess(state, user) {
            state.login = 1;
            state.user = user;
        },
        setBuyCars(state, buyCars) {
            state.buyCars = buyCars;
        },
    },
    getters: {
        login(state) {
            return state.login;
        },
        user(state) {
            return state.user;
        }
    },

});
FastClick.attach(document.body);

Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
    store,
    router,
    render: h => h(App),
}).$mount('#app-box')
