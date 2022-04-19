import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

const now = new Date();

export default new Vuex.Store({
    state:{
        routes:[],
        sessions: {},
        admins: [],
        currentAdmin: JSON.parse(window.sessionStorage.getItem('user')),
        currentSession: null
    },
    mutations:{
        INIT_ADMIN(state,admin){
            state.currentAdmin = admin;
        },
        initRoutes(state,data){
            state.routes = data;
        }
    },
    actions:{

    }
})