import request from '@/utils/request';
import {baseHost} from '@/services/host'
export async function fakeAccountLogin(params) {
  return request(`${baseHost}/api/login/account`, {
    method: 'POST',
    data: params,
  });
}
export async function getFakeCaptcha(mobile) {
  return request(`/api/login/captcha?mobile=${mobile}`);
}
