import React, {Component} from 'react';
import {connect} from 'dva';
import {Tree, Button, Modal} from 'antd';
import {
  Form,
  Input,
  Tooltip,
  Icon,
  Cascader,
  Select,
  Switch,
  Row,
  Col,
  Checkbox,
  AutoComplete,
} from 'antd';


@connect(state => ({}))
class GoodsCategoryForm extends Component {


  ADD_TYPE_ADD_PEER = 1;
  ADD_TYPE_ADD_CHILDREN = 2;

  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      modelName: "",
      visible: false, // 控制model显示隐藏
      categoryOptions: [],
      categoryFormData: {
        categoryId: null,
        categoryFid: null,
        categoryName: null,
        categoryStatus: 1, // 0默认启用
        categorySort: 0,
      }
    }
  }

  componentDidMount() {
    this.getGoodsCategory();
  }

  getGoodsCategory = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/getGoodsCategory',
      payload: {fid: 0}
    }).then(resp => {
      if (resp === undefined) {
        return;
      }
      let _categoryOptions = [];
      for (let i = 0; i < resp.data.length; i++) {
        const option = {
          value: resp.data[i]['categoryId'],
          label: resp.data[i]['categoryName'],
          isLeaf: false,
        };
        _categoryOptions.push(option);
      }
      this.setState({
        categoryOptions: _categoryOptions,
      })
    });
  };

  getGoodsCategoryChildren = (targetOption) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/getGoodsCategory',
      payload: {fid: targetOption.value}
    }).then(resp => {
      targetOption.loading = false;
      if (resp === undefined) {
        return;
      }
      if (resp.data.length === 0) {
        targetOption.isLeaf = true;
        return;
      }
      let _categoryOptions = [];
      for (let i = 0; i < resp.data.length; i++) {
        const option = {
          value: resp.data[i]['categoryId'],
          label: resp.data[i]['categoryName'],
          isLeaf: false,
        };
        _categoryOptions.push(option);
      }
      targetOption.children = _categoryOptions;
    });
  };


  handleOk = () => {
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        this.setState({visible: false});
      }
    });

  };
  handleCancel = () => {
    this.setState({visible: false})
  };

  editModelShow = () => {
    this.setState({visible: true, modelName: "编辑类目"})

  };

  addModelShow = (type) => {
    let fid = 0;
    if (type === this.ADD_TYPE_ADD_CHILDREN) {
      const {item} = this.props;
      fid = item.categoryFid;
    }
    this.setState({visible: true, modelName: "添加类目"})
  };


  saveGoodsCategory = () =>{
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        const {dispatch} = this.props;

        dispatch({
          type: 'goodsCategory/saveGoodsCategory',
          payload: {}
        })

      }
    });
  };

  categoryLoadData = selectedOptions => {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    console.log(targetOption);
    // load options lazily
    targetOption.loading = false;
    this.getGoodsCategoryChildren(targetOption);
    this.setState({
      categoryOptions: [...this.state.categoryOptions],
    });
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
            <Form.Item label="上级类目">
              {getFieldDecorator('categoryFid', {
                rules: [
                  {
                    required: true,
                    message: '请选择父级',
                  },
                ],
              })(<Cascader
                options={this.state.categoryOptions}
                loadData={this.categoryLoadData}
                onChange={this.onCategoryChange}
                changeOnSelect
              />)}
            </Form.Item>
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
