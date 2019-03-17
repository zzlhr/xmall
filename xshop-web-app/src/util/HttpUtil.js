import axios from "axios";

const http = {
  baseurl(name) {
    // const host = "https://xuanhuobang.com/"
    // const baseHost = host + "api/";
    const baseHost = "http://192.168.31.8:8008/"
    // const host = "http://192.168.31.9:8008/"
    // const baseHost = host;
    switch (name) {
      case 'home':
        return baseHost;
      case 'user':
        return baseHost + 'user/';
      case 'enterprise':
        return baseHost + 'enterprise/';
      case 'auth':
        return baseHost + 'auth/';
      case 'goods':
        return baseHost + 'goods/';
      case 'buyCar':
        return baseHost + "buyCar/";
      case 'order':
        return baseHost + 'order/';
      case 'address':
        return baseHost + 'address/';
      case 'app':
        return baseHost + 'app/';
      case 'message':
        return baseHost + 'message/';
      case 'city':
        return baseHost + 'city/';
      default:
        return baseHost
    }

  },

  get(that, apiPath, url, callback) {
    axios.get(this.baseurl(apiPath) + url).then(response => {
      if (response.status !== 200) {
        return;
      }
      callback(response)
    })
  },
  postForm(that, apiPath, url, params, callback) {
    const formData = new FormData();
    for (const param in params) {
      formData.append(param, params[param])
    }
    axios.post(this.baseurl(apiPath) + url, formData)
      .then(function (response) {
        callback(response)
      })
      .catch(function (error) {
        console.log(error);
      });
  },
  post(that, apiPath, url, param, callback) {
    axios.post(this.baseurl(apiPath) + url, param, {emulateJSON: true})
      .then(response => {
        if (response.status !== 200) {
          return;
        } else if (response.data.code !== 0) {
        }
        callback(response)
      })
  },
  postFormAsync(that, apiPath, url, params) {
    const formData = new FormData();
    for (const param in params) {
      formData.append(param, params[param])
    }
    try {
      return axios.post(this.baseurl(apiPath) + url, formData);
    } catch (error) {
      console.error(error);
    }
  }


};

export default http;
