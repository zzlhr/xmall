import React from 'react'
import { connect } from 'dva'
import { PageHeaderWrapper } from '@ant-design/pro-layout'
import { Card } from 'antd'

class GoodsStock extends React.Component<any, any> {

    render(){
        return(
            <PageHeaderWrapper content="商品库存">
                <Card>

                </Card>
            </PageHeaderWrapper>
        )
    }
}

export default connect(({ }: any) => ({}))(GoodsStock)