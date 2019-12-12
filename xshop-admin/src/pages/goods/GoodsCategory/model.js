import {getGoodsCategory} from "@/pages/goods/GoodsCategory/service";
import {notification} from 'antd'

const Model = {
  namespace: "goodsCategory",
  state: {},
  effects: {
    * getGoodsCategory({payload}, {call, put}) {
      const response = yield call(getGoodsCategory, payload);
      if (response.code === 0) {
        return response;
      } else {
        notification.error({
          message: `请求错误 ${response.code}`,
          description: response.msg,
      });
      }
    }
  },
  reducers: {},

};

export default Model;
