import React, {Component} from 'react';
import {connect} from 'dva';
import {Tree, Button, Modal} from 'antd';
import {
  Form,
  Input,
  Cascader,
  Switch,
} from 'antd';


@connect(state => ({}))
export default class CategoryCascader extends Component {

  constructor(props) {
    super(props);
    this.props = props;
    this.state = {
      categoryOptions: [],
    }
  }

  componentDidMount() {
    this.getGoodsCategory();
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
      let _categoryOptions = [];
      for (let i = 0; i < resp.data.length; i++) {
        const option = {
          value: resp.data[i]['categoryId'],
          label: resp.data[i]['categoryName'],
          isLeaf: false,
        };
        _categoryOptions.push(option);
      }
      this.setState({
        categoryOptions: _categoryOptions,
      })
    });
  };

  getGoodsCategoryChildren = (targetOption) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'goodsCategory/getGoodsCategory',
      payload: {fid: targetOption.value}
    }).then(resp => {
      targetOption.loading = false;
      if (resp === undefined) {
        return;
      }
      if (resp.data.length === 0) {
        targetOption.isLeaf = true;
        return;
      }
      let _categoryOptions = [];
      for (let i = 0; i < resp.data.length; i++) {
        const option = {
          value: resp.data[i]['categoryId'],
          label: resp.data[i]['categoryName'],
          isLeaf: false,
        };
        _categoryOptions.push(option);
      }
      targetOption.children = _categoryOptions;
    });
  };


  categoryLoadData = selectedOptions => {
    const targetOption = selectedOptions[selectedOptions.length - 1];
    targetOption.loading = true;
    console.log(targetOption);
    // load options lazily
    targetOption.loading = false;
    this.getGoodsCategoryChildren(targetOption);
    this.setState({
      categoryOptions: [...this.state.categoryOptions],
    });
  };

  onCategoryChange = (value, selectedOptions) => {
    const {onChange} = this.props;
    onChange(value, selectedOptions)
  };

  render() {
    const {disabled} = this.props;
    return (
      <Cascader
        disabled={disabled}
        options={this.state.categoryOptions}
        loadData={this.categoryLoadData}
        onChange={this.onCategoryChange}
        changeOnSelect
      />
    );
  }


}

