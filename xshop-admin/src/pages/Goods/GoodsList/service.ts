import request from "@/utils/request";
import {BASE_URL} from "@/utils/config";
import {GoodsMaster, TableSearchData} from "@/pages/Goods/GoodsList/data";

export async function getGoodsList(params: TableSearchData) {
  return request(`${BASE_URL}goods/getGoodsList`, {method: 'POST', data: params})
}
export async function saveGoodsMaster(goods: GoodsMaster) {
  return request(`${BASE_URL}goods/saveGoodsMaster`, {method: 'POST', data: goods})
}
export async function getGoodsCategoryAttrs(param: {goodsCategoryId: number}) {
  return request(`${BASE_URL}goods/getGoodsCategoryAttrs`, {method: 'POST', data: param})
}
export async function getGoodsDetail(param: {goodsId: number}) {
  return request(`${BASE_URL}goods/getGoodsDetail`, {method: 'POST', data: param})
}
