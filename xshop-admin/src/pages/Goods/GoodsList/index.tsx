import React from "react";
import { PageHeaderWrapper } from "@ant-design/pro-layout";
import GoodsTable from "@/pages/Goods/GoodsList/components/GoodsTable";


export interface GoodsListProps {

}

class GoodsList extends React.Component<GoodsListProps, any>{

  render() {
    return (
      <PageHeaderWrapper content="商品列表">
        <GoodsTable />
      </PageHeaderWrapper>
    );
  }
}

export default GoodsList
