import React, { ReactNode } from "react";
import { connect, Dispatch } from 'dva';
import { Tree, Button } from "antd";
import { GoodsCategoryVO } from "@/pages/Goods/GoodsCategory/data";
import { router } from "umi";


export interface CategoryListProps {
  dispatch: Dispatch,
  categories: Array<GoodsCategoryVO>
}

class CategoryList extends React.Component<CategoryListProps, any> {

  constructor(props: CategoryListProps) {
    super(props);
    const { dispatch } = this.props;
    dispatch({
      type: 'goodsCategory/fetchCategoryList',
    })
  }

  editCategoryAttr = (categoryId: number) => {
    router.push(`/goods/categoryAttr?categoryId=${categoryId}`)
  }

  toTreeData = (categories: Array<GoodsCategoryVO>) => {
    const treeData: { key: string, title: String | ReactNode; children: any[]; }[] = []
    categories.forEach(it => {
      let children: { key: string, title: String | ReactNode; children: any[]; }[] = []
      if (it.children.length > 0) {
        children = this.toTreeData(it.children)
      }
      treeData.push({
        key: it.categoryId,
        title: <>{it.categoryName} <Button onClick={() => { this.editCategoryAttr(it.categoryId) }} type="link">编辑参数</Button></>,
        children
      })
    })
    return treeData
  }

  render(): React.ReactNode {
    const { categories } = this.props
    const treeData = this.toTreeData(categories)
    return (
      <Tree
        // checkable
        // defaultExpandedKeys={['0-0-0', '0-0-1']}
        // defaultSelectedKeys={['0-0-0', '0-0-1']}
        // defaultCheckedKeys={['0-0-0', '0-0-1']}
        treeData={treeData}
      />
    );
  }
}

export default connect(({ goodsCategory }: any) => ({
  categories: goodsCategory.categories
}))(CategoryList)
