import {SearchUserData, User} from "@/pages/user/UserList/data";
import request from "@/utils/request";
import {BASE_URL} from '@/utils/config'

export async function fetchUserList(params: SearchUserData) {
  return request(`${BASE_URL}user/userList`, {method: 'POST', data: params})
}

export async function addUser(params: User) {
  return request(`${BASE_URL}user/addUser`, {method: 'POST', data: params})
}

export async function editUser(params: User) {
  return request(`${BASE_URL}user/editUser`, {method: 'POST', data: params})
}
export async function phoneIsExist(params: {phone: string}) {
  return request(`${BASE_URL}user/phoneIsExist`, {method: 'POST', data: params})
}

