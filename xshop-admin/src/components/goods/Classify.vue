<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>{{title}}</span>
        </div>
        <el-tree
                :load="loadNode"
                show-checkbox
                node-key="id"
                lazy
                :expand-on-click-node="false"
                :node-expand="(data, node, self) => getClassify(data.clId)">
            <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>{{ node.label }}</span>
                <span>
                    <el-button
                            type="text"
                            size="mini"
                            @click="() => edit(data)">
                        编辑
                    </el-button>
                    <el-button
                            type="text"
                            size="mini"
                            @click="() => append(data)">
                        添加
                    </el-button>
                    <el-button
                            type="text"
                            size="mini"
                            @click="() => remove(data)">
                        删除
                    </el-button>
                </span>
            </span>
        </el-tree>
        <el-dialog
                title="分类"
                :visible.sync="dialogVisible"
                width="30%">
            <el-form>
                <el-form-item label="分类名称">
                    <el-input v-model="classifyForm.clName"></el-input>
                </el-form-item>
                <el-upload
                        class="upload-demo"
                        drag,
                        name="img"
                        :multiple="false"
                        accept="jpg,png"
                        limit="1"
                        :on-success="(response, file, fileList) => {classifyForm.picture=response.data.data.fileName}"
                        :on-error="() => {this.$message.error('上传文件失败！')}"
                        :action="classifyUploadPath">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
        </el-dialog>
    </el-card>
</template>

<script>
    import httpUtil from "~/util/HttpUtil";

    export default {
        name: "Classify",
        data() {
            return {
                title: "分类列表",
                fClassifys: [],
                dialogVisible: false,
                classifyForm: {
                    clName: '',
                    clGrade: 0,
                    clFid: 0,
                    clSerial: 99,
                    eid: 0,
                    picture: '',
                },
                classifyUploadPath: httpUtil.baseurl('goods') + 'uploadClassifyPicture'
            }
        },
        mounted() {
            this.getClassify(0);
            this.classifyForm.eid = this.$store.state.user.enterprise;
        },
        methods: {
            append(data) {
                this.dialogVisible = true;
            },
            remove(data) {

            },
            edit(data) {
                this.dialogVisible = true;
            },
            loadNode(node, resolve) {
                if (node.level === 0) {
                    this.getClassify(0, resolve)
                } else {
                    this.getClassify(node.data.clId, resolve)
                }
            },

            getClassify(fid, resolve) {

                const that = this;
                httpUtil.post(this, "goods", "fClassify", {fid: fid}, function (resp) {
                    // if (fid === 0) {
                    //     // that.fClassifys = resp.data.data;
                    //     resolve(resp.data.data)
                    // } else {
                    //     for (let i = 0; i < that.fClassifys.length; i++) {
                    //         if (that.fClassifys[i].clId === fid) {
                    //             that.fClassifys[i].children = resp.data.data;
                    //         }
                    //     }
                    // }
                    resolve(resp.data.data)
                })
            }
        }
    }
</script>

<style scoped>

</style>