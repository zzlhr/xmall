<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>编辑权限——{{authGroup.agName}}</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="getMenuList">刷新</el-button>
        </div>
        <el-tree
                :data="menuList"
                show-checkbox
                node-key="id"
                ref="tree"
                default-expand-all
                :default-checked-keys="defaultSelected"
                :expand-on-click-node="false">
          <span class="custom-tree-node" slot-scope="{ node, menuList }">
            <span>{{ node.label }}</span>
          </span>
        </el-tree>
        <el-button type="primary" size="mini" @click="saveAuthValue">保存</el-button>
    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";

    export default {
        name: "EditAuthValue",
        data() {
            return {
                menuList: [],
                dialog: {
                    title: '添加菜单',
                    show: false,

                },
                authGroup:{},
                // 默认选中节点的id
                defaultSelected:[],
            }
        },
        mounted() {
            const authGroup = JSON.parse(sessionStorage.getItem('editAuthValueObj'));
            console.log(authGroup);
            this.$set(this.$data, 'authGroup', authGroup);
            this.getMenuList();
            // this.getOpenAuth();
        },
        methods: {
            saveAuthValue(){
                // const nodes = this.$children[0].$children;
                // console.log(nodes)
                // nodes.forEach(function (node) {
                //     if (node.$vnode.componentOptions.tag === 'el-tree'){
                //         console.log('====')
                //         console.log(node.getCheckedNodes(true))
                //         console.log(node.getSelectedNodes(true))
                //     }
                // })
                    // .tree.getCheckedNodes();
                // console.log(checkedNodes);
                this.sendSaveAuthValue()
            },
            getMenuList() {
                const that = this;
                // this.getOpenAuth();
                httpUtil.get(this, "auth", "getMenuList", function (resp) {
                    const d1 = JSON.parse(resp.body.data);
                    console.log(d1);
                    const elTree = that.getTree();
                    that.$set(that.$data, 'menuList', d1)
                    that.getOpenAuth(function (defalutClicked) {
                        let dd = [];
                        defalutClicked.forEach(c => {
                            if (elTree.getNode(c).childNodes.length === 0){
                                dd.push(c)
                            }
                        })
                        console.log(dd);
                        elTree.setCheckedKeys(dd);
                    })

                })
            },
            getOpenAuth(callback){
                const that = this;
                httpUtil.post(this, "auth", "openAuth",{'groupId':this.$data.authGroup.agid}, function (resp) {
                    const d1 = JSON.parse(resp.body.data);
                    console.log(d1);
                    that.$set(that.$data, 'defaultSelected', d1)
                    callback(d1);
                })
            },
            getTree(){
                const nodes = this.$children[0].$children;
                for (let i = 0; i < nodes.length; i ++){
                    const node = nodes[i];
                    if (node.$vnode.componentOptions.tag === 'el-tree'){
                        return node;
                        // console.log(node.getCheckedNodes(true))
                        // console.log(node.getSelectedNodes(true))
                    }
                }
            },
            sendSaveAuthValue(){
                // this.$data.menuForm.$remove('createTime');
                // this.$data.menuForm.$remove('updateTime')
                const that = this;

                const elTree = this.getTree()
                const clickedNodes = elTree.getCheckedNodes();
                const halfClickedNodes = elTree.getHalfCheckedNodes();
                let authValues = '';
                for(let i=0;i<clickedNodes.length; i++){
                    console.log(clickedNodes[i].id)
                    authValues += clickedNodes[i].id+"|";
                }
                for (let i = 0; i < halfClickedNodes.length; i ++){
                    authValues += halfClickedNodes[i].id+"|";
                }

                authValues = authValues.substring(0, authValues.length - 1);
                httpUtil.post(this, 'auth', 'saveAuthValue', {
                    'authValues': authValues,
                    'groupId': this.$data.authGroup.agid,
                    'token': this.$store.getters.user.token
                }, function (resp) {
                    console.log(resp);
                    // const respBody = resp.body;
                    // console.log(respBody)
                    // if (respBody.code === 0){
                    //     that.dialogClose();
                    //     that.getMenuList();
                    //     that.successMsg("编辑菜单成功.");
                    // }
                })
            },
            append(node){
                console.log(node);
                this.$set(this.$data.dialog, 'show', true);
                this.$set(this.$data.dialog, 'title', "添加");
                this.$set(this.$data.menuForm, "menuFmid", node.data.id);
                this.$set(this.$data.menuForm, "menuLevel", node.data.level + 1)
                // this.$set(this.$data.menuForm, 'updateUser', )

            },
            update(node) {
                this.$set(this.$data.dialog, 'show', true);
                this.$set(this.$data.dialog, 'title', "编辑菜单");
                console.log(node);
                const that = this;
                httpUtil.post(this, 'auth', 'getMenu', {mid: node.data.id}, function (resp) {
                    const menuFormData = resp.body.data;
                    that.$set(that.$data, 'menuForm', JSON.parse(menuFormData));
                })

            },
            successMsg(value) {
                this.$message({
                    showClose: true,
                    message: value,
                    type: 'success'
                });
            },
            remove(node) {
                // const parent = node.parent;
                // const children = parent.data.children || parent.data;
                // const index = children.findIndex(d => d.id === data.id);
                // children.splice(index, 1);
            }
        }
    }
</script>

<style scoped>
    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }
</style>