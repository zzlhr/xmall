
import React from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { Card, Select, Row, Col, Button, Table } from 'antd';
import FormItemLabel from 'antd/lib/form/FormItemLabel';

export interface GoodsCategoryAttrProps {
    categoryId: number
}

const tableColumn = [
    {
        title: '序号',
        dataIndex: 'goodsAttrKeyId',
        key: 'goodsAttrKeyId',
    },
    {
        title: '属性名',
        dataIndex: 'goodsAttrKeyName',
        key: 'goodsAttrKeyName',
    },
    {
        title: '排序',
        dataIndex: 'goodsAttrKeySort',
        key: 'goodsAttrKeySort',
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
    },
    {
        title: '更新时间',
        dataIndex: 'updateTime',
        key: 'updateTime',
    },
]
class GoodsCategoryAttr extends React.Component<GoodsCategoryAttrProps, any> {

    constructor(props: GoodsCategoryAttrProps) {
        super(props)
        console.log("props:", props);
    }


    render() {
        const { Option } = Select;
        return (
            <PageHeaderWrapper content={<Button type='link'>添加属性</Button>}>
                <Card>
                    <FormItemLabel label="选择分类" prefixCls="clazz" />
                    <Select placeholder="请选择分类">
                        <Option value="111">111</Option>
                        <Option value="222">222</Option>
                    </Select>
                    <Table columns={tableColumn} />
                </Card>
            </PageHeaderWrapper>
        )
    }
}

export default GoodsCategoryAttr;