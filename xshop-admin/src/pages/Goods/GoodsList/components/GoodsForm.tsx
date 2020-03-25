import React from 'react';
import {connect, Dispatch} from 'dva';
import {Form, Input, Select, Modal, Button} from 'antd';
import {GoodsDetail} from "@/pages/Goods/GoodsList/data";

export interface GoodsFormProps {
  type: number, // 0添加 1编辑
  goodsId?: string
  dispatch: Dispatch
}

export interface GoodsFormState {
  goodsDetail: GoodsDetail,
  visible: boolean
}

class GoodsForm extends React.Component<GoodsFormProps, GoodsFormState> {

  state = {
    visible: false,
    goodsDetail: {}
  };

  formRef = React.createRef<any>();



  onOpenModal = () => {
    this.setState({visible: true});
    const {type, dispatch, goodsId} = this.props;
    console.log('openModal');
    if (type === 1) {
      dispatch({
        type: 'goods/getGoodsDetail',
        payload: {
          goodsId,
          callback: this.onGoodsDetailCallBack
        }
      })
    }
  };

  onGoodsDetailCallBack = (responseData: GoodsDetail) => {
    console.log('callback:', responseData);
    if (responseData !== undefined && responseData !== null) {
      this.setState({
        goodsDetail: responseData
      });
      const {goodsDetail} = this.state;

      if (goodsDetail !== {}){
        this.formRef.current.setFieldsValue({
          goodsTitle: goodsDetail.goodsTitle,
          categoryId: goodsDetail.categoryId,
          goodsDescribe: goodsDetail.goodsDescribe,
          goodsContent: goodsDetail.goodsContent
        });
      }
    }
  };


  render() {

    let btn;
    let title = '添加商品';
    const {type} = this.props;
    if (type === 0) {
      btn = <Button onClick={this.onOpenModal}>添加</Button>
    }
    if (type === 1) {
      title = "编辑商品";
      btn = <Button onClick={this.onOpenModal}>编辑</Button>
    }

    return (
      <div>
        {btn}
        <Modal visible={this.state.visible} title={title}>
          <Form ref={this.formRef}>
            <Form.Item label="商品标题" name="goodsTitle">
              <Input/>
            </Form.Item>
            <Form.Item label="商品分类" name="categoryId">
              <Select/>
            </Form.Item>
            <Form.Item label="商品描述" name="goodsDescribe">
              <Input/>
            </Form.Item>
            <Form.Item label="商品内容" name="goodsContent">
              <Input/>
            </Form.Item>
          </Form>
        </Modal>
      </div>
    );
  }
}

export default connect(({goods}) => ({}))(GoodsForm)
