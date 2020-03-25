import React from 'react'
import {Table, Button} from 'antd';
import {connect, Dispatch} from 'dva';
import {GoodsListVO} from "@/pages/Goods/GoodsList/data";
import {PaginationConfig} from 'antd/lib/pagination';
import {User} from "@/pages/user/UserList/data";
import GoodsForm from "@/pages/Goods/GoodsList/components/GoodsForm";

const columns = [
  {
    title: '商品ID',
    dataIndex: 'goodsId'
  },
  {
    title: '商品标题',
    dataIndex: 'goodsTitle'
  },
  {
    title: '分类名称',
    dataIndex: 'categoryName'
  },
  {
    title: '商品状态',
    dataIndex: 'goodsStatus'
  },
  {
    title: '更新用户',
    dataIndex: 'updateUserName'
  },
  {
    title: '创建时间',
    dataIndex: 'createTime'
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime'
  },
  {
    title: '操作',
    dataIndex: 'action',
    render: (_: any, record: GoodsListVO) => (
      <>
        <GoodsForm type={1} goodsId={record.goodsId} />
      </>
    ),
  }
];


export interface GoodsTableProps {
  dispatch: Dispatch,
  goods: Array<GoodsListVO>,
  pagination: PaginationConfig
}

class GoodsTable extends React.Component<GoodsTableProps, any> {

  constructor(props: GoodsTableProps) {
    super(props);
    const {dispatch} = this.props;

    dispatch({
      type: 'goods/fetchGoodsList',
      payload: {}
    })
  }

  onTableChange = (pagination: PaginationConfig) => {
    console.log('pagination:', pagination);
    const {dispatch} = this.props;
    dispatch({
      type: 'goods/updatePagination',
      payload: pagination,
    });

    dispatch({
      type: 'goods/fetchGoodsList',
      payload: {}
    })

  };

  render() {
    const {goods, pagination} = this.props;
    return (
      <Table rowKey={(record) => record.goodsId} columns={columns} dataSource={goods} pagination={pagination}
             onChange={this.onTableChange}/>
    );
  }
}


export default connect(({goods}: any) => ({
  goods: goods.goods,
  pagination: goods.pagination
}))(GoodsTable)
