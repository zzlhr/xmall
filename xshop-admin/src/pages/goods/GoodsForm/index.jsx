import {PageHeaderWrapper} from '@ant-design/pro-layout';
import React, {Component} from 'react';
import styles from './index.less';
import GoodsEditForm from './GoodsEditForm';

export default class GoodsForm extends Component {
  constructor(props) {
    super(props);
    this.props = props;
  }

  render() {
    return (
      <PageHeaderWrapper content="这是一个新页面，从这里进行开发！" className={styles.main}>
        <div
          style={{
            paddingBottom: 20,
            // textAlign: 'center',

          }}
        >
          <GoodsEditForm/>
        </div>
      </PageHeaderWrapper>
    );
  }
}
