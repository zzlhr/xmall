<template>
    <el-dialog
            :title="editType === 0 ? '添加分类' : '编辑分类'"
            :visible.sync="dialogShow"
            width="30%">
        <el-form>
            <el-form-item label="分类名称">
                <el-input v-model="classifyForm.clName"></el-input>
            </el-form-item>
            <el-form-item v-if="classifyForm.clGrade === 2" label="分类图片">
                <el-upload
                        ref="imgUpload"
                        class="upload-demo"
                        drag
                        name="img"
                        :multiple="false"
                        accept="jpg,png"
                        :limit="1"
                        list-type="picture"
                        :file-list="fileList"
                        :on-success="(response, file, fileList) => {classifyForm.picture=response.data.fileName}"
                        :on-error="() => {$message.error('上传文件失败！')}"
                        :action="classifyUploadPath">
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                    <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>
            </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button type="text" @click="closeDialog()">取 消</el-button>
            <el-button type="text" @click="sendClassifyForm(false)">确 定</el-button>
            <el-button type="text" v-if="editType === 0" @click="sendClassifyForm(true)">添加并继续</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import httpUtil from "~/util/HttpUtil";

    function noop() {

    }

    export default {
        name: "EditClassify",
        props: {
            editType: {
                // 编辑类型，0添加，1编辑
                type: Number,
                default: 0,
            },
            isShow: {
                type: Boolean,
                default: false,
            },
            classifyForm: {
                type: Object,
                default: {}
            },
            isShowChange: {
                type: Function,
                default: noop
            },
            editAfter: {
                type: Function,
                default: noop,
            }
        },
        data() {
            return {
                dialogShow: this.isShow,
                classifyUploadPath: httpUtil.baseurl('goods') + 'uploadClassifyPicture',
                classifyImgPath: httpUtil.baseurl('goods') + 'classifyImg/',
                fileList: []
            }
        },
        watch: {
            isShow(val) {
                console.log("isShow:" + val);
                this.dialogShow = val;
            },
            dialogShow(val) {
                this.isShowChange(val);
            },
            classifyForm(val){
                this.fileList = [];
                if (this.editType === 1 && this.classifyForm.clGrade === 2 && this.classifyForm.picture !== "") {
                    const imgUrl = this.classifyImgPath + this.classifyForm.picture;
                    this.fileList.push({name: this.classifyForm.picture, url: imgUrl})
                }
            }

        },
        mounted() {


        },
        methods: {
            closeDialog() {
                this.dialogShow = false;
            },
            sendClassifyForm(isNext) {
                console.log(isNext);
                const that = this;
                if (this.editType === 0) {
                    httpUtil.post(this, 'goods', 'addClassify', this.classifyForm, function (resp) {
                        console.log(resp);
                        if (resp.data.code === 0) {
                            that.$message.success("添加分类成功！");
                            that._editAfter(resp.data.data, isNext);
                            that.$refs.imgUpload.uploadFiles = []
                        } else {
                            that.$message.error("添加分类失败！");
                        }
                    })
                } else {
                    httpUtil.post(this, 'goods', 'updateClassify', this.classifyForm, function (resp) {
                        console.log(resp);
                        if (resp.data.code === 0) {
                            that.$message.success("编辑分类成功！");
                            that._editAfter(resp.data.data, isNext);
                            that.$refs.imgUpload.uploadFiles = []
                        } else {
                            that.$message.error("编辑分类失败！");
                        }
                    })
                }


            },
            _editAfter(data, isNext) {
                this.editAfter(data, isNext);
            },
        }
    }
</script>

<style scoped>

</style>