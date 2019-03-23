<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>消息列表</span>
            </div>
            <div>
                <el-card class="box-card" v-for="message in messages" :key="message.msgId">
                    <p>
                        新订单待确认
                        <el-badge value="new" v-if="message.messageStatus === 0" class="item"></el-badge>
                    </p>
                    <p>
                        <small style="color:#999;">发送人: {{message.sendUserName}} &nbsp;&nbsp;&nbsp; 发送时间:
                            {{message.createTime}}
                        </small>
                    </p>
                    <small>{{message.messageValue}}</small>
                    <el-button type="text" @click="goOrder(message.messageValue, message.msgId)">查看订单</el-button>
                </el-card>
            </div>
        </el-card>
    </div>
</template>

<script>
    import http from '../../util/HttpUtil'
    export default {
        name: "MessageList",
        data() {
            return {
                messages: [],
                sendForm: {
                    page: 1,
                    pageSize: 10,
                }
            }
        },
        mounted() {
            this.loadData();
        },
        methods: {
            loadData() {
                const that = this;
                http.post(this, 'message', 'getMessageList', this.sendForm, function (resp) {
                    const dt = JSON.parse(resp.body.data);
                    console.log(dt)
                    that.$set(that.$data, 'messages', dt.arr)
                })
            },
            goOrder(orderId, msgId) {
                orderId = orderId.substring(orderId.indexOf('订单号:') + 4, (orderId.indexOf(',请及时处理!')))
                const that = this;
                http.post(this, 'message', 'readMessage', {msgId: msgId}, function (resp) {
                    that.$router.push({
                        path: '/order/list',
                        query: {
                            orderId: orderId
                        }
                    })

                })

            },
        }

    }
</script>

<style scoped>
    .el-card {
        margin-bottom: 20px;
    }
</style>