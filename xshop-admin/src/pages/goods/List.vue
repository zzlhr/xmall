<!--suppress ALL -->
<template >
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>商品列表</span>
            <template class="noselect_btn" v-if="isSelectItem === 'false'">
                <el-button style="float: right; padding: 6px 0; margin: 0 10px;" type="text" @click="loadData" icon="el-icon-refresh">刷新</el-button>
                <el-button style="float: right; padding: 6px 0" type="text" @click="jumpAdd" icon="el-icon-circle-plus">添加</el-button>
            </template>
            <template class="select_btn" v-else>
                <el-button style="float: right; padding: 6px 0" type="text" @click="examine" icon="el-icon-edit-outline">审核</el-button>
            </template>

            <el-form :inline="true" :model="param" class="demo-form-inline">
                <el-form-item label="商品标题">
                    <el-input size="mini" v-model="param.title" placeholder="可模糊关键字"></el-input>
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
                max-height="7000" >
            <el-table-column
                    type="selection"
                    width="45" >
            </el-table-column>
            <el-table-column
                    fixed
                    prop="title"
                    label="商品标题"
                    min-width="200">
            </el-table-column>
            <el-table-column
                    prop="originalPrice"
                    label="原价"
                    width="100" >
            </el-table-column>
            <el-table-column
                    prop="salePrice"
                    label="活动价"
                    width="130">
            </el-table-column>
            <el-table-column
                    prop="saleStatus"
                    label="活动否"
                    width="160">
            </el-table-column>
            <!--<el-table-column-->
                    <!--prop="cover"-->
                    <!--label="商品封面"-->
                    <!--width="200">-->
            <!--</el-table-column>-->
            <el-table-column
                    prop="stock"
                    label="库存"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="salesVolume"
                    label="月销量"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="deliveryPlace"
                    label="发货地"
                    width="70">
            </el-table-column>
            <el-table-column
                    prop="updateUserName"
                    label="更新人"
                    width="70">
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
                    <el-button type="success" size="mini" plain @click="jumpEdit(scope.row)">编辑</el-button>
                    <el-button type="warning" size="mini" plain @click="delectGoods(scope.row)">删除</el-button>
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
        name: "GoodsList",
        data() {
            return {
                information: false,
                isSelectItem: 'false',
                selectItem:[],
                error:{
                    msg:'暂无消息',
                    type:'info',
                    show:'true'
                },
                param:{
                    page: 1,
                    pageSize: 10,
                    title: '',
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
                enterpriseList:[], //企业列表


            }
        },
        methods: {
            delectGoods(row){
                const data = {"goodsId": row.goodsId}
                const that = this
                httpUtil.post(this, 'goods', 'deleteGoods', data, function (resp) {
                    const data = JSON.parse(resp.body.data);
                    that.loadData();
                })

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
                if(username===''&phoneNum===''&email===''&enterpriseId==='') {
                    this.information = true
                }else{
                    this.loadData();
                }

            },
            handleClick(row) {
                sessionStorage.setItem("userListUID", row.uid)
                this.$router.push({path:'/user/edit'})

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
                httpUtil.post(this, 'goods', "list", this.$data.param, function (resp) {
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
                console.log(row);
                // this.clickPhone = row.phone;
                // console.log(this.clickPhone)
                // sessionStorage.setItem("goodsIdEdit", row.goodsId);
                this.$router.push({name:'GoodsEdit', query:{goodsId: row.goodsId}})
                //console.log(row.ui
            },
            jumpAdd(row) {
                this.$router.push({path:'/goods/edit'})

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
                            message: `action: ${ action }`
                        });
                    }
                });
                //console.log('++++++++++++++++++')
                // console.log('审核内容')
                //console.log(this.$data.selectItem)
                //console.log('++++++++++++++++++')

            },
            querySearchAsync(query) {
                if ( query !== "" ){
                    const that = this;
                    httpUtil.get(this, 'enterprise', "getEnterpriseDropDown?keyword=" + query, (resp) => {
                        const data = JSON.parse(resp.body.data);
                        let list = [];
                        for (let i=0; i<data.length; i++){
                            list.push({
                                label:data[i].epShortName,
                                value:data[i].eid
                            });
                        }
                        that.$set(that.$data, 'enterpriseList', list);
                    });
                }
            },
            handleSelect(item) {
                console.log("选中项的值为",item);
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
    .el-card__header, .el-card__body{
        padding: 0.5rem;
    }
</style>
