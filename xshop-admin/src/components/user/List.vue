<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>用户列表</span>
            <template class="noselect_btn" v-if="isSelectItem === 'false'">
                <el-button style="float: right; padding: 6px 0; margin: 0 10px;" type="text" @click="loadData"
                           icon="el-icon-refresh">刷新
                </el-button>
                <el-button style="float: right; padding: 6px 0" type="text" @click="jumpAdd" icon="el-icon-circle-plus">
                    添加
                </el-button>
            </template>
            <template class="select_btn" v-else>
                <el-button style="float: right; padding: 6px 0" type="text" @click="examine"
                           icon="el-icon-edit-outline">审核
                </el-button>
            </template>

            <el-form :inline="true" :model="param" class="demo-form-inline">
                <el-form-item label="用户名">
                    <el-input size="mini" v-model="param.username" placeholder="可模糊关键字"></el-input>
                </el-form-item>
                <el-form-item label="手机号">
                    <el-input size="mini" v-model="param.phoneNum" placeholder="可模糊关键字"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input size="mini" v-model="param.email" placeholder="可模糊关键字"></el-input>
                </el-form-item>
                <el-form-item label="企业">
                    <el-select
                            v-model="param.enterpriseId"
                            filterable
                            remote
                            reserve-keyword
                            placeholder="请输入关键词"
                            :remote-method="querySearchAsync"
                            :loading="loading"
                            size="mini">
                        <el-option
                                v-for="item in enterpriseList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" type="primary" plain icon="el-icon-search" @click="onSubmit">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="tableData"
                v-loading="loading"
                border
                style="width: 100%;"
                stripe
                @select="tableItemSelect"
                @select-all="tableItemSelectAll"
                highlight-current-row
                max-height="7000">
            <el-table-column
                    type="selection"
                    width="45">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="uid"
                    label="#"
                    width="70">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="username"
                    label="用户名"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="手机号"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="enterpriseName"
                    label="所属企业"
                    width="160">
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="邮箱"
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="updateTime"
                    label="更新时间"
                    min-width="200">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    width="200">
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" plain @click="handleClick(scope.row)">查看</el-button>
                    <el-button type="success" size="mini" plain @click="jumpEdit(scope.row)">编辑</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="param.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalCount">
        </el-pagination>
        <el-dialog title="提示信息" :visible.sync="information">
            <span>请输入信息后再查询</span>
            <span slot="footer" class="dialog-footer">
            <el-button size="mini" type="primary" @click="information = false">确 定</el-button>
        </span>
        </el-dialog>
    </el-card>
</template>

