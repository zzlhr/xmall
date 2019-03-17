<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>权限组管理</span>
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
                <el-form-item label="权限组名称">
                    <el-input size="mini" v-model="param.agName" placeholder="可模糊关键字"></el-input>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select size="mini" v-model="param.agStatus" value="启用" placeholder="请选择状态">
                        <!-- <el-option label="全部" value="null"></el-option> -->
                        <el-option label="启用" :value="0"></el-option>
                        <el-option label="禁用" :value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" type="primary" plain @click="onSubmit" icon="el-icon-search">查询</el-button>
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
                    prop="agid"
                    label="#"
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="agName"
                    label="权限组名称"
                    min-width="150">
            </el-table-column>
            <el-table-column
                    prop="agStatus"
                    label="状态"
                    :formatter="formatterStatus"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="enterpriseName"
                    label="所属企业"
                    min-width="150">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    width="200">
            </el-table-column>

            <el-table-column
                    prop="updateTime"
                    label="更新时间"
                    width="200">
            </el-table-column>

            <el-table-column
                    label="操作"
                    width="250">
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" plain @click="handleClick(scope.row)">查看</el-button>
                    <el-button type="success" size="mini" plain @click="jumpEdit(scope.row)">编辑</el-button>
                    <el-button type="danger" size="mini" plain @click="editAuth(scope.row)">编辑权限</el-button>
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
        <!-- 添加/编辑 权限组弹窗 -->
        <el-dialog :title="Title" :visible.sync="editDialogVisible">
            <el-form :model="form" label-width="80px">
                <el-form-item label="权限组">
                    <el-input size="mini" v-model="form.agName" auto-complete="off" placeholder="权限组名称"></el-input>
                </el-form-item>
                <el-form-item label="所属企业" v-show="isShow">
                    <el-select
                            v-model="value10"
                            filterable
                            allow-create
                            default-first-option
                            placeholder="请选择企业" :disabled="disable"
                            @change="changeSelect(value10)"
                            size="mini">
                        <el-option
                                v-for="item in options5"
                                :key="item.eid"
                                :label="item.epShortName"
                                :value="item.eid">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio-group v-model="form.agStatus">
                        <el-radio :label="0">启用</el-radio>
                        <el-radio :label="1">停用</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click="editDialogVisible = false">取 消</el-button>
                <el-button size="mini" type="primary" @click="agree">确 定</el-button>
            </div>
        </el-dialog>
        <!-- 查看权限组弹窗 -->
        <el-dialog title="查看权限组" :visible.sync="lookDialogVisible">
            <div class="spterp_info_item">
                <div class="spterp_info_label">权限组名称:&nbsp;&nbsp;</div>
                <span class="spterp_info_value">{{dataObject.agName}}</span>
            </div>
            <div class="spterp_info_item">
                <div class="spterp_info_label">权限组状态:&nbsp;&nbsp;</div>
                <span class="spterp_info_value">{{agStatusValue}}</span>
            </div>
            <div class="spterp_info_item">
                <div class="spterp_info_label">所属企业:&nbsp;&nbsp;</div>
                <span class="spterp_info_value">{{dataObject.enterpriseName}}</span>
            </div>
            <div class="spterp_info_item">
                <div class="spterp_info_label">创建时间:&nbsp;&nbsp;</div>
                <span class="spterp_info_value">{{dataObject.createTime}}</span>
            </div>
            <div class="spterp_info_item">
                <div class="spterp_info_label">更新时间:&nbsp;&nbsp;</div>
                <span class="spterp_info_value">{{dataObject.updateTime}}</span>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click="lookDialogVisible = false">取 消</el-button>
                <el-button size="mini" type="primary" @click="lookAgree">确 定</el-button>
            </div>
        </el-dialog>

    </el-card>
</template>

