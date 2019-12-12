import request from "@/utils/request";


export async function getGoodsCategory(payload) {
  return request('/goods/getGoodsCategory',
    {
      method:"POST",
      data: payload
    }
  )
}
