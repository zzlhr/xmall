import React from 'react';
import {connect, Dispatch} from 'dva';
import {PageHeaderWrapper} from '@ant-design/pro-layout';
import {Card, Table, Button} from 'antd';
import {UserListStateType} from "@/pages/user/UserList/model";
import {User} from "@/pages/user/UserList/data";
import {PaginationConfig} from 'antd/lib/pagination';
import {AnyAction} from 'redux';
import {Key, SorterResult} from 'antd/lib/table/interface';


const columns = [
  {
    title: '用户id',
    dataIndex: 'uid',
  },
  {
    title: '用户名',
    dataIndex: 'username',
  },
  {
    title: '手机号',
    dataIndex: 'phone',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  {
    title: '操作',
    dataIndex: 'option',
    render: (_: any, record: User) => (
      <>
        <Button>编辑</Button>
      </>
    ),
  }
];

export interface UserListProps {
  dispatch: Dispatch<AnyAction>,
  users: Array<User>
  pagination: PaginationConfig
}


class UserList extends React.Component<UserListProps, any> {

  constructor(props: UserListProps) {
    super(props);
    const {dispatch} = this.props;

    dispatch({
      type: 'userList/fetchUserList',
      payload: {}
    })
  }

  onTableChange = (pagination: PaginationConfig) => {
    console.log('pagination:',pagination);
    const {dispatch} = this.props;
    dispatch({
      type: 'userList/updatePagination',
      payload: pagination,
    });

    dispatch({
      type: 'userList/fetchUserList',
      payload: {}
    })

  };

  render() {
    const {users, pagination} = this.props;
    return (
      <PageHeaderWrapper content="用户管理">
        <Card>
          <Table rowKey={(record) => record.uid} columns={columns} dataSource={users} pagination={pagination}
                 onChange={this.onTableChange}/>
        </Card>
      </PageHeaderWrapper>
    );
  }
}

export default connect(({userList}: { userList: UserListStateType }) => ({
  users: userList.users,
  pagination: userList.pagination
}))(UserList)
