<template>

    <el-card class="box-card" shadow="never">

        <div slot="header" class="clearfix">
            <span>企业列表</span>
            <el-button style="float: right; padding: 6px 0" type="text" @click="loadData" icon="el-icon-refresh">刷新</el-button>
            <el-button style="float: right; padding: 6px 0" type="text" @click="addEnterprise" icon="el-icon-circle-plus">添加</el-button>
            <el-form :inline="true" :model="param" class="demo-form-inline">
                <el-form-item label="企业名">
                    <el-input size="mini" v-model="param.epName" placeholder="可模糊关键字"></el-input>
                </el-form-item>
                <el-form-item label="企业类型">
                    <el-select size="mini" v-model="param.epType" value="全部" placeholder="请选择类型">
                        <el-option label="全部" value="" ></el-option>
                        <el-option label="搅拌站" value="0"></el-option>
                        <el-option label="混凝土" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="企业状态">
                    <el-select size="mini" v-model="param.epStatus" value="全部" placeholder="请选择状态">
                        <el-option label="全部" value=""></el-option>
                        <el-option label="启用" value="0"></el-option>
                        <el-option label="禁用" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="tableData"
                v-loading="loading"
                border
                style="width: 100%"
                stripe
                max-height="500">
            <el-table-column
                    type="selection"
                    fixed
                    width="45" >
            </el-table-column>
            <el-table-column
                    prop="eid"
                    label="#"
                    fixed
                    width="50">
            </el-table-column>
            <el-table-column
                    prop="epName"
                    label="企业全称"
                    fixed
                    width="260">
            </el-table-column>
            <el-table-column
                    prop="epShortName"
                    label="企业简称"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="添加时间"
                    width="200">
            </el-table-column>
            <el-table-column
                    prop="epStatus"
                    label="企业状态"
                    width="100">
            </el-table-column>
            <el-table-column
                    prop="updateTime"
                    label="更新时间"
                    min-width="200"
            >
            </el-table-column>
            <el-table-column
                    label="操作"
                    fixed="right"
                    width="180"
                    >
                <template slot-scope="scope">
                    <el-button type="primary" size="mini" plain @click="handleClick(scope.row)">查看</el-button>
                    <el-button type="success" size="mini" plain @click="jumpEdit(scope.row)">编辑</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog :title="dtitle" :visible.sync="dialogVisible" width="40%" @close="diaCloseHandle">
            <el-form ref="form" :rules="rules" :disabled="form.forbidden" :model="form" label-width="80px">
                <el-form-item label="企业全称" prop="epName">
                    <el-input size="small" v-model="form.epName" placeholder="请输入企业全称"></el-input>
                </el-form-item>
                <el-form-item label="企业简称" prop="epShortName">
                    <el-input size="small" v-model="form.epShortName" placeholder="请输入企业简称"></el-input>
                </el-form-item>
                <el-form-item label="联系方式" prop="epLink">
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="form.epLink" placeholder="请输入联系方式"></el-input>
                </el-form-item>
                <el-form-item label="企业备注" prop="epRemark">
                    <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="form.epRemark" placeholder="请输入企业备注"></el-input>
                </el-form-item>
                <el-form-item label="企业类型" prop="epType" >
                    <el-select size="small" v-model="form.epType"  placeholder="请选择类型" >
                        <el-option label="搅拌站" :value="0"></el-option>
                        <el-option label="混凝土" :value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="企业状态" prop="epStatus">
                    <el-select size="small" v-model="form.epStatus"  placeholder="请选择状态" >
                        <el-option label="启用" :value="0"></el-option>
                        <el-option label="禁用" :value="1"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" v-show="form.btnShow">
                <el-button @click="resetForm('form')" v-show="form.restBtn">重置</el-button>
                <el-button type="primary" @click="submitForm('form')">确 定</el-button>
            </span>
        </el-dialog>
        <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="param.pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalCount">
        </el-pagination>
    </el-card>
</template>

