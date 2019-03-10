import http from "./HttpUtil";

const NEED_LOGIN_PAGES = [
  'Address',
  'editAddress',
  'orders',
  'uppwd',
  'shoppingCart',
  'my'
];


export default {

  /**
   * 验证token是否可用
   */
  tokenUse(that) {
    const token = localStorage.getItem("token");
    if (token === null || token === undefined || token === "") {
      return false;
    }
    return http.postFormAsync(that, 'user', 'tokenUse', {token: token})
  },
  pageAuth(that) {
    for (let i = 0; i < NEED_LOGIN_PAGES.length; i++) {
      if (that.$route.name === NEED_LOGIN_PAGES[i]) {
        this.tokenUse(that).then((resp) => {
          console.log(resp)
          if (resp.data.code !== 0)
            that.$router.push({name: 'Login', params: {from: that.$route.name}});
        })
      }
    }
    return true;

  }


}
