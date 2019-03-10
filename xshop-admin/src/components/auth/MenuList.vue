<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>菜单列表</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="getMenuList">刷新</el-button>
        </div>
        <el-tree
                :data="menuList"
                show-checkbox
                node-key="id"
                default-expand-all
                :expand-on-click-node="false">
          <span class="custom-tree-node" slot-scope="{ node, menuList }">
            <span>{{ node.label }}</span>
            <span>
            <el-button
                    type="text"
                    size="mini"
                    @click="() => append(node)">
            添加
            </el-button>
              <el-button
                      type="text"
                      size="mini"
                      @click="() => update(node)">
                编辑
              </el-button>
              <!--<el-button-->
                      <!--type="text"-->
                      <!--size="mini"-->
                      <!--@click="() => remove(node)">-->
                <!--删除-->
              <!--</el-button>-->
            </span>
          </span>
        </el-tree>
        <el-dialog
                :title="dialog.title"
                :visible.sync="dialog.show"
                width="30%"
                :before-close="dialogClose">
            <!--<span>这是一段信息</span>-->
            <div>
                <el-form ref="menuForm" :model="menuForm" label-width="80px">
                    <el-form-item label="菜单名称">
                        <el-input v-model="menuForm.menuName"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单url">
                        <el-input v-model="menuForm.menuUrl"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单API">
                        <el-input v-model="menuForm.menuApi"></el-input>
                    </el-form-item>
                    <el-form-item label="菜单状态">
                        <el-radio-group v-model="menuForm.menuStatus">
                            <el-radio :label="0">启用</el-radio>
                            <el-radio :label="1">停用</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="sendMenuForm">确 定</el-button>
        </span>
        </el-dialog>
    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";

    export default {
        name: "MenuList",
        data() {
            return {
                menuList: [],
                dialog: {
                    title: '添加菜单',
                    show: false,

                },
                menuForm: {
                    mid: 0,
                    menuName: '',
                    menuUrl: '',
                    menuApi: '',
                    menuFmid: 0,
                    menuStatus: 0,
                    menuLevel: 0,
                }
            }
        },
        mounted() {
            this.getMenuList()
        },
        methods: {
            dialogClose() {
                this.$set(this.$data.menuForm, 'mid', 0);
                this.$set(this.$data.menuForm, 'menuName', '');
                this.$set(this.$data.menuForm, 'menuUrl', '');
                this.$set(this.$data.menuForm, 'menuApi', '');
                this.$set(this.$data.menuForm, 'menuFmid', 0);
                this.$set(this.$data.menuForm, 'menuStatus', 0);
                this.$set(this.$data.menuForm, 'menuLevel', 0);
                this.$set(this.$data.dialog, 'show', false);
            },
            getMenuList() {
                const that = this;
                const user = this.$store.getters.user;
                console.log(user);
                const enterprise = user.enterprise;
                console.log(enterprise);
                httpUtil.get(this, "auth", "getMenuList?enterprise="+enterprise, function (resp) {
                    const d1 = JSON.parse(resp.body.data);
                    console.log(d1);
                    that.$set(that.$data, 'menuList', d1)
                })
            },
            sendMenuForm(){
                // this.$data.menuForm.$remove('createTime');
                // this.$data.menuForm.$remove('updateTime')
                const that = this;
                httpUtil.post(this, 'auth', 'saveMenu', this.$data.menuForm, function (resp) {
                    console.log(resp);
                    const respBody = resp.body;
                    console.log(respBody)
                    if (respBody.code === 0){
                        that.dialogClose();
                        that.getMenuList();
                        that.successMsg("编辑菜单成功.");
                    }
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

<style>
    .custom-tree-node {
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 14px;
        padding-right: 8px;
    }
</style>
