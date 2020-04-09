import request from "@/utils/request";
import {BASE_URL} from "@/utils/config";


export async function getGoodsCategory() {
  return request(`${BASE_URL}goods/getGoodsCategory`, {method: 'POST'})
}
