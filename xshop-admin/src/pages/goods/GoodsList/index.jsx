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
    }
  }

  render() {
    return (
      <PageHeaderWrapper content="这是一个新页面，从这里进行开发！" className={styles.main}>
        <div
          style={{
            // paddingTop: 100,
            textAlign: 'center',
          }}
        >
          <GoodsTable className="GoodsTable"/>
          {/*<Spin spinning={loading} size="large"/>*/}
        </div>
      </PageHeaderWrapper>
    )
  };
}


