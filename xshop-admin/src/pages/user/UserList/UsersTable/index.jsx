import React from 'react';
import {Table, Button} from 'antd';
import styles from './index.less';
import SaveUserModel from "@/pages/user/UserList/SaveUserModel";

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
    // dataIndex: 'authGroupName',
    key: 'authGroups',
    width: 150,
    render: (text, obj) => {
      const {authGroups} = obj;
      const authGroupLabels = [];
      for (let i = 0; i < authGroups.length; i++){
        const authGroup = authGroups[i];
        console.log("authGroup:",authGroup);
        authGroupLabels.push(
          <label key={i}>{authGroup.agName}</label>
        )
      }
      return(
        authGroupLabels
      )
    },
  },
  {
    title: '是否管理员',
    key: 'admin',
    width: 150,
    render: (text, obj) =>{
      if (obj.admin === 1){
        return <label>是</label>
      }else{
        return <label>否</label>
      }
    }
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
    title: '操作',
    key: 'operation',
    // fixed: 'right',
    width: 200,
    render: (text, obj) => {
      return(
        <div className="xshopTableOption">
          <SaveUserModel type={2} uid={obj.uid}/>
        </div>
      )
    },
  },
];


export default ({data, current, pageSize, total, onChange}) => (
  <div className={styles.container}>
    <div id="components-table-demo-fixed-columns-header">
      <Table
        size="small"
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
