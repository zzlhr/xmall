import React from 'react';
import {Table, Pagination} from 'antd';
import styles from './index.less';

const columns = [
  {
    title: 'ID',
    width: 100,
    dataIndex: 'uid',
    key: 'uid',
    // fixed: 'left',
  },
  {
    title: '用户名',
    width: 200,
    dataIndex: 'username',
    key: 'username',
    // fixed: 'left',
  },
  {
    title: '手机号',
    dataIndex: 'phone',
    key: 'phone',
    width: 150,
  },
  {
    title: '邮箱',
    dataIndex: 'email',
    key: 'email',
    width: 150,
  },
  {
    title: '权限组',
    dataIndex: 'authGroupName',
    key: 'authGroupName',
    width: 150,
  },
  {
    title: '是否管理员',
    dataIndex: 'admin',
    key: 'admin',
    width: 150,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    width: 150,
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    width: 150,
  },
  {
    title: 'Action',
    key: 'operation',
    // fixed: 'right',
    width: 100,
    render: () => <a>action</a>,
  },
];


export default ({data, current, pageSize, total, onChange}) => (
  <div className={styles.container}>
    <div id="components-table-demo-fixed-columns-header">
      <Table
        columns={columns}
        dataSource={data}
        pagination={{
          current: current, pageSize: pageSize, total: total,
          showTotal: ((total) => {
            return `共 ${total} 条`
          }),
          onChange: onChange
        }}
        // pagination={pagination}
        scroll={{
          x: 1500,
          y: 300,
        }}
      />
    </div>
  </div>
);
