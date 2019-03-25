<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>用户详情</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="jumpEdit">编辑</el-button>
        </div>
        <div class="spterp_info_item">
            <div class="spterp_info_label">用户名:&nbsp;&nbsp;</div>
            <span class="spterp_info_value">{{user.username}}</span>
        </div>
        <div class="spterp_info_item">
            <div class="spterp_info_label">手机号:&nbsp;&nbsp;</div>
            <span class="spterp_info_value">{{user.phone}}</span>
        </div>
        <div class="spterp_info_item">
            <label class="spterp_info_label">邮箱:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.email}}</span>
        </div>
        <!-- <div class="spterp_info_item">
            <label class="spterp_info_label">企业代号:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.enterprise}}</span>
        </div> -->
        <div class="spterp_info_item">
            <label class="spterp_info_label">所属企业:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.enterpriseName}}</span>
        </div>
        <!-- <div class="spterp_info_item">
            <label class="spterp_info_label">权限组:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.authGroup}}</span>
        </div> -->
        <div class="spterp_info_item">
            <label class="spterp_info_label">所属权限组:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.authGroupName}}</span>
        </div>
        <div class="spterp_info_item">
            <label class="spterp_info_label">状态:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.status === 0 ? '启用' : '禁用'}}</span>
        </div>
        
        <!-- <div class="spterp_info_item">
            <label class="spterp_info_label">创建时间:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.createTime}}</span>
        </div>
        
        <div class="spterp_info_item">
            <label class="spterp_info_label">更新时间:&nbsp;&nbsp;</label>
            <span class="spterp_info_value">{{user.updateTime}}</span>
        </div> -->
    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";

    export default {
        name: "getUser",
        data(){
            return{
                param:{
                    
                },
                user: {},
                loading:true
            }
        },
        mounted(){
            // console.log();
            this.loadData()
        },
        methods:{
            loadData(){
                const that = this;
                this.$set(this.$data, 'loading', true);
                const uid = sessionStorage.getItem("userListUID");
                console.log(uid)
                httpUtil.post(this, 'user', "getUser", {uid:uid}, function (resp) {
                     console.log(resp);
                     //const data = resp.body.data
                     const data = JSON.parse(resp.body.data);
                     console.log("getUser==========")
                     console.log(data);
                    //const data = resp.body.data;
                    that.$set(that.$data, "user", data);
                    that.$set(that.$data, 'loading', false);
                    
                })
            },
            jumpEdit(){
                this.$router.push({path:'/user/edit/' + this.$route.params.userId})
            },
            
        }
    }
</script>

<style scoped>
.spterp_info_item{
    margin: 20px;
    line-height: 1rem;
    width: 100%;
    height: 20px;
}
.spterp_info_label{
    width: 100px !important;
    text-align: right;
    float: left;
}
</style>