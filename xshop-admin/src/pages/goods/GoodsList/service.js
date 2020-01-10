import request from "@/utils/request";


export async function getGoodsList(payload) {
  return request('/goods/getGoodsList',
    {
      method:"POST",
      data: payload
    }
  )
}


