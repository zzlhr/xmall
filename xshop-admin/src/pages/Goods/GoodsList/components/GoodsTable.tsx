import React from 'react'
import {Table, Button} from 'antd';
import {connect, Dispatch} from 'dva';
import {GoodsListVO} from "@/pages/Goods/GoodsList/data";
import {PaginationConfig} from 'antd/lib/pagination';
import router from 'umi/router';

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
        <Button onClick={()=>{router.push(`/goods/edit?goodsId=${record.goodsId}&type=1`)}}>编辑商品</Button>
        <Button onClick={()=>{router.push(`/stock/goodsStock?goodsId=${record.goodsId}&type=1`)}}>设置库存</Button>

        {/* <GoodsForm type={1} goodsId={record.goodsId} /> */}
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
