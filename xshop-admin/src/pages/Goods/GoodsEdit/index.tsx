import React, {ReactNode} from 'react';
import {connect, Dispatch} from 'dva';
import {Form, Input, Select, Card, Upload, Modal, Button, notification} from 'antd';
import {GoodsCategory, GoodsDetail} from "@/pages/Goods/GoodsList/data";
import {PageHeaderWrapper} from "@ant-design/pro-layout";
import {PlusOutlined} from "@ant-design/icons/lib";
import {BASE_URL} from '@/utils/config'
import {UploadChangeParam, UploadFile} from "antd/lib/upload/interface";

export interface GoodsFormProps {
  dispatch: Dispatch,
  location: any,
  goodsCategory: Array<GoodsCategory>
}

export interface GoodsFormState {
  goodsDetail?: GoodsDetail, // 商品详情
  previewVisible: boolean, // 控制预览框显示
  previewImage: string, // 预览url
  goodsCategoryFSelected?: GoodsCategory // 选中的1级分类
  goodsContentFileList?: Array<UploadFile>, // 商品内容图片列表
  goodsCoverFileList?: Array<UploadFile>, // 商品封面
  goodsBannerFileList?: Array<UploadFile> // 商品轮播图
}

const {Option} = Select;

class GoodsEdit extends React.Component<GoodsFormProps, GoodsFormState> {

  state: GoodsFormState = {
    goodsDetail: {},
    previewVisible: false,
    previewImage: '',
    goodsBannerFileList: [],
    goodsCoverFileList: [],
    goodsContentFileList: [],
  };

  formRef = React.createRef<any>();


  async componentDidMount() {

    const {type, goodsId} = this.props.location.query;
    const {dispatch} = this.props;

    // 获取分类下拉
    await dispatch({
      type: 'goodsEdit/getGoodsCategory'
    });
    if (type === "1") {
      // 如果是编辑,获取商品详情
      await dispatch({
        type: 'goodsEdit/getGoodsDetail',
        payload: {
          goodsId,
          callback: this.onGoodsDetailCallBack
        }
      });
    }
  }

  onGoodsDetailCallBack = async (responseData: GoodsDetail) => {
    if (responseData !== undefined && responseData !== null) {
      this.setState({
        goodsDetail: responseData,
      });

      // this.state.editorState.setConvertOptions(responseData.goodsContent);
      const {goodsDetail} = this.state;

      if (goodsDetail !== {}) {
        this.formRef.current.setFieldsValue({
          goodsTitle: goodsDetail!.goodsTitle,
          category1: `${goodsDetail!.category1}`,
          goodsDescribe: goodsDetail!.goodsDescribe,
        });

        const {goodsCategory} = this.props;
        // 更新二级分类
        for (let i = 0; i < goodsCategory.length; i += 1) {
          if (goodsCategory[i].categoryId === goodsDetail?.category1) {
            await this.setState({
              goodsCategoryFSelected: goodsCategory[i]
            });
            this.formRef.current.setFieldsValue({
              category2: `${goodsDetail!.category2}`,
            });
          }
        }

        // 初始化几个上传组件内容
        if (goodsDetail?.goodsBanner !== undefined) {
          const goodsBannerFileList: Array<UploadFile> = JSON.parse(goodsDetail?.goodsBanner)
            .map(item => {
              return {
                uid: item,
                name: 'image.png',
                status: 'done',
                url: item,
              }
            });
          this.setState({
            goodsBannerFileList,
          })
        }

        if (goodsDetail?.goodsCover !== undefined) {
          const goodsCoverFileList: Array<UploadFile> = JSON.parse(goodsDetail?.goodsCover)
            .map(item => {
              return {
                uid: item,
                name: 'image.png',
                status: 'done',
                url: item,
              }
            });
          this.setState({
            goodsCoverFileList,
          })
        }
        if (goodsDetail?.goodsContent !== undefined) {
          const goodsContentFileList: Array<UploadFile> = JSON.parse(goodsDetail?.goodsContent)
            .map(item => {
              return {
                uid: item,
                name: 'image.png',
                status: 'done',
                url: item,
              }
            });
          this.setState({
            goodsContentFileList,
          })
        }

      }


    }
  };

