import React from 'react';
import {
  Form,
  Input,
  Cascader,
  Checkbox,
  Button,
  Upload,
  Modal,
  Icon,
  message
} from 'antd';
import styles from './index.less';
import {connect} from 'dva';
import {baseUrl} from '@/utils/request'

@connect(state => ({}))
class GoodsEditForm extends React.Component {
  state = {
    confirmDirty: false,
    autoCompleteResult: [],
    categoryOptions: [], // 分类Option
    goodsFormData: {
      goodsBanner: "",
      goodsCover: "", // cover图片地址
    },
    coverLoading: false,
    picturesLoading: false,
    goodsCoverFileList: [], // cover图片 list
    goodsBannerFileList: [], // 轮播图 list
  };

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
        // values.goodsCover =
        // 处理 goodsCover
        const sendData = values;
        sendData.goodsCover = this.state.goodsFormData.goodsCover;
        sendData.goodsBanner = this.state.goodsFormData.goodsBanner;

        console.log(sendData)
      }
    });
  };


  componentDidMount() {
    this.loadCategoryOptions();
  }

  // 获取分类数据
  loadCategoryOptions() {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/getGoodsCategory',
      payload: {fid: 0}
    }).then(resp => {
      if (resp === undefined) {
        return;
      }
      this.setState({
        categoryOptions: resp.data,
      })
    });
  }

  handleCoverChange = async info => {
    console.log(info);
    if (info.file.state === "uploading") {
      this.setState({
        coverLoading: true,
      })
    } else if (info.file.status === 'done') {
      console.log("done imageUrl:", baseUrl + "/goods/goodsCover/" + info.file.response.data.fileName);
      // Get this url from response in real world.
      this.setState({
        coverLoading: false,
        goodsFormData: {goodsCover: baseUrl + "/goods/goodsCover/" + info.file.response.data.fileName}
      });
    }
    this.setState({
      goodsCoverFileList: [info.file],
    })
  };
  handlePicturesChange = async info => {
    console.log(info);

    if (info.file.state === "uploading") {
      this.setState({
        picturesLoading: true,
      })
    } else if (info.file.status === 'done') {
      console.log("done imageUrl:",);
      // Get this url from response in real world.
      console.log(info.fileList);
      let goodsBanner = [];
      for (let i = 0; i < info.fileList.length; i++) {
        const item = info.fileList[i];
        let respData = item.response;
        if (respData === undefined){
          break;
        }
        let data = respData.data;
        if (data === undefined){
          break;
        }
        let fileName = data.fileName;
        if (fileName === undefined){
          break;
        }
        goodsBanner = [goodsBanner,...[fileName]]
      }
      this.setState({
        picturesLoading: false,
        goodsFormData: {
          goodsBanner: goodsBanner
        }
      });
    }
    // this.state.goodsBannerFileList.push(info);
    this.setState({
      goodsBannerFileList: info.fileList,
    })
  };
  beforeUpload = (file) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('只能上传JPG/PNG格式.');
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
      message.error('图片大小不得大于2MB.');
    }
    return isJpgOrPng && isLt2M;
  };

  render() {
    const {getFieldDecorator} = this.props.form;
    const formItemLayout = {
      labelCol: {
        xs: {
          span: 24,
        },
        sm: {
          span: 4,
        },
      },
      wrapperCol: {
        xs: {
          span: 24,
        },
        sm: {
          span: 16,
        },
      },
    };
    const tailFormItemLayout = {
      wrapperCol: {
        xs: {
          span: 24,
          offset: 0,
        },
        sm: {
          span: 16,
          offset: 4,
        },
      },
    }; // 控制提交按钮位置
    const coverUploadButton = (
      <div>
        <Icon type={this.state.coverLoading ? 'loading' : 'plus'}/>
        <div className="ant-upload-text">上传图片</div>
      </div>
    );
    const picturesUploadButton = (
      <div>
        <Icon type={this.state.picturesLoading ? 'loading' : 'plus'}/>
        <div className="ant-upload-text">上传图片</div>
      </div>
    );
    return (
      <Form {...formItemLayout} onSubmit={this.handleSubmit}>
        <Form.Item label="商品名称">
          {getFieldDecorator('goodsTitle', {
            rules: [
              {
                required: true,
                message: '请输入商品名称',
              },
            ],
          })(<Input/>)}
        </Form.Item>
        <Form.Item label="商品描述" hasFeedback>
          {getFieldDecorator('goodsDescribe', {
            rules: [
              {
                required: true,
                message: '请输入商品描述!',
              },
            ],
          })(<Input.TextArea/>)}
        </Form.Item>
        <Form.Item label="商品状态">
          {getFieldDecorator('goodsStatus', {
            rules: [
              {
                required: false,
              },
            ],
          })(<Checkbox>上架</Checkbox>)}
        </Form.Item>
        <Form.Item label="所属分类">
          {getFieldDecorator('categoryId', {
            rules: [
              {
                required: true,
              },
            ],
          })(<Cascader
            fieldNames={{label: 'categoryName', value: 'categoryId', children: 'children'}}
            options={this.state.categoryOptions}
            loadData={this.loadData}
            onChange={this.onChange}
            changeOnSelect
          />)}
        </Form.Item>
        <Form.Item label="商品封面">
          {getFieldDecorator('goodsCover', {
            valuePropName: 'goodsCoverFileList',
            getValueFromEvent: this.handleCoverChange,
          })(
            <Upload
              name="img"
              listType="picture-card"
              className="avatar-uploader"
              showUploadList={false}
              action={baseUrl + "/goods/uploadGoodsCover"}
              beforeUpload={this.beforeUpload}
              multiple={false}
              fileList={this.state.goodsCoverFileList}
            >
              {this.state.goodsCoverFileList.length > 0 && !this.state.coverLoading ?
                <img src={this.state.goodsFormData.goodsCover} alt="avatar"
                     style={{width: '100%'}}/> : coverUploadButton}
            </Upload>)}
        </Form.Item>

        <Form.Item label="商品轮播图">
          {getFieldDecorator('goodsBanner', {
            valuePropName: 'goodsBanner',
            getValueFromEvent: this.handlePicturesChange,
          })(
            <Upload
              name="img"
              listType="picture-card"
              action={baseUrl + "/goods/uploadGoodsCover"}
              beforeUpload={this.beforeUpload}
              multiple={true}
              showUploadList={true}
              fileList={this.state.goodsBannerFileList}
            >
              {/*TODO: 最大轮播图数量应该通过变量控制*/}
              {this.state.goodsBannerFileList >= 5 ? null : picturesUploadButton}
            </Upload>)}
        </Form.Item>
        <Form.Item {...tailFormItemLayout}>
          <Button type="primary" htmlType="submit">
            下一步
          </Button>
        </Form.Item>
      </Form>
    );
  }
}

const WrappedRegistrationForm = Form.create({
  name: 'register',
})(GoodsEditForm);
export default () => (
  <div className={styles.container}>
    <div id="components-form-demo-register">
      <WrappedRegistrationForm/>
    </div>
  </div>
);
