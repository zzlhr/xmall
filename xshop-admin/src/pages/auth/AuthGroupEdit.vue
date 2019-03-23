<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>{{title}}</span>
            <!--<el-button style="float: right; padding: 3px 0" type="text">编辑</el-button>-->
        </div>

        <el-form ref="formData" :model="user" label-width="80px" >
            <el-form-item label="用户名">
                <el-input size="small" v-model="user.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="密码">
                <el-input size="small" v-model="user.password" type="password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码">
                <el-input size="small" v-model="user.confirmPassword" type="password" placeholder="请输入确认密码"></el-input>
            </el-form-item>
            <el-form-item label="手机号">
                <el-input size="small" v-model="user.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="所属企业">
                <el-input size="small" v-model="user.epShortname" placeholder="请选择企业" v-on:input="loadAll">
                    <!-- <el-option v-for="enterprise in enterpriseList" :label="enterprise.name" :value="enterprise.id"></el-option> -->
                </el-input>
                <ul id="ul_eqShortname">
                    <li class="li_eqShortname" v-for='(item,index) in eqData'  @click="changeone(index)">
                        {{item.epShortName}}
                    </li>
                </ul>
            </el-form-item>
            <el-form-item label="状态">
                <el-select size="small" v-model="user.status" placeholder="请选择状态">
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
        name: "UserEdit",
        data(){
            return{
                loading:true,
                title: '',
                message: '',
                eqData: [],
                
                param:{
                 
                },
                userStatusSelect:{lable: '启用', value: 0},
                user: {
                    uid: '',
                    username:'',
                    password:'',
                    confirmPassword:'',
                    phone:'',
                    epShortname:'',
                    
                    userEnterprise:{
                        enterpriseId: ''
                    },
                    status: {
                        lable: '启用',
                        value: '启用'
                    },
                },
               // enterpriseList:[],
            }
        },
        mounted(){
           //this.loadData(); 
            const uid = sessionStorage.getItem("userListUID");
            //console.log(uid)
            this.$set(this.$data.user, 'uid', uid);
            console.log("======================");
            
            console.log(uid);
         
            if (uid === "undefined") {
                console.log("undefined")
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
                        //console.log(that.eqData);
                        
                        //this.$set(this.$data, 'message',resp);
                    })
                }
               
            },
            changeone(index) {
                    this.$data.user.epShortname = this.eqData[index].epShortName;  
                    this.eqData = [];
                },
            onSubmit() {
                //var data = this.$data.user;
               // console.log(data);
                const that = this;
                httpUtil.post(this, 'user', 'editUser', this.$data.user, function (resp) {
                    console.log(resp)
                    const d1 = JSON.parse(resp.body.data);
                    console.log(d1);
                    that.$set(that.$data.user, "username", d1.username);
                    
                    
                })
            },
            add() {
                
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
                     console.log(data);
                    //const data = resp.body.data;
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