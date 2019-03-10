<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>消息列表</span>
            <template class="noselect_btn">
                <el-button style="float: right; padding: 6px 0; margin: 0 10px;" type="text" @click="loadData"
                           icon="el-icon-refresh">刷新
                </el-button>
                <el-button style="float: right; padding: 6px 0; margin: 0 10px;" type="text" @click="addMessage"
                           icon="el-icon-circle-plus-outline">添加
                </el-button>
            </template>
        </div>
        <el-table
                :data="tableData"
                v-loading="loading"
                border
                highlight-current-row>
            <el-table-column
                    fixed
                    prop="msgId"
                    label="消息id"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="messageValue"
                    label="消息内容"
                    min-width="200">
            </el-table-column>
            <el-table-column
                    prop="inceptUser"
                    label="消息指向"
                    min-width="40">
            </el-table-column>
            <el-table-column
                    prop="sendUser"
                    label="创建人"
                    min-width="40">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="updateTime"
                    label="修改时间"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    min-width="50">
                <template slot-scope="scope">
                    <el-button type="info" size="mini" plain
                               @click="delMessage(scope.row.msgId)">删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalCount">
        </el-pagination>
        <el-dialog
                title="添加首页通知"
                :visible.sync="addMessageFormShow"
                width="500px" style=" height:100%">
            <div>
                <el-form :model="addMessageForm" class="demo-form-inline">
                    <el-form-item label="消息内容">
                        <el-input v-model="addMessageForm.messageValue" placeholder="消息内容"></el-input>
                    </el-form-item>
                    <el-form-item label="跳转地址">
                        <el-input v-model="addMessageForm.messageJumpUrl" placeholder="跳转地址"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" plain icon="el-icon-search" @click="sendMessage">添加</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
    </el-card>
</template>

<script>
    import http from '../../util/HttpUtil'

    export default {
        name: "Message",
        data() {
            return {
                loading: false,
                addMessageFormShow: false,
                currentPage: 0,
                pageSize: 10,
                totalCount: 0,
                tableData: [],
                addMessageForm: {
                    messageType: 3,
                    messageValue: '',
                    messageJumpUrl: '',
                    messageStatus: 0,
                    sendUser: 0,
                    inceptUser: 0,
                },
            }
        },
        mounted() {
            this.loadData();
        },
        methods: {
            addMessage() {
                const addMessageForm = {
                    messageType: 3,
                    messageValue: '',
                    messageJumpUrl: '',
                    messageStatus: 0,
                    sendUser: 0,
                    inceptUser: 0,
                }

                this.$set(this.$data, "addMessageForm", addMessageForm);
                this.$set(this.$data, "addMessageFormShow", true);
            },
            sendMessage() {
                // 组合消息内容体
                const that = this;

                this.$set(this.$data.addMessageForm, "messageValue",
                    this.$data.addMessageForm.messageValue + "|" + this.$data.addMessageForm.messageJumpUrl)
                http.post(this, "message", "sendMessage", this.$data.addMessageForm, function (resp) {
                    const data = resp.data
                    if (data.code === 0) {
                        that.$message("添加消息成功！");
                        that.$set(that.$data, 'addMessageFormShow', false)
                        that.loadData();
                    }
                    console.log(data);
                })
            },
            delMessage(msgId) {
                const that = this;
                http.post(this, "message", "delMessage", {msgId: msgId}, function (resp) {
                    const data = resp.data
                    console.log(data)
                    if (data.code === 0) {
                        that.$message("删除消息成功！");
                        that.loadData();
                    }
                })
            },
            loadData() {
                const that = this;
                http.post(this, 'message', 'getMessageList', {
                    token: this.$cookie.get("token"),
                    type: 3,
                    page: 1,
                    pageSize: this.$data.pageSize
                }, function (resp) {
                    const data = JSON.parse(resp.data.data)
                    console.log(data.arr)
                    that.$set(that.$data, 'currentPage', data.currentPage);
                    that.$set(that.$data, 'totalCount', data.totalCount);
                    that.$set(that.$data, 'tableData', data.arr);


                })
            }
        }
    }
</script>

<style scoped>

</style>