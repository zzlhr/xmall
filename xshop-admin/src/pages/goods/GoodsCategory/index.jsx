import {PageHeaderWrapper} from '@ant-design/pro-layout';
import styles from './index.less';
import React, {Component} from 'react';
import {connect} from 'dva';
import {Tree, Button} from 'antd';
import WrappedGoodsCategoryForm from "@/pages/goods/GoodsCategory/GoodsCategoryForm";

const {TreeNode} = Tree;

@connect(state => ({}))
class GoodsCategory extends Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      list: [],
      // searchVal: {},
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
  getGoodsCategoryChildren = (treeNode, resolve) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/getGoodsCategory',
      payload: {fid: treeNode.props.dataRef.categoryId}
    }).then(resp => {
      if (resp === undefined) {
        return;
      }
      treeNode.props.dataRef.children = resp.data;
      this.setState({
        list: [...this.state.list],
      });
      resolve();
    });
  };


  componentDidMount() {
    this.getGoodsCategory();
  }

  onLoadData = treeNode =>
    new Promise(resolve => {
      console.log(treeNode);
      if (treeNode.props.children) {
        resolve();
        return;
      }
      this.getGoodsCategoryChildren(treeNode, resolve);
    });
  renderTreeNodes = data => data.map(item => {
    if (item.children) {
      return (
        <TreeNode title={
          <WrappedGoodsCategoryForm item={item}/>
        } key={item.categoryId} dataRef={item}>
          {this.renderTreeNodes(item.children)}
        </TreeNode>
      );
    }
    return <TreeNode title={
        <WrappedGoodsCategoryForm item={item}/>
    } key={item.categoryId} dataRef={item}/>;
  });

  render() {

    return (
      <PageHeaderWrapper content="这是一个新页面，从这里进行开发！" className={styles.main}>
        <div>
          <Tree loadData={this.onLoadData}>{this.renderTreeNodes(this.state.list)}</Tree>
          {/*<Spin spinning={loading} size="large"></Spin>*/}
        </div>
      </PageHeaderWrapper>
    );
  }
}

export default GoodsCategory;
