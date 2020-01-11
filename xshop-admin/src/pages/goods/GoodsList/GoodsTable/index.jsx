import React, {Component} from 'react';
import {Table, Divider, Tag} from 'antd';
import styles from './index.less';
import {connect} from 'dva'

const columns = [
  {
    title: "商品图片",
    dataIndex: "goodsCover",
    key: "goodsCover",
    render: goodsCover => <img src={goodsCover} alt="goodsCover"/>
  },
  {
    title: '商品名称',
    dataIndex: 'goodsTitle',
    key: 'goodsTitle',
    render: text => <a>{text}</a>,
  },
  {
    title: '分类',
    dataIndex: 'categoryName',
    key: 'categoryName',
  },
  {
    title: '商品状态',
    dataIndex: 'goodsStatus',
    key: 'goodsStatus',
  },
  {
    title: '更新人',
    key: 'updateUserName',
    dataIndex: 'updateUserName',
  },
  {
    title: '更新时间',
    key: 'updateTime',
    dataIndex: 'updateTime',
  },
  {
    title: '创建时间',
    key: 'createTime',
    dataIndex: 'createTime',
  },
  {
    title: '操作',
    key: 'action',
    render: (text, record) => (
      <span>
        <a>Invite {record.name}</a>
        <Divider type="vertical"/>
        <a>Delete</a>
      </span>
    ),
  },
];
@connect(state => ({}))
export default class GoodsTable extends Component {

  constructor(props) {

    super(props);
    this.props = props;
  }



  render() {
    const {list, onChange, current, pageSize, total} = this.props;
    return (
      <div className={styles.container}>
        <div id="components-table-demo-basic">
          <Table columns={columns} dataSource={list} pagination={{
            current: current, pageSize: pageSize, total: total,
            showTotal: ((total) => {
              return `共 ${total} 条`
            }),
            onChange: onChange
          }}/>
        </div>
      </div>
    );
  }
}
