<template>
    <el-card class="box-card" shadow="never">
        <div slot="header" class="clearfix">
            <span>系统配置</span>
        </div>
        <el-form ref="form" :model="form" label-width="80px">
            <el-form-item label="联系方式">
                <el-input type="textarea" v-model="form.link"></el-input>
            </el-form-item>
            <el-form-item label="图一链接">
                <el-select v-model="value1" filterable placeholder="请选择">
                    <el-option
                            v-for="item in goodsOptions1"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="图二链接">
                <el-select v-model="value2" filterable placeholder="请选择">
                    <el-option
                            v-for="item in goodsOptions2"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="图三链接">
                <el-select v-model="value3" filterable placeholder="请选择">
                    <el-option
                            v-for="item in goodsOptions3"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="图四链接">
                <el-select v-model="value4" filterable placeholder="请选择">
                    <el-option
                            v-for="item in goodsOptions4"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="图五链接">
                <el-select v-model="value5" filterable placeholder="请选择">
                    <el-option
                            v-for="item in goodsOptions5"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="轮播图">
                <el-upload
                        class="upload-demo"
                        :action="picturesUploadUrl"
                        :on-preview="handlePreview"
                        :on-remove="handlePicturesImagesRemove"
                        :on-success="picturesSuccess"
                        :file-list="picturesImages"
                        :before-upload="beforePicturesUpload"
                        list-type="picture-card">
                    <img v-if="imageUrl" :src="imageUrl" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    <!--<el-button size="small" type="primary">点击上传</el-button>-->
                    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>
                <el-dialog :visible.sync="dialogVisible">
                    <img width="100%" :src="dialogImageUrl" alt="">
                </el-dialog>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">立即创建</el-button>
                    <el-button>取消</el-button>
                </el-form-item>
            </el-form-item>
        </el-form>
    </el-card>

</template>

<script>
    import httpUtil from '../../util/HttpUtil'

    export default {
        name: "SystemIndex",
        data() {
            return {
                form: {
                    link: '',
                },
                imageUrl: '',
                picturesUrl: httpUtil.home() + 'home_upload/',
                picturesImages: [],
                picturesUploadUrl: httpUtil.baseurl() + 'app/picturesUpload',
                dialogImageUrl: '',
                dialogVisible: false,
                goodsOptions1: [],
                goodsOptions2: [],
                goodsOptions3: [],
                goodsOptions4: [],
                goodsOptions5: [],

                value1: '',
                value2: '',
                value3: '',
                value4: '',
                value5: '',
            }
        },
        mounted() {
            const that = this;
            httpUtil.post(this, 'goods', 'pullDown', {}, function (data) {
                console.log()
                that.$set(that.$data, 'goodsOptions1', JSON.parse(data.body.data))
                that.$set(that.$data, 'goodsOptions2', JSON.parse(data.body.data))
                that.$set(that.$data, 'goodsOptions3', JSON.parse(data.body.data))
                that.$set(that.$data, 'goodsOptions4', JSON.parse(data.body.data))
                that.$set(that.$data, 'goodsOptions5', JSON.parse(data.body.data))

            })
            httpUtil.get(this, 'app', 'link', function (data) {
                that.$set(that.$data.form, 'link', data.body.data)

            })
        },
        methods: {
            onSubmit(){
                // httpUtil.post(this, 'app', 'save', '')
                const pictures = this.picturesImages;
                const values = [
                    this.$data.value1,
                    this.$data.value2,
                    this.$data.value3,
                    this.$data.value4,
                    this.$data.value5];
                const pictureSendArray = []
                for (let i = 0; i < pictures.length; i++){
                    const d = {}
                    d.picture = pictures[i].url;
                    d.url = values[i];
                    pictureSendArray.push(d)
                }
                const formData = this.$data.form;
                formData.picture = JSON.stringify(pictureSendArray);
                httpUtil.post(this, 'app', 'save', formData, function (data) {
                    console.log(data)
                })

            },
            handlePreview() {

            },
            handlePicturesImagesRemove(file, fileList) {
                const list = []
                for (let i = 0; i < fileList.length; i++) {
                    if (fileList[i].uid !== file.uid) {
                        list.push(fileList[i])
                    }
                }
                this.$set(this.$data, "picturesImages", list);
                this.$set(this.$data.goods, "picturesImages", list);
            },
            picturesSuccess(response, file, fileList) {

                const data = JSON.parse(response.data);
                console.log(data)
                this.picturesImages.push({
                    name: data.file,
                    url: this.$data.picturesUrl + data.file,
                });


            },
            beforePicturesUpload(file) {
                return this.imgUploadRule(file)
            },
            imgUploadRule(file) {
                const isImage = file.type === 'image/png' || file.type === 'image/jpeg';
                const isLt10M = file.size / 1024 / 1024 < 10;
                console.log(file.size / 1024 / 1024)
                if (!isImage) {
                    this.$message.error('上传头像图片只能是 JPG / PNG 格式!');
                }
                if (!isLt10M) {
                    this.$message.error('上传头像图片大小不能超过 10MB!');
                }
                return isImage && isLt10M;
            },
        }
    }
</script>

<style scoped>

</style>