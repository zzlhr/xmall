import request from "@/utils/request";
import {BASE_URL} from "@/utils/config";
import {GoodsMaster} from "@/pages/Goods/GoodsList/data";

export async function getGoodsCategoryAttrs(param: {goodsCategoryId: number}) {
  return request(`${BASE_URL}goods/getGoodsCategoryAttrs`, {method: 'POST', data: param})
}
export async function getGoodsDetail(param: {goodsId: number}) {
  console.log('params:', param);
  return request(`${BASE_URL}goods/getGoodsDetail`, {method: 'POST', data: param})
}
export async function getGoodsCategory(param: {fid?: number, status?: number}) {
  return request(`${BASE_URL}goods/getGoodsCategory`, {method: 'POST', data: param})
}

export async function saveGoodsMaster(param: GoodsMaster) {
  return request(`${BASE_URL}goods/saveGoodsMaster`, {method: 'POST', data: param})
}
