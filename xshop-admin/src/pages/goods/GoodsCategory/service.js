import request from "@/utils/request";


export async function getGoodsCategory(payload) {
  return request('/goods/getGoodsCategory',
    {
      method:"POST",
      data: payload
    }
  )
}


export async function saveGoodsCategory(payload) {
  return request('/goods/saveGoodsCategory',
    {
      method:"POST",
      data: payload
    }
  )
}
