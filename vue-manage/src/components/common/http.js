// 引入 axios
import axios from 'axios'
import qs from 'qs';
import md5 from 'js-md5';


// 让ajax携带cookie
// axios.defaults.withCredentials = true

// request拦截器
// axios.interceptors.request.use(
//   config => {
//     const token = localStorage.getItem();
//     if (token) {
//         config.headers['token'] = token // 让每个请求携带自定义token 请根据实际情况自行修改
//     }
//     return config
//   },
//   error => {
//   // Do something with request error
//     console.log(error) // for debug
//     Promise.reject(error)
//   })

var http = {
  baseURL : 'http://localhost:8000/controller/api',
  /** get 请求
     * @param  {接口地址} url
     * @param  {请求参数} params
     */
  get: function(url) {

    return new Promise((resolve, reject) => {
      axios
        .get(url)
        .then(response => {
          if (response.data.errno === -2) {
            reject('登陆超时,请重新登陆')
          } else if (response.data.error) {
            reject(response.data.error)
          } else {
            resolve(response.data)
          }
        })
        .catch(error => {
          console.log(error);
          reject(error)
        })
    })
  },

  md5:function(v){
    var salt1 = "a";
    var salt2 = "0";
    var salt3 = "=";
    return md5(salt1 +v+ salt2)+salt3
  },

  /** post 请求
     * @param  {接口地址} url
     * @param  {请求参数} params
     */
  post: function(method, params, vue) {


    //显示加载中
    const loading = vue.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    });

    //请求头添加token
    let token = localStorage.getItem('token');
    params.method = method;
    params.token = token;
    //如果有密码，则必须加密传输
    if(params.pwd){
      params.pwd = this.md5(params.pwd)
    }
    return new Promise((resolve, reject) => {
      axios.post(
        this.baseURL, params
      ).then(function (response) {
        console.log(response.data)
        if(response.data.msg == "未登录！"){
            //跳到登录
          window.location.href='/#/login';
          return;
        }
        resolve(response)
        loading.close();
      }.bind(this)
      ).catch(function (error) {
        loading.close();
        console.log(error);
      })
    });
  }

}
export default http
