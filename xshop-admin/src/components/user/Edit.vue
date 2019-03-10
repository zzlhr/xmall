<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>{{title}}</span>
            <!--<el-button style="float: right; padding: 3px 0" type="text">编辑</el-button>-->
        </div>

        <el-form ref="user" :model="user" :rules="rules" label-width="80px" >
            <el-form-item label="用户账号" prop="username">
                <el-input size="mini" v-model="user.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="用户密码" prop="password" v-show="isShow">
                <el-input size="mini" v-model="user.password" type="password" placeholder="请输入密码"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword" v-show="isShow">
                <el-input size="mini" v-model="user.confirmPassword" type="password" placeholder="请输入确认密码"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phone">
                <el-input size="mini" v-model="user.phone" placeholder="请输入手机号"></el-input>
            </el-form-item>
            <el-form-item label="用户邮箱" prop="email">
                <el-input size="mini" v-model="user.email" placeholder="请输入用户邮箱"></el-input>
            </el-form-item>
            <el-form-item label="所属企业" prop="epShortname">
                <el-input size="mini" v-model="user.epShortname" placeholder="请选择企业" v-on:input="loadAll" :disabled="disable">
                    <!-- <el-option v-for="enterprise in enterpriseList" :label="enterprise.name" :value="enterprise.id"></el-option> -->
                </el-input>
                <ul id="ul_eqShortname">
                    <li class="li_eqShortname" v-for='(item,index) in eqData'  @click="changeone(index)">
                        {{item.epShortName}}
                    </li>
                </ul>
            </el-form-item>
            <el-form-item label="启用状态" prop="status">
                <el-select size="mini" v-model="user.status" placeholder="请选择状态">
                    <el-option label="启用" :value="0"></el-option>
                    <el-option label="禁用" :value="1"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item size="small">
                <el-button type="primary" size="mini" @click="onSubmit('user')">提交</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";

    export default {
        name: "UserEdit",
        data(){
            /* 验证密码 */
            const validatePass2 = (rule, value, callback) => {
                if (value !== this.user.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            // 验证手机号是否存在
            const checkPhone = (rule, value, callback) => {
                // console.log(this.currPhone,'>>>>>',value);
                // 点击用户自己的手机号注册不做已经注册处理
                if (this.currPhone === value) {
                    callback();
                } else {
                    httpUtil.post(this, 'user', 'phoneIsExist', {"phone": value}, function (resp) {
                        if (resp.body.code === 0) {
                            console.log("okok");
                            callback();
                        } else {
                            callback(new Error('手机号已存在!'));
                        }
                    })
                }
            };

            return{
                disable: false,
                loading:true,
                title: '',
                message: '',
                eqData: [],
                param:{},
                userStatusSelect:{lable: '启用', value: 0},
                isShow: true,
                currPhone:'',  // 点击编辑后对应用户的手机号
                user: {
                    uid: 0,
                    username:'',
                    password:'',
                    confirmPassword:'',
                    phone:'',
                    enterprise:'',
                    email: '',
                    eid: 0,
                    epShortname: '',
                    // userEnterprise:{
                    //     enterpriseId: ''
                    // },
                    status: {
                        lable: '启用',
                        value: '启用'
                    },
                },
               // enterpriseList:[],
               rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' },
                        { validator: this.Validate.checkSpace, trigger: 'blur' }
                    ],
                   password:[
                       { required: true, message: '请输入密码', trigger: 'blur' },
                       { validator: this.Validate.checkPass, trigger: 'blur' },
                       { min: 2, max: 128, message: '长度在 2 到 128 个字符', trigger: 'blur' }
                   ],
                   confirmPassword:[
                       { required: true, message: '请再次输入密码', trigger: 'blur' },
                       { validator: validatePass2, trigger: ['blur', 'change']  }
                   ],
                   phone:[
                       { required: true, message: '请输入手机号', trigger: 'blur' },
                       { max: 20, message: '长度最大为 20 个字符', trigger: 'blur' },
                       { validator: checkPhone, trigger: 'blur'},
                       { validator: this.Validate.checkIllegal , trigger: ['blur', 'change']  },
                    //    { validator: this.Validate.checkSpace , trigger: ['blur', 'change']  }
                   ],
                   email:[
                       { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                       { type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                   ],
                   enterprise:[
                       { required: true, message: '请选择一个企业', trigger: 'blur' }
                   ],
                   authGroup:[
                       { required: true, message: '请选择权限组', trigger: 'blur' }
                   ],
                   status:[
                       { required: true, message: '请选择启用状态', trigger: 'blur' }
                   ]
                }
            }
        },
        mounted(){
           this.titleSelect();
        },
        methods:{
            // 根据点击添加用户或者编辑用户，标题的替换。
            titleSelect() {
                const uid = sessionStorage.getItem("userListUID");
                const phone = sessionStorage.getItem("userListPhone");
                this.$set(this.$data.user, 'uid', uid);
                this.currPhone = phone;
                console.log(this.currPhone);
                if (uid !== 'undefined') {
                    this.$set(this.$data,'title','编辑用户');
                    this.$set(this.$data,'isShow',false);
                    this.loadData();
                }else{
                    this.$set(this.$data,'title','添加用户')
                    this.$set(this.$data.user,'uid',null)
                }
            },
            // 企业关键词搜索，下拉
            loadAll() {
                //console.log('loadAll');
                const that = this;      
                //console.log(this.$data.user.epShortname);
                if(this.$data.user.epShortname !== undefined && this.$data.user.epShortname.length > 0){
                    httpUtil.get(this, 'enterprise', "getEnterpriseDropDown?keyword="+this.$data.user.epShortname, function(resp) {
                        let epArr= JSON.parse(resp.body.data);
                        that.$set(that.$data, 'eqData', epArr);
                        console.log(that.eqData)
                        console.log(epArr)
                    })
                }
            },
            // 下拉选择某一项
            changeone(index) {
                    this.$data.user.enterprise = this.eqData[index].eid;
                    this.$data.user.epShortname = this.eqData[index].epShortName;
                    this.$data.user.eid = this.eqData[index].eid;
                    console.log(this.eqData)
                    this.eqData = [];   
                },
            // 提交按钮，针对添加用户和编辑用户的判断。
            onSubmit(formName) {
                if( this.$data.title === '编辑用户' ){
                    this.$data.user.password = 'default';
                    this.$data.user.confirmPassword = 'default';
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        const uid = sessionStorage.getItem("userListUID");
                        console.log(uid);
                        console.log(this.$data.user);
                        const that = this;
                        // 判断是否存在uid来确定是添加还是编辑
                        // 添加用户
                        if (uid === 'undefined') {
                            httpUtil.post(this, 'user', 'addUser', this.$data.user, function (resp) {
                                console.log(resp)
                                const d2 = JSON.parse(resp.body.data);
                                console.log(">>>>>===")
                                console.log(d2);
                                that.$set(that.$data,'user', d2);
                                that.$set(that.$data.user,'epShortname', d2.enterpriseName);
                                if(resp.body.code === 0) {
                                    that.successMsg("添加用户成功");
                                    that.$set(that.$data,'user', {});
                                }
                            })
                        }else{
                            // 编辑用户
                            httpUtil.post(this, 'user', 'editUser', this.$data.user, function (resp) {
                                console.log(resp)
                                const d1 = JSON.parse(resp.body.data);
                                console.log(d1);
                                that.$set(that.$data,'user', d1);
                                that.$set(that.$data.user,'epShortname', d1.enterpriseName);
                                if(resp.body.code === 0) {
                                    that.successMsg("编辑用户成功")
                                }
                            })
                        }
                    } else {
                        return false;
                    }
                });

            },
             loadData(){
                const that = this;
                this.$set(this.$data, 'loading', true);
                const uid = sessionStorage.getItem("userListUID");
                httpUtil.post(this, 'user', "getUser", {uid:uid}, function (resp) {
                    const data = JSON.parse(resp.body.data);
                    console.log('loadData================')
                    console.log(data);
                    data.epShortname = data.enterpriseName
                    console.log(data)
                    that.$set(that.$data, "user", data);
                    that.$set(that.$data, 'loading', false);
                    that.$set(that.$data, 'disable', true)
                }) 
            },
            successMsg(value) {
                this.$message({  
                    message: value,
                    type: 'success'
                });
            }
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
    font-size: 16px;
    cursor: pointer;
}
</style>