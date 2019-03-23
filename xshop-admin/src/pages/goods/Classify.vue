<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span style="line-height: 38px;">{{title}}</span>
            <el-button style="float: right" type="text" @click="addTopClassify">添加顶级分类</el-button>
        </div>
        <el-tree ref="classifyTree"
                 :load="loadNode"
                 show-checkbox
                 node-key="clId"
                 lazy
                 :expand-on-click-node="false"
                 :node-expand="nodeExpand">
            <span class="custom-tree-node" slot-scope="{ node, data }">
                <span v-html="node.label"></span>
                <span>
                    <el-button
                            type="text"
                            size="mini"
                            @click="() => edit(data, node)">
                        编辑
                    </el-button>
                    <el-button
                            type="text"
                            size="mini"
                            @click="() => append(data, node)">
                        添加
                    </el-button>
                    <el-button
                            type="text"
                            size="mini"
                            @click="() => remove(data, node)">
                        删除
                    </el-button>
                </span>
            </span>
        </el-tree>
        <edit-classify :edit-type.sync="editType" :is-show.sync='dialogVisible' :classify-form.sync="classifyForm"
                       :isShowChange="(val) => this.dialogVisible = val" :edit-after="editAfter"></edit-classify>
    </el-card>
</template>

<script>
    import httpUtil from "~/util/HttpUtil";
    import EditClassify from "~/components/EditClassify";

    export default {
        name: "Classify",
        components: {EditClassify},
        data() {
            return {
                title: "分类列表",
                dialogVisible: false,
                classifyForm: {
                    clName: '',
                    clGrade: 0,
                    clFid: 0,
                    clSerial: 99,
                    picture: '',
                },
                editType: 0,
                editNode: null,
                resolve: null,
            }
        },
        mounted() {
            this.getClassify(0);
        },
        methods: {
            editAfter(data, isNext) {
                console.log(isNext)
                if (this.editType === 0) {
                    if (data.clFid === 0) {
                        this.$refs.classifyTree.root.insertChild({data: data}, data.clId, true);
                        return;
                    }
                    const fNode = this.$refs.classifyTree.getNode(data.clFid);
                    this.$refs.classifyTree.append(data, fNode);
                } else {
                    const treeNode = this.$refs.classifyTree.getNode(data.clId);
                    treeNode.data = data;
                    this.$refs.classifyTree.updateKeyChildren(data.clId, this.node)
                }
                if (!isNext) {
                    this.dialogVisible = false;
                }
                this.classifyForm = {
                    clName: '',
                    clGrade: 0,
                    // clFid: 0,
                    clSerial: 99,
                    picture: '',
                }
            },
            addTopClassify() {
                this.classifyForm = {
                    clName: '',
                    clGrade: 0,
                    clFid: 0,
                    clSerial: 99,
                    picture: '',
                };
                this.editType = 0;
                this.dialogVisible = true;
            },
            nodeExpand(data, node, self) {
                this.getClassify(data.clId)
            },
            append(data, node) {
                this.classifyForm = {
                    clName: '',
                    clGrade: 0,
                    clFid: data.clId,
                    clSerial: 99,
                    picture: '',
                };
                this.editType = 0;
                this.editNode = node;
                this.dialogVisible = true;
            },
            remove(data, node) {
                const that = this;
                httpUtil.post(this, 'goods', 'delClassify', {clId: data.clId}, function (resp) {
                    if (resp.data.code === 0) {
                        that.$message.success("删除分类成功！");
                        that.$refs.classifyTree.remove(data);
                    } else {
                        that.$message.success("删除分类失败！");
                    }
                })
            },
            edit(data, node) {
                this.editType = 1;
                this.editNode = node;
                this.classifyForm = data;
                this.dialogVisible = true;
            },
            loadNode(node, resolve) {
                this.resolve = resolve;
                if (node.level === 0) {
                    this.getClassify(0, resolve)
                } else {
                    this.getClassify(node.data.clId, resolve)
                }
            },

            getClassify(fid, resolve) {
                const that = this;
                httpUtil.post(this, "goods", "classifyByFid", {fid: fid}, function (resp) {
                    console.log(resp.data.data)
                    if (resolve !== undefined) {
                        resolve(resp.data.data)
                    }
                })
            }
        }
    }
</script>

<style scoped>

</style>