<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>{{title}}</span>
            <!--<el-button style="float: right; padding: 3px 0" type="text">编辑</el-button>-->
        </div>

        <el-form ref="formData" :model="user" label-width="80px" >
            <el-form-item label="企业全称">
                <el-input size="small" v-model="enterprise.epName" placeholder="请输入企业全称"></el-input>
            </el-form-item>
            <el-form-item label="企业简称">
                <el-input size="small" v-model="enterprise.epShortName" placeholder="请输出企业简称"></el-input>
            </el-form-item>
            <el-form-item label="企业联系方式">
                <el-input size="small" v-model="enterprise.epLink" placeholder="请输出企业联系方式"></el-input>
            </el-form-item>
            <el-form-item label="企业备注">
                <el-input size="small" v-model="enterprise.epRemark" placeholder="请输出企业方式"></el-input>
            </el-form-item>
            <el-form-item label="企业类型">
                <el-select size="small" v-model="user.status === 0 ? '启用' : '禁用'" value="混凝土" placeholder="请选择类型">
                    <el-option label="混凝土" value="0"></el-option>
                    <el-option label="砂浆" value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="企业状态">
                <el-select size="small" v-model="user.status === 0 ? '启用' : '禁用'" value="启用" placeholder="请选择状态">
                    <el-option label="启用" value="0"></el-option>
                    <el-option label="禁用" value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item size="small">
                <el-button type="primary" @click="onSubmit">提交</el-button>
            </el-form-item>
        </el-form>

    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";

    export default {
        name: "EnterpriseEdit",
        data(){
            return{
                loading:true,
                title: '',
                message: '',
                eqData: [{epName:'',epName:0}],

                selectData: {
                    epName: '',
                    epEid:0
                },
                param:{

                },
                userStatusSelect:{lable: '启用', value: 0},
                enterprise: {
                    eid: 0,
                    epName:'',
                    epShortName:'',
                    epLink:'',
                    epRemark:'',
                    epType: {
                        lable: '混凝土',
                        value: '混凝土'
                    },
                    epStatus: {
                        lable: '启用',
                        value: '启用'
                    },
                },
            }
        },
        mounted(){
            //this.loadData();
            const uid = sessionStorage.getItem("userListUID");
            //console.log(uid)

            this.$set(this.$data.user, 'uid', uid);
            console.log("======================");
            console.log(uid);
            if (uid !== 'undefined') {
                //this.$set(this.$data.user, 'uid', uid);
                this.$set(this.$data,'title','编辑用户')
                this.loadData();
            }else{
                this.$set(this.$data,'title','添加用户')
                this.$set(this.$data.user,'uid',null)
            }



        },
        methods:{
            loadAll() {
                //console.log('loadAll');
                const that = this;
                //console.log(this.$data.user.epShortname);
                if(this.$data.user.epShortname !== undefined && this.$data.user.epShortname.length > 0){
                    httpUtil.get(this, 'enterprise', "getEnterpriseDropDown?keyword="+this.$data.user.epShortname, function(resp) {
                        const epArr= JSON.parse(resp.body.data);
                        // console.log(epArr);
                        //console.log(epArr.epShortName);
                        that.$set(that.$data, 'eqData', epArr);
                        console.log(that.eqData);

                        //this.$set(this.$data, 'message',resp);
                    })
                }

            },
            changeone(index) {
                // this.$data.user.enterprise = this.eqData[index].epShortName;
                this.$data.user.enterprise = this.eqData[index].eid;
                this.$data.user.epShortname = this.eqData[index].epShortName;
                this.$data.user.eid = this.eqData[index].eid;
                console.log(this.eqData)
                // this.$data.selectData.epName = this.eqData[index].epShortName;
                // this.$data.selectData.epEid = this.eqData[index].eid;
                this.eqData = [];
                // console.log(this.selectData)
                // console.log(this.user)

            },
            onSubmit() {
                //var data = this.$data.user;
                // console.log(data);
                const uid = sessionStorage.getItem("userListUID");
                console.log(uid);
                console.log(this.$data.user)
                const that = this;
                if (uid === 'undefined') {
                    httpUtil.post(this, 'user', 'addUser', this.$data.user, function (resp) {
                        // console.log(resp)
                        const d2 = JSON.parse(resp.body.data);
                        console.log(d2);
                        that.$set(that.$data,'user', d2);


                    })
                }else{
                    httpUtil.post(this, 'user', 'editUser', this.$data.user, function (resp) {
                        console.log(resp)
                        const d1 = JSON.parse(resp.body.data);
                        console.log(d1);
                        that.$set(that.$data,'user', d1);
                    })
                }

            },


            loadData(){
                const that = this;
                this.$set(this.$data, 'loading', true);
                const uid = sessionStorage.getItem("userListUID");
                //console.log(uid)

                httpUtil.post(this, 'user', "getUser", {uid:uid}, function (resp) {
                    //console.log(resp);
                    //const data = resp.body.data
                    const data = JSON.parse(resp.body.data);
                    console.log('================')
                    console.log(data);
                    //const data = resp.body.data;.

                    data.epShortname = data.enterpriseName
                    console.log(data)

                    console.log(data)
                    that.$set(that.$data, "user", data);
                    that.$set(that.$data, 'loading', false);

                })


            },

        }
    }
</script>

<style scoped>
    .spterp_info_item{
        margin: 20px;
    }
    .el-input{
        width: 200px;
    }
    #ul_eqShortname {
        display: block;
        list-style: none;
        padding: 0;
        margin-top: 0;

    }
    .li_eqShortname {
        /* line-height: 30px; */
        /* text-indent: 10px; */

        font-size: 16px;
        cursor: pointer;
        /* background-color: #eee; */
    }
</style>