<script>
    import httpUtil from '../../util/HttpUtil.js';

    export default {
        name: "UserList",
        data() {
            return {
                information: false,
                isSelectItem: 'false',
                selectItem: [],
                error: {
                    msg: '暂无消息',
                    type: 'info',
                    show: 'true'
                },
                param: {
                    page: 1,
                    pageSize: 10,
                    username: '',
                    phoneNum: '',
                    email: '',
                    enterpriseId: '',
                },
                formInline: {
                    user: '',
                    region: ''
                },
                currentPage: 1, //当前页数
                currentNumber: '',
                pageSize: 0,
                totalCount: 0,
                totalPages: 0,

                loading: true,
                //totalElements: 0,
                tableData: [],
                enterpriseList: [], //企业列表


            }
        },
        methods: {

            formatterStatus(row) {
                return row.agStatus === 0 ? '启用' : '禁用';
            },
            //
            onSubmit() {
                let username = this.$data.param.username;
                let phoneNum = this.$data.param.phoneNum;
                let email = this.$data.param.email;
                let enterpriseId = this.$data.param.enterpriseId;
                if (username === '' & phoneNum === '' & email === '' & enterpriseId === '') {
                    this.information = true
                } else {
                    this.loadData();
                }

            },
            handleClick(row) {
                sessionStorage.setItem("userListUID", row.uid)
                this.$router.push("/user/info/")
                // console.log(row.uid)
            },
            handleSizeChange(val) {
                this.$set(this.$data.param, 'pageSize', val);
                this.$set(this.$data.param, 'page', 1);
                this.loadData();
                //console.log(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                this.$set(this.$data.param, 'page', val);
                this.loadData();
                //console.log(`当前页: ${val}`);
            },
            alertClose(e) {
                //console.log(e);
            },

            loadData() {
                const that = this;
                this.$set(this.$data, 'loading', true);
                httpUtil.post(this, 'user', "userList", this.$data.param, function (resp) {
                    // let aa = JSON.parse(resp.data.data);
                    //  console.log(aa);
                    const data = resp.body.data;
                    console.log(data);
                    that.$set(that.$data, 'tableData', data.arr);
                    that.$set(that.$data, 'currentNumber', data.currentNumber);
                    that.$set(that.$data, 'currentPage', data.currentPage);
                    that.$set(that.$data, 'pageSize', data.pageSize);
                    that.$set(that.$data, 'totalCount', data.totalCount);
                    that.$set(that.$data, 'totalPages', data.totalPage);

                    //that.$set(that.$data, 'totalElements', data.totalElements);
                    console.log(that.tableData);
                    that.$set(that.$data, 'loading', false);
                })
            },
            jumpEdit(row) {
                // console.log(row);
                // this.clickPhone = row.phone;
                // console.log(this.clickPhone)
                sessionStorage.setItem("userListUID", row.uid);
                sessionStorage.setItem("userListPhone", row.phone);
                this.$router.push({path: '/user/edit/'})
                //console.log(row.ui
            },
            jumpAdd(row) {
                sessionStorage.setItem("userListUID", row.uid);
                this.$router.push({path: '/user/edit/'})
                // console.log(row.uid)

            },
            tableItemSelect(selection, row) {
                if (selection.length > 0) {
                    this.$set(this.$data, 'isSelectItem', 'true')
                } else {
                    this.$set(this.$data, 'isSelectItem', 'false')
                }
                this.$set(this.$data, 'selectItem', selection)
            },
            tableItemSelectAll(selection) {
                if (selection.length > 0) {
                    this.$set(this.$data, 'isSelectItem', 'true')
                } else {
                    this.$set(this.$data, 'isSelectItem', 'false')
                }
                this.$set(this.$data, 'selectItem', selection)
            },
            examine() {
                this.$alert(this.$data.selectItem, '审核内容', {
                    confirmButtonText: '确定',
                    callback: action => {
                        this.$message({
                            type: 'info',
                            message: `action: ${action}`
                        });
                    }
                });
                //console.log('++++++++++++++++++')
                // console.log('审核内容')
                //console.log(this.$data.selectItem)
                //console.log('++++++++++++++++++')

            },
            querySearchAsync(query) {
                if (query !== "") {
                    const that = this;
                    httpUtil.get(this, 'enterprise', "getEnterpriseDropDown?keyword=" + query, (resp) => {
                        const data = JSON.parse(resp.body.data);
                        let list = [];
                        for (let i = 0; i < data.length; i++) {
                            list.push({
                                label: data[i].epShortName,
                                value: data[i].eid
                            });
                        }
                        that.$set(that.$data, 'enterpriseList', list);
                    });
                }
            },
            handleSelect(item) {
                console.log("选中项的值为", item);
            }

        },
        mounted() {
            console.log(this.$data.param.username)
            // load data
            this.loadData();
            this.$set(this.$data, 'totalPages', 1)
        }
    }
</script>

<style>
    .el-pagination {
        margin-top: 1rem;
    }

    .clearfix > span {
        text-align: left !important;
    }

    .el-form-item {
        margin-bottom: 0;
        margin-top: 1rem;
    }

    .box-card {
        height: 100%;
    }

    .el-card__header, .el-card__body {
        padding: 0.5rem;
    }
</style>
