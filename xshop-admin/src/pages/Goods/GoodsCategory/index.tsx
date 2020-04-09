import React from "react";
import {connect} from 'dva';
import {PageHeaderWrapper} from "@ant-design/pro-layout";
import {Button, Card} from "antd";
import CategoryList from "@/pages/Goods/GoodsCategory/components/CategoryList";

class GoodsCategory extends React.Component<any, any>{
  render(): React.ReactNode {
    return (
      <PageHeaderWrapper content="分类管理">
        <Card>
          <Button>添加分类</Button>
          <CategoryList/>
        </Card>
      </PageHeaderWrapper>
    );
  }
}


export default connect()(GoodsCategory)
