import {PageHeaderWrapper} from '@ant-design/pro-layout';
import styles from './index.less';
import React, {Component} from 'react';
import {connect} from 'dva';
import {Tree} from 'antd';
import WrappedGoodsCategoryForm from "@/pages/goods/GoodsCategory/GoodsCategoryForm";

const {TreeNode} = Tree;

@connect(state => ({}))
class GoodsCategory extends Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      list: [],
    }
  }

  getGoodsCategory = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/getGoodsCategory',
      payload: {fid: 0}
    }).then(resp => {
      if (resp === undefined) {
        return;
      }
      this.setState({
        list: resp.data,
      })
    });
  };


  componentDidMount() {
    this.getGoodsCategory();
  }

  reloadData = () => {
    this.getGoodsCategory();
  };
  renderTreeNodes = data => data.map(item => {
    if (item.children) {
      return (
        <TreeNode title={
          <WrappedGoodsCategoryForm item={item} onChange={this.reloadData}/>
        } key={item.categoryId} dataRef={item}>
          {this.renderTreeNodes(item.children)}
        </TreeNode>
      );
    }
    return <TreeNode title={
      <WrappedGoodsCategoryForm item={item} onChange={this.reloadData}/>
    } key={item.categoryId} dataRef={item}/>;
  });

  render() {

    return (
      <PageHeaderWrapper content="在这里对商品分类进行编辑" className={styles.main}>
        <div style={{paddingBottom: "20px"}}>
          <Tree>{this.renderTreeNodes(this.state.list)}</Tree>
          {/*<Spin spinning={loading} size="large"></Spin>*/}
        </div>
      </PageHeaderWrapper>
    );
  }
}

export default GoodsCategory;
