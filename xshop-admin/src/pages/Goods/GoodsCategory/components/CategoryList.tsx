import React from "react";
import {connect, Dispatch} from 'dva';
import {Tree} from "antd";
import {GoodsCategoryVO} from "@/pages/Goods/GoodsCategory/data";


export interface CategoryListProps {
  dispatch: Dispatch,
  categories: Array<GoodsCategoryVO>
}

class CategoryList extends React.Component<CategoryListProps, any> {

  constructor(props: CategoryListProps) {
    super(props);
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/fetchCategoryList',
    })
  }

  toTreeData = (categories: Array<GoodsCategoryVO>) =>{
    const treeData: {key: string, title: String; children: any[]; }[] = []
    categories.forEach(it => {
      let children: {key: string, title: String; children: any[]; }[] = []
      if (it.children.length > 0){
        children = this.toTreeData(it.children)
      }
      treeData.push({
        key: it.categoryId,
        title: it.categoryName,
        children
      })
    })
    return treeData
  }

  render(): React.ReactNode {
    const {categories} = this.props
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

export default connect(({goodsCategory}) => ({
  categories: goodsCategory.categories
}))(CategoryList)
