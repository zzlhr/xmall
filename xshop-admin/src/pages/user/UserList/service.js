import request from "@/utils/request";

export async function queryUsers(params) {
  console.log("come in queryUsers params:", params);
  return request('/user/userList', {
    method: "POST",
    data: params
  },);
}
