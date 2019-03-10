<!--suppress ALL -->
<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>订单列表&nbsp;&nbsp;&nbsp;<small style="color:#999;">橙色行表示没有发货</small></span>
            <template class="noselect_btn" v-if="isSelectItem === 'false'">
                <el-button style="float: right; padding: 6px 0; margin: 0 10px;" type="text" @click="loadData"
                           icon="el-icon-refresh">刷新
                </el-button>
            </template>

            <el-form :inline="true" :model="param" class="demo-form-inline">
                <el-form-item label="订单id">
                    <el-input size="mini" v-model="param.orderId" placeholder="请输入订单id"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button size="mini" type="primary" plain icon="el-icon-search" @click="select">查询</el-button>
                </el-form-item>
            </el-form>
        </div>
        <el-table
                :data="tableData"
                v-loading="loading"
                border
                @select="tableItemSelect"
                @select-all="tableItemSelectAll"
                highlight-current-row
                :row-class-name="tableRowClassName">
            <el-table-column
                    fixed
                    prop="orderId"
                    label="订单id"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    prop="orderMoney"
                    label="订单总价"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="orderAmount"
                    label="商品总价"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="despatchMoney"
                    label="总运费"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="offer"
                    label="优惠"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="username"
                    label="用户名"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="phone"
                    label="手机号"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="email"
                    label="邮箱"
                    min-width="120">
            </el-table-column>
            <el-table-column
                    prop="addr"
                    label="收货地址"
                    min-width="150">
            </el-table-column>
            <!--<el-table-column-->
            <!--prop="cover"-->
            <!--label="商品封面"-->
            <!--width="200">-->
            <!--</el-table-column>-->
            <el-table-column
                    prop="status"
                    label="状态"
                    min-width="70">
            </el-table-column>
            <el-table-column
                    prop="createTime"
                    label="创建时间"
                    min-width="100">
            </el-table-column>
            <el-table-column
                    fixed="right"
                    label="操作"
                    min-width="100">
                <template slot-scope="scope">
                    <el-button type="success" size="mini" plain @click="openDetails(scope.row)">详情</el-button>
                    <el-button v-show="scope.row.status === 0" type="info" size="mini" plain
                               @click="showConsignment(scope.row)">发货
                    </el-button>
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

        <el-dialog
                title="选择配送员"
                :visible.sync="configConsignmentShow"
                width="30%">
            <el-select v-model="deliveryId" placeholder="请选择配送员">
                <el-option
                        v-for="item in deliverys"
                        :key="item.did"
                        :label="item.deliveryName"
                        :value="item.did">
                </el-option>
            </el-select>
            <span slot="footer" class="dialog-footer">
                <el-button @click="configConsignmentShow = false">取 消</el-button>
                <el-button type="primary" @click="consignment">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog
                title="订单详情"
                :visible.sync="orderInfoShow"
                width="800px" style=" height:100%">
            <div>
                <el-button type="success" size="mini" @click="print(orderDetails)">打印</el-button>
                <div>
                    <label>订单id</label>
                    <span>{{orderDetails.orderId}}</span>
                </div>
                <div>
                    <label>用户名称</label>
                    <span>{{orderDetails.username}}</span>
                </div>
                <div>
                    <label>用户手机号</label>
                    <span>{{orderDetails.phone}}</span>
                </div>
                <div>
                    <label>用户邮箱</label>
                    <span>{{orderDetails.email}}</span>
                </div>
                <el-card class="box-card" v-for="(orderInfoVO, index) in orderDetails.orderInfoVOS" :key="index">
                    <div slot="header" class="clearfix">
                        <span>{{orderInfoVO.goods.title}} <small style="color: #ff0000;">x{{orderInfoVO.number}}</small></span>
                        <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
                    </div>
                    <div>
                        商品原价：{{orderInfoVO.goods.originalPrice}}
                    </div>
                    <div>
                        促销价：{{orderInfoVO.goods.salePrice}}
                    </div>
                    <div>
                        促销状态：{{orderInfoVO.goods.saleStatus === 0 ? '未促销' : '促销'}}
                    </div>
                    <div>
                        库存：{{orderInfoVO.goods.stock}}
                    </div>
                    <div>
                        运费：{{orderInfoVO.goods.despatchMoney}}
                    </div>
                </el-card>
            </div>
        </el-dialog>


    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";

    export default {
        name: "OrderList",
        data() {
            return {
                deliveryId: '',
                deliverys: [],
                configConsignmentShow: false,
                information: false,
                isSelectItem: "false",
                selectItem: [],
                error: {
                    msg: "暂无消息",
                    type: "info",
                    show: "true"
                },
                param: {
                    page: 1,
                    pageSize: 10,
                    orderId: ""
                },
                formInline: {
                    user: "",
                    region: ""
                },
                currentPage: 1, //当前页数
                currentNumber: "",
                pageSize: 10,
                totalCount: 0,
                totalPages: 0,

                loading: true,
                //totalElements: 0,
                tableData: [],
                enterpriseList: [], //企业列表
                orderInfoShow: false,
                orderDetails: {},
                consignmentRow: {},
            };
        },
        methods: {
            getDeliveryList() {
                const that = this;
                // httpUtil.post(this, 'delivery', 'list', {}, function (resp) {
                //     that.$set(that.$data, 'deliverys', JSON.parse(resp.body.data))
                // })
            },
            select() {
                this.$set(this.$data.param, "page", 1);
                this.loadData();
            },
            tableRowClassName({row, rowIndex}) {
                console.log(row.status);
                if (row.status === 0) {
                    return "warning-row";
                }
            },
            // 发货
            showConsignment(row) {
                this.$set(this.$data, 'consignmentRow', row)
                this.$set(this.$data, 'configConsignmentShow', true)
            },
            consignment() {
                const that = this;
                httpUtil.post(
                    this,
                    "order",
                    "consignment",
                    {
                        token: this.$store.getters.user.token,
                        orderId: this.$data.consignmentRow.orderId,
                        deliveryId: this.$data.deliveryId
                    },
                    function (resp) {
                        const data = resp.body;
                        if (data.code === 0) {
                            that.$message({
                                message: "发货成功！",
                                type: "success"
                            });
                            that.$set(that.$data, 'configConsignmentShow', false)
                            that.loadData();
                        }
                    }
                );
            },
            print(data) {
                console.log(data);
                window.open(
                    httpUtil.baseurl("order") + "print.html?orderId=" + data.orderId
                );
            },
            formatterStatus(row) {
                return row.agStatus === 0 ? "启用" : "禁用";
            },
            //
            onSubmit() {
                let username = this.$data.param.username;
                let phoneNum = this.$data.param.phoneNum;
                let email = this.$data.param.email;
                let enterpriseId = this.$data.param.enterpriseId;
                if (
                    (username === "") &
                    (phoneNum === "") &
                    (email === "") &
                    (enterpriseId === "")
                ) {
                    this.information = true;
                } else {
                    this.loadData();
                }
            },
            handleClick(row) {
                sessionStorage.setItem("userListUID", row.uid);
                this.router.push({path: "/user/info/"});
                // console.log(row.uid)
            },
            handleSizeChange(val) {
                this.$set(this.$data.param, "pageSize", val);
                this.$set(this.$data.param, "page", 1);
                this.loadData();
                //console.log(`每页 ${val} 条`);
            },
            handleCurrentChange(val) {
                this.$set(this.$data.param, "page", val);
                this.loadData();
                //console.log(`当前页: ${val}`);
            },
            alertClose(e) {
                //console.log(e);
            },

            loadData() {
                const that = this;
                this.$set(this.$data, "loading", true);
                httpUtil.post(this, "order", "list", this.$data.param, function (resp) {
                    console.log(resp.body);
                    let data = JSON.parse(resp.body.data);
                    console.log(data);
                    // const data = JSON.parse(resp.body.data);
                    // console.log(data);
                    that.$set(that.$data, "tableData", data.arr);
                    that.$set(that.$data, "currentNumber", data.currentNumber);
                    that.$set(that.$data, "currentPage", data.currentPage);
                    that.$set(that.$data, "pageSize", data.pageSize);
                    that.$set(that.$data, "totalCount", data.totalCount);
                    that.$set(that.$data, "totalPages", data.totalPage);
                    that.$set(that.$data, "totalElements", data.totalElements);
                    that.$set(that.$data, "loading", false);
                });
            },
            openDetails(row) {
                console.log(row);
                this.$set(this.$data, "orderDetails", row);
                this.$set(this.$data, "orderInfoShow", true);
            },
            jumpAdd(row) {
                this.$router.push({path: "/goods/edit/"});

                // console.log(row.uid)
            },
            tableItemSelect(selection, row) {
                if (selection.length > 0) {
                    this.$set(this.$data, "isSelectItem", "true");
                } else {
                    this.$set(this.$data, "isSelectItem", "false");
                }
                this.$set(this.$data, "selectItem", selection);
            },
            tableItemSelectAll(selection) {
                if (selection.length > 0) {
                    this.$set(this.$data, "isSelectItem", "true");
                } else {
                    this.$set(this.$data, "isSelectItem", "false");
                }
                this.$set(this.$data, "selectItem", selection);
            },
            examine() {
                this.$alert(this.$data.selectItem, "审核内容", {
                    confirmButtonText: "确定",
                    callback: action => {
                        this.$message({
                            type: "info",
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
                    httpUtil.get(
                        this,
                        "enterprise",
                        "getEnterpriseDropDown?keyword=" + query,
                        resp => {
                            const data = JSON.parse(resp.body.data);
                            let list = [];
                            for (let i = 0; i < data.length; i++) {
                                list.push({
                                    label: data[i].epShortName,
                                    value: data[i].eid
                                });
                            }
                            that.$set(that.$data, "enterpriseList", list);
                        }
                    );
                }
            },
            handleSelect(item) {
                console.log("选中项的值为", item);
            }
        },
        mounted() {
            console.log(this.$data.param.username);
            if (this.$route.query.orderId !== undefined) {
                this.$set(this.$data.param, "orderId", this.$route.query.orderId);
            }
            // load data
            this.loadData();
            this.$set(this.$data, "totalPages", 1);
            this.getDeliveryList();
        }
    };
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

    .el-card__header,
    .el-card__body {
        padding: 0.5rem;
    }

    .el-table .warning-row {
        background: oldlace;
    }
</style>