  // 获取上传文件得文件url baseUrl需要用/结尾
  getUploadPictureUrl = (baseUrl: string, fileList: Array<UploadFile>) => {

    const urls = [];
    for (let i = 0; i < fileList.length; i += 1) {
      if (fileList[i].url !== undefined) {
        urls.push(fileList[i].url)
      }
      if (fileList[i].response !== undefined
        && fileList[i].response.data !== undefined
        && fileList[i].response.data.fileName !== undefined) {
        urls.push(baseUrl + fileList[i].response.data.fileName)
      }
    }
    return urls
  };


  handlePreview = (record: any) => {
    this.setState({
      previewVisible: true,
      previewImage: record.url
    })
  };

  handleCancel = () => {
    this.setState({
      previewVisible: false
    })
  };

  handleCoverChange = ({file, fileList}: UploadChangeParam) => {
    this.setState({
      goodsCoverFileList: fileList
    });
    if (file.response !== undefined && file.response.code !== 0) {
      this.setState({
        goodsCoverFileList: fileList.filter(it => it.uid !== file.uid)
      });
      notification.error({
        message: `上传失败 ${file.response.code}`,
        description: file.response.msg,
      });
    }
  };

  handleGoodsBannerChange = ({file, fileList}: UploadChangeParam) => {
    this.setState({
      goodsBannerFileList: fileList
    });
    if (file.response !== undefined && file.response.code !== 0) {
      this.setState({
        goodsBannerFileList: fileList.filter(it => it.uid !== file.uid)
      });
      notification.error({
        message: `上传失败 ${file.response.code}`,
        description: file.response.msg,
      });
    }
  };

  handleContentChange = ({file, fileList}: UploadChangeParam) => {
    this.setState({
      goodsContentFileList: fileList
    });
    if (file.response !== undefined && file.response.code !== 0) {
      this.setState({
        goodsContentFileList: fileList.filter(it => it.uid !== file.uid)
      });
      notification.error({
        message: `上传失败 ${file.response.code}`,
        description: file.response.msg,
      });
    }
  };

  handleGoodsCategoryFSelectedChange = (selected: string) => {

    for (let i = 0; i < this.props.goodsCategory.length; i += 1) {
      if (`${this.props.goodsCategory[i].categoryId}` === selected) {
        this.setState({
          goodsCategoryFSelected: this.props.goodsCategory[i]
        })
      }
    }
  };


  handleSaveGoods = async () => {
    this.formRef.current.validateFields().then(async values => {
      const {goodsCoverFileList, goodsBannerFileList, goodsContentFileList} = this.state;
      if (goodsCoverFileList === undefined || goodsCoverFileList?.length === 0) {
        notification.error({
          message: "错误提示:",
          description: "商品封面不能为空",
        });
      }

      if (goodsBannerFileList === undefined || goodsBannerFileList?.length === 0) {
        notification.error({
          message: "错误提示:",
          description: "商品轮播不能为空",
        });
      }

      if (goodsContentFileList === undefined || goodsContentFileList?.length === 0) {
        notification.error({
          message: "错误提示:",
          description: "商品内容不能为空",
        });
      }
      const goodsCoverUrls: Array<string | undefined> = this.getUploadPictureUrl(
        `${BASE_URL}goods/goodsCover/`, goodsCoverFileList!);
      const goodsBannerUrls: Array<string | undefined> = this.getUploadPictureUrl(
        `${BASE_URL}goods/goodsPicture/`, goodsBannerFileList!);
      const goodsContentUrls: Array<string | undefined> = this.getUploadPictureUrl(
        `${BASE_URL}goods/goodsPicture/`, goodsContentFileList!);
      const payload = {
        ...values,
        goodsCover: JSON.stringify(goodsCoverUrls || '[]'),
        goodsContent: JSON.stringify(goodsContentUrls || '[]'),
        goodsBanner: JSON.stringify(goodsBannerUrls || '[]'),
      };
      const {type, goodsId} = this.props.location.query;
      if (type === "1") {
        payload.goodsId = goodsId;
      }
      // console.log('最终提交数据:', payload);
      const {dispatch} = this.props;

      await dispatch({
        type: 'goodsEdit/saveGoodsMaster',
        payload
      })

    }).catch(errorInfo => {
      console.log(errorInfo)
    })
  };

