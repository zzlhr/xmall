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
import {baseHost} from "@/services/host";

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
        const sendData = values;
        console.log(this.state.goodsBannerFileList.length);
        // 处理上传数据
        let bannerFiles = [];
        for (let i = 0; i < this.state.goodsBannerFileList.length; i++) {
          console.log("itemii:", i);
          const item = this.state.goodsBannerFileList[i];
          console.log("item", item);
          console.log("fileName:", item.response.data.fileName);
          bannerFiles.push(item.response.data.fileName)
        }
        if (this.state.goodsCoverFileList.length !== 1) {
          message.error('封面不能为空!', 20);
        }
        sendData.goodsCover = this.state.goodsCoverFileList[0].response.data.fileName;
        sendData.goodsBanner = bannerFiles;
        console.log(sendData);
      } else {
        console.log(err)
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

    console.log("handleCoverChange:", info);

    if (info.file.state === "uploading") {
      this.setState({
        coverLoading: true,
      })
    }
    if (info.file.status === 'done') {
      this.setState({
        coverLoading: false,
      });
    }
    // this.state.goodsBannerFileList.push(info);
    console.log(info.fileList.length);
    if (info.fileList.length === 1){
      this.setState({
        goodsCoverFileList: info.fileList,
      })
    }
  };
  handlePicturesChange = async info => {

    console.log("handlePicturesChange:", info);

    if (info.file.state === "uploading") {
      this.setState({
        picturesLoading: true,
      })
    }
    if (info.file.status === 'done') {
      this.setState({
        picturesLoading: false,
      });
    }
    // this.state.goodsBannerFileList.push(info);
    this.setState({
      goodsBannerFileList: info.fileList,
    })
  };

  beforeCoverUpload = (file) => {
    const isMax = this.state.goodsCoverFileList.length <= 1;
    if (this.state.goodsCoverFileList.length >= 1){
      message.error("最多上传1个封面图片!");
    }
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('只能上传JPG/PNG格式.');
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
      message.error('图片大小不得大于2MB.');
    }
    return isJpgOrPng && isLt2M && isMax;
  };
  beforePictureUpload = (file) => {
    const isMax = this.state.goodsBannerFileList.length <= 6;
    if (this.state.goodsBannerFileList.length >= 6){
      message.error("最多上传6个轮播图片!");
    }
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
      message.error('只能上传JPG/PNG格式.');
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
      message.error('图片大小不得大于2MB.');
    }
    return isJpgOrPng && isLt2M && isMax;
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
                message: '请选择产品所属分类!',
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
              showUploadList={true}
              action={baseUrl + "/goods/uploadGoodsCover"}
              beforeUpload={this.beforeCoverUpload}
              multiple={false}
              openFileDialogOnClick={this.state.goodsCoverFileList.length === 0} // 限制上传数量,只能上传一张
              fileList={this.state.goodsCoverFileList}
            >
              {coverUploadButton}
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
              action={baseUrl + "/goods/uploadGoodsPicture"}
              beforeUpload={this.beforePictureUpload}
              multiple={true}
              //TODO: 最大轮播图数量应该通过变量控制,并且需要在多选图片时限制数量
              openFileDialogOnClick={this.state.goodsBannerFileList.length <= 5} // 限制上传数量,只能上传6张
              showUploadList={true}
              fileList={this.state.goodsBannerFileList}
            >
              {/*TODO: 最大轮播图数量应该通过变量控制*/}
              {picturesUploadButton}
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