<script>
    import httpUtil from '../../util/HttpUtil.js';
    export default {
        name: "UserList",
        data() {

            return {
                error:{
                    msg:'暂无消息',
                    type:'info',
                    show:'true'
                },
                param:{
                    page: 1,
                    pageSize: 10,
                    epName: '',
                    epType: '',
                    epStatus: '',
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
                totalElements: 0,
                tableData: [],
                //弹窗是否显示
                dialogVisible:false,
                //弹窗标题
                dtitle: '添加企业',
                //添加企业
                form: {
                    eid: null,
                    epName:'',
                    epShortName:'',
                    epLink:'',
                    epRemark:'',
                    epType: '',
                    epStatus: '',
                    //按钮组是否显示
                    btnShow: true,
                    //表单是否禁止编辑
                    forbidden:false,
                    restBtn:true
                },
                //添加企业自定义验证
                rules: {
                    epName: [
                        { required: true, message: '请输入企业全称', trigger: 'blur' },
                        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
                        { validator:this.Validate.checkSpace, trigger: 'blur' },
                        { validator: this.Validate.checkIllegal, trigger: 'blur' }
                    ],
                    epShortName: [
                        { required: true, message: '请输入企业简称', trigger: 'change' },
                        { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' },
                        { validator:this.Validate.checkSpace, trigger: 'blur' },
                        { validator: this.Validate.checkIllegal, trigger: 'blur' }
                    ],
                    epLink: [
                        { required: true, message: '请输入企业联系电话', trigger: 'change' },
                        { max: 255, message: '长度在 2 到 255 个字符', trigger: 'blur' }
                    ],
                    epRemark: [
                        {  max: 255, message: '长度不得大于255个字', trigger: 'blur' }
                    ],
                    epType: [
                        { required: true, message: '请选择企业类型', trigger: 'change' }
                    ],
                    epStatus: [
                        { required: true, message: '请选择企业状态', trigger: 'blur' }
                    ]
                }
            }
        },
        methods: {
            onSubmit() {
                this.loadData();
            },
            //查看企业
            handleClick(row) {
                this.$data.form = row;
                this.$data.form.btnShow = false;
                this.$data.form.forbidden = true;
                this.$data.dtitle = "查看企业";
                this.dialogVisible = true;
            },
            handleSizeChange(val) {
                this.$set(this.$data.param, 'pageSize', val);
                this.$set(this.$data.param, 'page', 1);
                this.loadData();
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
                httpUtil.post(this, 'enterprise', "selectEnterprise", this.$data.param, function (resp) {
                    console.log(resp)
                    let data = JSON.parse(resp.body.data);
                    //转换企业中的企业状态
                    for (let i = 0; i < data.arr.length; i++) {
                        data.arr[i].epStatus = data.arr[i].epStatus == 0 ? "启用" : data.arr[i].epStatus == 1 ? "禁用" : "未知";
                    }
                    that.$set(that.$data, "tableData", data.arr);
                    that.$set(that.$data, 'currentNumber', data.currentNumber);
                    that.$set(that.$data, 'currentPage', data.currentPage);
                    that.$set(that.$data, 'pageSize', data.pageSize);
                    that.$set(that.$data, 'totalCount', data.totalCount);
                    that.$set(that.$data, 'totalPages', data.totalPage);
                    // console.log(data);
                    that.$set(that.$data, 'loading', false);
                })
            },
            //编辑企业
            jumpEdit(row) {
                this.$data.form = row;
                this.$data.form.btnShow = true;
                this.$data.form.forbidden = false;
                this.$data.form.restBtn = false;
                this.$data.dtitle = "编辑企业";
                console.log(this.$data.form.epType);
                //处理数据
                this.$data.form.epStatus = this.$data.form.epStatus === '启用' ?  0 : 1;

                this.dialogVisible = true;
            },
            //添加/编辑企业api
            submitForm(formName) {
                let that = this;
                let title = that.$data.dtitle;
                that.$data.form.btnShow = true;
                that.$data.form.forbidden = false;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        httpUtil.post(this, 'enterprise', title === "添加企业" ? "addEnterprise" : "updateEnterprise", this.$data.form, function (resp) {
                            let code = resp.body.code;
                            if (code === 0) {
                                that.$notify({
                                    title: '成功',
                                    message: title === "添加企业" ? '企业添加成功！' : '企业信息修改成功',
                                    type: 'success'
                                });
                                //重置表单对象,清空表单内容
                                that.dialogVisible = false;
                                that.$refs[formName].resetFields();
                                that.loadData();
                            }
                        });
                    } else {
                        return false;
                    }
                });
            },
            //重置表单
            resetForm(formName) {
                this.$refs[formName].resetFields();
            },
            //添加企业
            addEnterprise() {
                this.$data.dtitle = '添加企业';
                this.$data.form.forbidden = false;
                this.$data.form.restBtn = true;
                this.$data.form.btnShow = true;
                this.initForm();
                this.$data.dialogVisible = true;
            },
            initForm() {
                this.$data.form.eid = null;
                this.$data.form.epName = '';
                this.$data.form.epShortName = '';
                this.$data.form.epLink = '';
                this.$data.form.epRemark = '';
                this.$data.form.epType = '';
                this.$data.form.epStatus = '';
            },
            diaCloseHandle(){
                this.loadData();
            }
        },
        mounted(){
            // load data
            this.loadData();
            this.$set(this.$data, 'totalPages' , 1)
        },

    }
</script>

<style>
    .el-pagination{
        margin-top: 1rem;
    }
    .clearfix>span{
        text-align: left !important;
    }
    .el-form-item{
        margin-bottom: 0;
        margin-top: 1rem;
    }
    .box-card{
        height: 100%;
    }
    .el-select,.el-select--small {
        display: block !important;
    }
</style>
