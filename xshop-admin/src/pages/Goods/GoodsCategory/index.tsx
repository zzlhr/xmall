import React from "react";
import { connect } from 'dva';
import { PageHeaderWrapper } from "@ant-design/pro-layout";
import { Button, Card } from "antd";
import CategoryList from "@/pages/Goods/GoodsCategory/components/CategoryList";

const GoodsCategory = () => {
  return (
    <PageHeaderWrapper content={<Button type="link">添加分类</Button>}>
      <Card>
        <CategoryList />
      </Card>
    </PageHeaderWrapper>
  );
}


export default connect()(GoodsCategory)