  public render() {
    const {type} = this.props.location.query;
    const {goodsCategory} = this.props;
    const {previewVisible, goodsContentFileList, previewImage, goodsCoverFileList, goodsBannerFileList} = this.state;
    const title = type === "1" ? '编辑商品' : "添加商品";
    const uploadButton = (
      <div>
        <PlusOutlined/>
        <div className="ant-upload-text">上传</div>
      </div>
    );
    const goodsCategoryOption: Array<ReactNode> = goodsCategory
      .map(d => <Option value={`${d.categoryId}`} key={`${d.categoryId}`}>{d.categoryName}</Option>);
    const {goodsCategoryFSelected} = this.state;
    let goodsCategoryCOption;
    if (goodsCategoryFSelected !== undefined) {
      goodsCategoryCOption = goodsCategoryFSelected.children.map(d => <Option value={`${d.categoryId}`}
                                                                              key={`${d.categoryId}`}>{d.categoryName}</Option>)
    }

    // 默认选中内容

    return (
      <PageHeaderWrapper content={title}>
        <Card>
          <Form ref={this.formRef}>
            <Form.Item label="商品标题" name="goodsTitle"
                       rules={[{required: true, message: '请输入商品标题'}]}>
              <Input/>
            </Form.Item>
            <Form.Item label="一级分类" name="category1" rules={[{required: true, message: '请选择一级分类'}]}>
              <Select onChange={this.handleGoodsCategoryFSelectedChange}>{goodsCategoryOption}</Select>
            </Form.Item>
            <Form.Item label="二级分类" name="category2" rules={[{required: true, message: '请选择二级分类'}]}>
              <Select>{goodsCategoryCOption}</Select>
            </Form.Item>
            <Form.Item label="商品描述" name="goodsDescribe" rules={[{required: true, message: '请输入商品描述'}]}>
              <Input/>
            </Form.Item>
            <Form.Item label="商品封面" name="goodsCover">
              <Upload
                name="img"
                action={`${BASE_URL}goods/uploadGoodsCover`}
                listType="picture-card"
                fileList={goodsCoverFileList}
                onPreview={this.handlePreview}
                onChange={this.handleCoverChange}
              >
                {goodsCoverFileList!.length >= 1 ? null : uploadButton}
              </Upload>
              <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                <img alt="example" style={{width: '100%'}} src={previewImage}/>
              </Modal>
            </Form.Item>
            <Form.Item label="商品轮播" name="goodsBanner">
              <Upload
                name="img"
                action={`${BASE_URL}goods/uploadGoodsPicture`}
                listType="picture-card"
                fileList={goodsBannerFileList}
                onPreview={this.handlePreview}
                onChange={this.handleGoodsBannerChange}
              >
                {goodsBannerFileList!.length >= 5 ? null : uploadButton}
              </Upload>
              <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                <img alt="example" style={{width: '100%'}} src={previewImage}/>
              </Modal>
            </Form.Item>

            <Form.Item label="商品内容" name="goodsContent">
              <Upload
                name="img"
                action={`${BASE_URL}goods/uploadGoodsPicture`}
                listType="picture-card"
                fileList={goodsContentFileList}
                onPreview={this.handlePreview}
                onChange={this.handleContentChange}
              >
                {goodsContentFileList!.length >= 8 ? null : uploadButton}
              </Upload>
              <Modal visible={previewVisible} footer={null} onCancel={this.handleCancel}>
                <img alt="example" style={{width: '100%'}} src={previewImage}/>
              </Modal>
            </Form.Item>
            <Button onClick={this.handleSaveGoods}>保存</Button>
          </Form>
        </Card>
      </PageHeaderWrapper>
    );
  }
}

export default connect(({goodsEdit}: any) => ({
  goodsCategory: goodsEdit.goodsCategory
}))(GoodsEdit)
