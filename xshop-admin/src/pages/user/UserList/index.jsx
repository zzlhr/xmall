import {PageHeaderWrapper} from '@ant-design/pro-layout';
import React, {Component} from 'react';
import {Spin} from 'antd';
import styles from './index.less';
import SearchUsers from './SearchUsers';
import UsersTable from "@/pages/user/UserList/UsersTable";
import {connect} from 'dva'

@connect(() => ({}))
class UserList extends Component {
  state = {
    list: [],
    total: 0, // 总条数
    current: 1, // 当前页
    pageSize: 10,
    searchVal: {},
  };

  queryUsers = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'userList/queryUsers',
      payload: { ...this.state.searchVal, page: this.state.current, pageSize: this.state.pageSize}
    }).then(resp => {
      console.log("then resp", resp);
      this.setState({
        list: resp.data.arr,
        current: resp.data.currentPage,
        pageSize: resp.data.pageSize,
        total: resp.data.totalCount
      })
    });
  };

  componentDidMount() {
    this.queryUsers();
  }

  onPageChange = (page, pageSize) => {
    this.setState({pageSize: pageSize, current: page}, ()=>this.queryUsers());
  };
  handleSearch = (searchVal) => {

    this.setState({searchVal:searchVal, current: 1,}, ()=>this.queryUsers());
  };
  render() {
    return (

      <PageHeaderWrapper content="这是一个新页面，从这里进行开发！" className={styles.main}>
        <SearchUsers handleSearch={this.handleSearch}/>
        <UsersTable data={this.state.list} total={this.state.total} current={this.state.current}
                    pageSize={this.state.pageSize} onChange={this.onPageChange}/>
        <div
          style={{
            paddingTop: 100,
            textAlign: 'center',
          }}
        >
          {/*<Spin spinning={loading} size="large"/>*/}
        </div>
      </PageHeaderWrapper>
    );
  }
}

export default UserList
