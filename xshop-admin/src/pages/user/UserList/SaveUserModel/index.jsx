import React, {Component} from 'react'
import {Modal, Button, Form, Input, Select, Switch} from 'antd'
import {connect} from 'dva'

const {Option} = Select;

@connect(status => ({}))
class SaveUserModel extends Component {
  constructor(props) {
    super(props);
    this.state = {
      userModelVisible: false,
      user: {
        email: "",
        password: "",
        phone: "",
        username: "",
        defaultAuthGroupValue: [],
      },
      authGroups: []
    }
  }

  //清理UserModel数据
  cleanModelHandler = () => {
    this.props.form.resetFields();
  };

  showModelHandler = () => {
    const {dispatch} = this.props;
    const that = this;
    dispatch({
      type: "userList/getAuthGroups",
      payload: {}
    }).then((resp) => {
      console.log("getAuthGroups:", resp.data);
      that.setState({authGroups: resp.data})
    });
    // 清理userModelData
    // this.cleanUserModel();
    const {uid} = this.props;
    if (this.props.type === 2) {
      // 查询用户
      console.log("uid:", uid);
      dispatch({
        type: "userList/getUserByUid",
        payload: {uid: uid}
      }).then((resp) => {
        console.log("resp:", resp.data);
        const respData = resp.data;
        const {form: {setFieldsValue}} = that.props;
        const authGroups = respData.authGroups;
        const defaultAuthValues = []; // 回显权限组变量
        for (let i = 0; i < authGroups.length; i++) {
          defaultAuthValues.push({key: authGroups[i].agid});
        }
        that.setState({defaultAuthGroupValue: defaultAuthValues});
        console.log("defaultAuthValues：",defaultAuthValues);
        // console.log("respData:",respData);
        setFieldsValue(respData);
      })

    }
    this.setState({userModelVisible: true})
  };

  //  userModel被关闭
  hideModelHandler = () => {
    this.setState({userModelVisible: false})
  };

  onUserModelSubmit = () => {
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log("onUserModelSubmit:", values);
        const {dispatch} = this.props;
        const authGroups = [];
        for (let i = 0; i < values.authGroup.length; i++) {
          authGroups.push(values.authGroup[i].key);
        }

        if (this.props.type === 1){

          dispatch({
            type:"userList/addUser",
            payload: {...values, authGroup: authGroups}
          });
        }

        if (this.props.type === 2){
          dispatch({
            type:"userList/editUser",
            payload: {...values, uid: this.props.uid, authGroup: authGroups}
          });
        }

        this.hideModelHandler();
        this.cleanModelHandler();
      }
    });
  };

  render() {
    const {getFieldDecorator} = this.props.form;

    const authGroupOptions = [];
    for (let authGroup of this.state.authGroups) {
      authGroupOptions.push(
        <Option key={authGroup.agid} value={authGroup.agid}>{authGroup.agName}</Option>
      )
    }
    return (
      <div>
        {this.props.type === 2 ? <Button type="link" onClick={this.showModelHandler}>修改</Button> :
          <Button onClick={this.showModelHandler}>添加用户</Button>}
        <Modal
          title={this.props.type === 2 ? "修改用户" : "添加用户"}
          visible={this.state.userModelVisible}
          onOk={this.onUserModelSubmit}
          onCancel={this.hideModelHandler}
        >
          <Form>
            <Form.Item label="用户名">
              {getFieldDecorator('username', {
                rules: [{required: true, message: '请输入用户名'}],
              })(<Input/>)}
            </Form.Item>

            {
              this.props.type === 1 &&
              <Form.Item label="密码">
                {getFieldDecorator('password', {
                  rules: [{required: true, message: '请输入密码'}],
                })(<Input.Password/>)}
              </Form.Item>
            }


            <Form.Item label="手机号">
              {getFieldDecorator('phone', {
                rules: [{required: true, message: '请输入手机号'}],
              })(<Input/>)}
            </Form.Item>

            <Form.Item label="邮箱">
              {getFieldDecorator('email', {
                rules: [{required: true, message: '请输入邮箱'}],
              })(<Input/>)}
            </Form.Item>
            <Form.Item label="选择权限组">
              {getFieldDecorator('authGroup', {
                initialValue: this.state.defaultAuthGroupValue,
                rules: [
                  {required: true, message: '请选择权限组!', type: 'array'},
                ],
              })(
                <Select labelInValue mode="multiple" placeholder="请选择权限组">
                  {authGroupOptions}
                </Select>
              )}
            </Form.Item>
            <Form.Item label="启用用户">
              {getFieldDecorator('status', {valuePropName: 'checked'})(<Switch/>)}
            </Form.Item>
            <Form.Item label="超级管理员">
              {getFieldDecorator('admin', {valuePropName: 'checked'})(<Switch/>)}
            </Form.Item>
          </Form>
        </Modal>
      </div>
    )
  }
}

export default Form.create({name: "save_user"})(SaveUserModel);
