import React from "react";
import {PageHeaderWrapper} from "@ant-design/pro-layout";
import GoodsTable from "@/pages/Goods/GoodsList/components/GoodsTable";
import {Button, Card} from 'antd';
import router from 'umi/router';

export interface GoodsListProps {

}

class GoodsList extends React.Component<GoodsListProps, any> {

  render() {
    return (
      <PageHeaderWrapper content="商品列表">
        <Card>
          <Button onClick={() => router.push('/goods/edit?type=0')}>添加商品</Button>
          <GoodsTable/>
        </Card>
      </PageHeaderWrapper>
    );
  }
}

export default GoodsList
