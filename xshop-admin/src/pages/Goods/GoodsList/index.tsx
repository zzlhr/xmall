import React from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import GoodsTable from '@/pages/Goods/GoodsList/components/GoodsTable';
import { Button, Card } from 'antd';
import router from 'umi/router';

export interface GoodsListProps {}

const GoodsList = () => {
  return (
    <PageHeaderWrapper
      content={
        <Button type="link" onClick={() => router.push('/goods/edit?type=0')}>
          添加商品
        </Button>
      }
    >
      <Card>
        <GoodsTable />
      </Card>
    </PageHeaderWrapper>
  );
};

export default GoodsList;
