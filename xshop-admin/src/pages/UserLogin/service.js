import request from '@/utils/request';

export async function adminLogin(params) {
  return request('/user/loginAdmin', {
    requestType: "form",
    method: 'POST',
    data: params,
  });
}
export async function getFakeCaptcha(mobile) {
  return request(`/api/login/captcha?mobile=${mobile}`);
}