<script>
    import httpUtil from '../../util/HttpUtil.js';

    export default {
        name: "AuthGroup",
        data() {
            return {
                disable: false,
                editDialogVisible: false,
                lookDialogVisible: false,
                isSelectItem: 'false',
                selectItem: [],
                eqData: [{epName: '', eid: 0}],
                enterpriseNameOne: '',
                Title: '',
                agStatusValue: '',
                options5: [],
                value10: '',
                error: {
                    msg: '暂无消息',
                    type: 'info',
                    show: 'true'
                },
                user: this.$store.getters.user,
                form: {
                    agName: '',
                    agStatus: 0,
                    enterprise: 1,
                    project: 0,
                    updateUser: this.$store.getters.user.uid,
                    enterpriseName: '',
                    agStatusValue: '',
                },
                dataObject: {},
                param: {
                    page: 1,
                    pageSize: 10,
                    agName: '',
                    agStatus: 0,
                    enterprise: 1,
                    enterpriseId: this.$store.getters.user.enterprise,
                    agStatusValue: ''
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
                //企业下拉是否显示
                isShow: false,
                loading: true,
                tableData: []
            }
        },

        methods: {
            editAuth(row) {
                sessionStorage.setItem('editAuthValueObj', JSON.stringify(row))
                this.$router.push({path: '/auth/editAuthValue/'})
            },
            formatterStatus(row) {
                return row.agStatus === 0 ? '启用' : '禁用';
            },
            // 查询按钮
            onSubmit() {
                this.loadData();
            },
            // 点击查看按钮
            handleClick(row) {
                //sessionStorage.setItem("userListUID", row.uid);
                //this.$emit('eleven', '/user/info/');
                this.$set(this.$data, 'lookDialogVisible', true)
                this.$set(this.$data, 'dataObject', row)
                if (this.dataObject.agStatus === 0) {
                    this.agStatusValue = '启用'
                } else {
                    this.agStatusValue = '禁用'
                }
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
            // 弹窗 '确定' 按钮
            agree() {
                this.$data.editDialogVisible = false
                this.loadForm()
            },
            // 查看按钮弹窗的 “确定” 按钮
            lookAgree() {
                this.$data.lookDialogVisible = false
                this.$data.dataObject = {}
            },
            // 获取editAuthGroup，修改数据。
            loadForm() {
                const that = this;
                this.$set(this.$data, 'loading', true);
                httpUtil.post(this, 'auth', 'editAuthGroup', this.$data.form, function (resp) {
                    let data = JSON.parse(resp.body.data);
                    console.log(data)
                    that.$set(that.$data, 'loading', false);
                    that.loadData();
                    if (resp.body.code === 0) {
                        that.successMsg("编辑权限成功")
                    }
                })
            },
            // 获取getAuthGroup数据，显示数据。
            loadData() {
                const that = this;
                this.$set(this.$data, 'loading', true);
                console.log(this.$data.param)
                httpUtil.post(this, 'auth', "getAuthGroup", this.$data.param, function (resp) {
                    const data = resp.body.data;
                    that.$set(that.$data, "tableData", data.arr);
                    that.$set(that.$data, 'currentNumber', data.currentNumber);
                    that.$set(that.$data, 'currentPage', data.currentPage);
                    that.$set(that.$data, 'pageSize', data.pageSize);
                    that.$set(that.$data, 'totalCount', data.totalCount);
                    that.$set(that.$data, 'totalPages', data.totalPage);
                    that.$set(that.$data, 'loading', false);
                })
            },
            // 企业下拉,选择
            seachAll() {
                const that = this;
                httpUtil.get(that, 'enterprise', "getEnterpriseDropDown?keyword=", function (resp) {
                    // console.log(resp.body.data)
                    let epArr = resp.body.data;
                    //console.log(epArr)
                    if (resp.body.code === 0) {
                        // that.$set(that.$data, 'eqData', epArr);
                        that.options5 = epArr
                    }

                    console.log("企业下拉=========")
                    console.log(that.options5);
                })

            },
            // 点击编辑按钮方法
            jumpEdit(row) {
                console.log(row);
                // this.$emit('eleven', '/user/edit/' + row.userId);

                this.$set(this.$data, 'editDialogVisible', true)
                this.$set(this.$data, 'form', row)
                this.disable = true
                this.Title = "编辑权限组"
                this.eqData = []
                this.value10 = row.enterprise;

            },
            // 点击添加按钮的方法
            jumpAdd() {
                this.$set(this.$data, 'form', {
                    agName: '',
                    agStatus: 0,
                    enterprise: this.$store.getters.user.enterprise,
                    project: 0,
                    updateUser: this.$store.getters.user.uid,
                    enterpriseName: '',
                    agStatusValue: ''
                })
                this.$set(this.$data, 'editDialogVisible', true)
                this.disable = false
                this.Title = '添加权限组'
                this.value10 = this.$store.getters.user.enterprise


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
            },
            // 成功信息提示
            successMsg(value) {
                this.$message({
                    message: value,
                    type: 'success'
                });
            },
            //选中企业更改时触发
            changeSelect(value) {
                this.value10 = value;
                this.form.enterprise = value;
            }
        },
        mounted() {
            // load data
            this.loadData();
            this.$set(this.$data, 'totalPages', 1);
            this.seachAll();
        },
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

    .spterp_info_item {
        margin: 20px;
        line-height: 1rem;
        width: 100%;
        height: 20px;
    }

    .spterp_info_label {
        width: 100px !important;
        text-align: right;
        float: left;
    }
</style>
