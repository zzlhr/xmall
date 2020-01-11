import {PageHeaderWrapper} from '@ant-design/pro-layout';
import React, {useState, useEffect, Component} from 'react';
import {Spin} from 'antd';
import styles from './index.less';
import {connect} from "dva"
import GoodsTable from './GoodsTable'

@connect(state => ({}))
export default class GoodsList extends Component {

  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      // searchVal: {},
      list: [],
      current: 1,
      pageSize: 10,
      total: 0
    };
  }
  componentDidMount() {
    this.loadGoodsList();
  }

  loadGoodsList() {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsList/getGoodsList',
      payload: {page: this.state.current, pageSize: this.state.pageSize}
    }).then(resp => {
      if (resp === undefined) {
        return;
      }
      this.setState({
        list: resp.data.arr,
        total: resp.data.totalCount,
        current: resp.data.currentPage,
      })
    });
  }

  onPageChange = (page, pageSize) => {
    this.setState({pageSize: pageSize, current: page}, () => this.loadGoodsList());
  };

  render() {
    return (
      <PageHeaderWrapper content="这是一个新页面，从这里进行开发！" className={styles.main}>
        <div
          style={{
            // paddingTop: 100,
            textAlign: 'center',
          }}
        >
          <GoodsTable className="GoodsTable" list={this.state.list} onChange={this.onPageChange}
                      current={this.state.current}
                      pageSize={this.state.pageSize} total={this.state.total}/>
          {/*<Spin spinning={loading} size="large"/>*/}
        </div>
      </PageHeaderWrapper>
    )
  };
}


