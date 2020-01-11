import {PageHeaderWrapper} from '@ant-design/pro-layout';
import React, {Component} from 'react';
import {Spin, Button, Modal} from 'antd';
import styles from './index.less';
import {connect} from 'dva'
@connect(state => ({}))
class AuthGroup extends Component {
  constructor(props) {
    super(props);
    this.props = props;
    this.state = {}
  }
  componentDidMount() {}

  render() {
    return (
      <PageHeaderWrapper className={styles.main} content={(
        <div>
          {/*<div></div>*/}
        </div>
      )}>
        <div>
          暂未开发
        </div>
      </PageHeaderWrapper>
    );
  }
}

export default AuthGroup
