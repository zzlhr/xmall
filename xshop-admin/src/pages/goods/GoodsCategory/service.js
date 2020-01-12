import {request} from "@/utils/request";


export async function getGoodsCategory(payload) {
  return request('/goods/getGoodsCategory',
    {
      method: "POST",
      data: payload
    }
  )
}


export async function saveGoodsCategory(payload) {
  if (payload.categoryStatus === undefined) {
    payload.categoryStatus = false
  }
  if (typeof (payload.categoryStatus) === "boolean") {
    payload.categoryStatus = payload.categoryStatus ? 1 : 0
  }
  return request('/goods/saveGoodsCategory',
    {
      method: "POST",
      data: payload
    }
  )
}
