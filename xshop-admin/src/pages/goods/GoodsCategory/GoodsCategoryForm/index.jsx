import React, {Component} from 'react';
import {connect} from 'dva';
import {Button, Modal} from 'antd';
import {
  Form,
  Input,
  Switch,
} from 'antd';
import CategoryCascader from "@/components/CategoryCascader";


@connect(state => ({}))
class GoodsCategoryForm extends Component {


  ADD_TYPE_ADD_PEER = 1; // 添加同级
  ADD_TYPE_ADD_CHILDREN = 2; // 添加子级
  ADD_TYPE_EDIT = 3; // 编辑

  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      type: 0,
      modelName: "",
      visible: false, // 控制model显示隐藏
      categoryOptions: [],
      categoryFormData: {
        categoryId: null,
        categoryFid: null,
        categoryName: null,
        categoryStatus: 1, // 1默认启用
        categorySort: 0,
      }
    }
  }


  handleOk = () => {
    const {dispatch} = this.props;
    this.props.form.validateFieldsAndScroll((err, values) => {
      console.log("save val:", values);
      if (!err) {
        console.log('Received values of form: ', values);
        this.setState({visible: false});
        let categoryId = null;
        if (this.state.type === this.ADD_TYPE_EDIT) {
          categoryId = this.state.categoryFormData.categoryId
        }
        dispatch({
          type: "goodsCategory/saveGoodsCategory",
          payload: {categoryId: categoryId, categoryFid: this.state.categoryFormData.categoryFid, ...values}
        }).then(resp => {
          const {onChange} = this.props;
          onChange();
        })
      }
    });

  };
  handleCancel = () => {
    this.setState({visible: false})
  };
  cleanModelData = () => {
    const {form} = this.props;
    form.setFieldsValue({
      categoryId: null,
      categoryFid: null,
      categoryName: null,
      categoryStatus: false,
      categorySort: 0,
    });
  };
  editModelShow = () => {
    this.setState({
      visible: true, modelName: "编辑类目", type: this.ADD_TYPE_EDIT,
    });
    const {item, form} = this.props;
    this.cleanModelData();
    form.setFieldsValue({
      categoryId: item.categoryId,
      categoryFid: item.categoryFid,
      categoryName: item.categoryName,
      categoryStatus: item.categoryStatus === 1,
      categorySort: item.categorySort,
    });
    this.setState({
      categoryFormData: {
        categoryId: item.categoryId,
        categoryFid: item.categoryFid,
        categoryName: item.categoryName,
        categoryStatus: item.categoryStatus === 1,
        categorySort: item.categorySort,
      }
    })


  };

  addModelShow = (type) => {

    this.setState({visible: true, modelName: "添加类目", type: type});
    this.cleanModelData();

    let fid = 0;
    const {item, form} = this.props;

    if (type === this.ADD_TYPE_ADD_PEER) {
      fid = item.categoryFid;

    }
    if (type === this.ADD_TYPE_ADD_CHILDREN) {
      fid = item.categoryId;
    }

    this.setState({categoryFormData: {categoryFid: fid}})

  };


  onCategoryChange = (value, selectedOptions) => {
    console.log(value, selectedOptions);
  };

  render() {
    const formItemLayout = {
      labelCol: {
        xs: {span: 24},
        sm: {span: 4},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 20},
      },
    };

    const {getFieldDecorator} = this.props.form;
    const {item} = this.props;
    return (
      <div>
        <span>{item.categoryName}</span>
        <Button type="link" onClick={this.editModelShow}>修改</Button>
        <Button type="link" onClick={() => {
          this.addModelShow(this.ADD_TYPE_ADD_PEER)
        }}>添加</Button>
        <Button type="link" onClick={() => {
          this.addModelShow(this.ADD_TYPE_ADD_CHILDREN)
        }}>添加子类目</Button>

        <Modal
          title={this.state.modelName}
          visible={this.state.visible}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
        >
          <Form {...formItemLayout}>
            {/*<Form.Item label="上级类目">*/}
            {/*  {getFieldDecorator('categoryFid', {*/}
            {/*    rules: [*/}
            {/*      {*/}
            {/*        required: false,*/}
            {/*        message: '请选择父级',*/}
            {/*      },*/}
            {/*    ],*/}
            {/*  })(<CategoryCascader*/}
            {/*    disabled={this.state.type === this.ADD_TYPE_ADD_PEER || this.state.type === this.ADD_TYPE_EDIT}*/}
            {/*    onChange={this.onCategoryChange}/>)}*/}
            {/*</Form.Item>*/}
            <Form.Item label="类目名称">
              {getFieldDecorator('categoryName', {
                rules: [
                  {
                    required: true,
                    message: '请输入类目名称',
                  },
                ],
              })(<Input/>)}
            </Form.Item>
            <Form.Item label="是否启用">
              {getFieldDecorator('categoryStatus', {valuePropName: 'checked'})(<Switch/>)}
            </Form.Item>
          </Form>
        </Modal>
      </div>
    );
  }


}

export default Form.create({name: "category_form"})(GoodsCategoryForm);
