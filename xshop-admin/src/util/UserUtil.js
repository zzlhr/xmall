import Vue from 'vue'
const user = {
    loginStatus() {
        const token = localStorage.getItem("token") || Vue.cookie.get("token");
        if (token === undefined || token === null || token.length === 0) {
            return -1;
        }
        return 0;
    },
    getToken() {
        if (this.loginStatus() !== 0) {
            return ""
        }
        return localStorage.getItem("token") || Vue.cookie.get("token");
    }
};


export default user;

