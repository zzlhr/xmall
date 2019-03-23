<!--suppress ALL -->
<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>供应商列表</span>
            <template class="noselect_btn" v-if="isSelectItem === 'false'">
                <el-button style="float: right; padding: 6px 0; margin: 0 10px;" type="text" @click="reloadData"
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

            <el-form :inline="true" :model="selectForm" class="demo-form-inline">
                <el-form-item label="供应商名称">
                    <el-input size="mini" v-model="selectForm.supplierName" placeholder="可模糊关键字"></el-input>
                </el-form-item>
                <el-form-item label="供应商状态">
                    <el-select size="mini" v-model="selectForm.status">
                        <el-option
                                v-for="item in statusData"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" type="primary" plain icon="el-icon-search" @click="loadData">查询</el-button>
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
                highlight-current-row>
            <el-table-column
                    fixed
                    prop="supplierId"
                    label="#"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    fixed
                    prop="supplierName"
                    label="供应商名称"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="supplierTel"
                    label="供应商联系方式"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="supplierAddress"
                    label="供应商地址"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="createUserName"
                    label="创建人"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="updateTime"
                    label="更新时间"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="updateUserName"
                    label="更新人"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="rowStatus"
                    label="删除否"
                    min-width="70"
                    :formatter="(row, column, cellValue, index) => statusColumnFormatter(row, column, cellValue, index)">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    min-width="100">
                <template slot-scope="scope">
                    <el-button type="success" size="mini" plain @click="openDetails(scope.row)">详情</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="selectForm.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalCount">
        </el-pagination>
        <el-dialog title="提示信息" :visible.sync="information">
            <span>请输入信息后再查询</span>
            <span slot="footer" class="dialog-footer">
            <el-button size="mini" type="primary" @click="information = false">确 定</el-button>
        </span>
        </el-dialog>

        <el-dialog
                title="添加供应商"
                :visible.sync="supplierFormShow"
                width="500px" style=" height:100%">
            <div>
                <el-form :model="addSupplierForm" class="demo-form-inline">
                    <el-form-item label="供应商名称">
                        <el-input v-model="addSupplierForm.supplierName" placeholder="供应商名称"></el-input>
                    </el-form-item>
                    <el-form-item label="供应商联系人">
                        <el-input v-model="addSupplierForm.supplierLinkMan" placeholder="供应商联系人"></el-input>
                    </el-form-item>
                    <el-form-item label="供应商联系电话">
                        <el-input v-model="addSupplierForm.supplierLinkTel" placeholder="供应商联系人"></el-input>
                    </el-form-item>
                    <el-form-item label="供应商状态">
                        <el-select v-model="addSupplierForm.supplierStatus">
                            <el-option
                                    v-for="item in statusData"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" plain icon="el-icon-search" @click="addSupplier">添加</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </el-dialog>
    </el-card>
</template>

<script>
    import httpUtil from '../../util/HttpUtil.js';

    export default {
        name: "SupplierList",
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
                selectForm: {
                    supplierName: '',
                    status: 0,
                    del: 0,
                    page: 1,
                    pageSize: 10,
                },
                addSupplierForm: {
                    supplierId: 0,
                    supplierName: '',
                    supplierAddress: '',
                    supplierDescribe: '',
                    supplierRemark: '',
                    rowStatus: 0,
                },
                statusData: [{
                    label: '启用',
                    value: 0
                }, {
                    label: '禁用',
                    value: 1
                }],
                formInline: {
                    user: '',
                    region: ''
                },
                currentPage: 1, //当前页数
                currentNumber: '',
                pageSize: 10,
                totalCount: 0,
                totalPages: 0,

                loading: true,
                //totalElements: 0,
                tableData: [],
                enterpriseList: [], //企业列表
                supplierFormShow: false,
                orderDetails: {},
            }
        },
        methods: {
            renderStatus(h, {column, $index}, index) {
                console.log(column)
            },
            statusColumnFormatter(row, column, cellValue, index) {
                if (cellValue === 0) {
                    return "未删除"
                } else {
                    return "删除"
                }
            },
            addSupplier() {
                const that = this
                httpUtil.post(this, 'supplier', 'add', this.$data.addSupplierForm, function (resp) {
                    let data = JSON.parse(resp.data.data);
                    if (resp.data.code === 0) {
                        that.$set(that.$data, 'supplierFormShow', false)
                    } else {
                        that.$message('添加供应商成功！');
                        that.reloadData();
                    }
                })
            },
            print(data) {
                console.log(data)
                window.open(httpUtil.baseurl('order') + "print.html?orderId=" + data.orderId)
            },
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
                this.$router.push({path: '/user/info/'})

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
            reloadData() {
                this.$set(this.$data.selectForm, 'page', 1)
                this.loadData();
            },
            loadData() {
                const that = this;
                that.loading = true
                httpUtil.post(this, 'supplier', "list", this.$data.selectForm, function (resp) {

                    const data = resp.data.data;
                    console.log(data)
                    that.tableData = data.arr
                    that.currentNumber = data.currentNumber
                    that.currentPage = data.currentPage
                    that.totalCount = data.totalCount
                    that.totalPages = data.totalPages
                    that.totalElements = data.totalElements
                    that.page++
                    that.loading = false

                })
            },
            openDetails(row) {
                console.log(row);
                this.$set(this.$data, 'orderDetails', row);
                this.$set(this.$data, 'orderInfoShow', true)
            },
            jumpAdd(row) {
                this.$set(this.$data, 'supplierFormShow', true)
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
            // load data
            this.loadData();
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
