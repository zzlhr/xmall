import React from 'react';
import {Form, Row, Col, Input, Button, Icon} from 'antd';
import styles from './index.less';

let _handleSearch = ()=>{};

class AdvancedSearchForm extends React.Component {
  state = {
    expand: false,
  }; // To generate mock Form.Item
  getFields() {
    const count = this.state.expand ? 10 : 6;
    const {getFieldDecorator} = this.props.form;
    const children = [];
    const searchFieldConf = [
      {
        fieldName: "用户名",
        alias: "username",
        placeholder: "请输入用户名"
      },
      {
        fieldName: "手机号",
        alias: "phoneNum",
        placeholder: "请输入手机号"
      },
      {
        fieldName: "邮箱",
        alias: "email",
        placeholder: "请输入邮箱"
      },
    ];
    for (let i = 0; i < searchFieldConf.length; i++) {
      children.push(
        <Col
          span={6}
          // labelCol={{span: 3, offset: 12}}
          key={i}
          style={{display: i < count ? 'block' : 'none'}}
        >
          <Form.Item label={`${searchFieldConf[i].fieldName}`}>
            {getFieldDecorator(`${searchFieldConf[i].alias}`, {
              rules: [
                {
                  required: false,
                },
              ],
            })(<Input placeholder={`${searchFieldConf[i].placeholder}`}/>)}
          </Form.Item>
        </Col>,
      );
    }

    return children;
  }

  handleSearch = e => {
    // console.log(this.props.handleSearch);
    e.preventDefault();

    this.props.form.validateFields((err, values) => _handleSearch(values));
  };

  handleReset = () => {
    this.props.form.resetFields();
  };

  toggle = () => {
    const {expand} = this.state;
    this.setState({
      expand: !expand,
    });
  };

  render() {
    return (
      <Form className="ant-advanced-search-form"
            onSubmit={this.handleSearch}
            labelCol={{span: 4}}
            wrapperCol={{span: 20}}
            style={{
              marginBottom: "17px"
            }}
      >
        <Row gutter={24}>{this.getFields()}</Row>
        <Row>
          <Col
            span={24}
            style={{
              textAlign: 'right',
            }}
          >
            <Button type="primary" htmlType="submit" onClick={this.handleSearch}>
              搜索
            </Button>
            <Button
              style={{
                marginLeft: 8,
              }}
              onClick={this.handleReset}
            >
              重置
            </Button>
          </Col>
        </Row>
      </Form>
    );
  }
}

const WrappedAdvancedSearchForm = Form.create({
  name: 'advanced_search',
})(AdvancedSearchForm);
export default ({handleSearch}) => {
  _handleSearch = handleSearch;
  return(
  <div className={styles.container}>
    <div id="components-form-demo-advanced-search">
      <div>
        <WrappedAdvancedSearchForm/>
      </div>
    </div>
  </div>
)};
