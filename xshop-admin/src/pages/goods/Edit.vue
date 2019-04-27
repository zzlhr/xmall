<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <span>{{title}}</span>
        </div>
        <el-form ref="goods" :model="goods" label-width="80px">
            <el-form-item label="商品标题">
                <el-input size="mini" v-model="goods.title" placeholder="请输入商品标题"></el-input>
            </el-form-item>
            <el-form-item label="商品描述">
                <el-input size="mini" v-model="goods.describe" style="width: 200px" type="textarea"
                          placeholder="请输入商品描述"></el-input>
            </el-form-item>
            <el-form-item label="原价">
                <el-input size="mini" v-model="goods.originalPrice" type="text" placeholder="请输入原价"></el-input>
            </el-form-item>
            <el-form-item v-show="goods.saleStatusShow" label="活动价">
                <el-input size="mini" v-model="goods.salePrice" placeholder="请输入活动价"></el-input>
                <el-button @click="saleMessageShow = true" size="mini">编写促销语</el-button>
            </el-form-item>
            <el-form-item v-show="saleMessageShow" label="促销语">
                <el-input size="mini" v-model="goods.saleMessage" placeholder="请输入促销语"></el-input>
            </el-form-item>
            <el-form-item label="开启活动">
                <el-switch size="mini" v-model="goods.saleStatusShow"></el-switch>
            </el-form-item>
            <el-form-item label="促销分类">
                <el-select size="mini" v-model="saleType" :placeholder="saleTypePlaceholder">
                    <el-option
                            v-for="item in promotionOptions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="上架">
                <el-switch size="mini" v-model="goods.statusShow"></el-switch>
            </el-form-item>
            <!--<el-form-item label="启用状态" >-->
            <!--<el-select size="mini" v-model="user.status" placeholder="请选择状态">-->
            <!--<el-option label="启用" :value="0"></el-option>-->
            <!--<el-option label="禁用" :value="1"></el-option>-->
            <!--</el-select>-->
            <!--</el-form-item>-->
            <el-form-item label="分类">
                <el-cascader
                        placeholder="请选择分类"
                        :options="classifyOptions"
                        size="mini"
                        change-on-select
                        @change="classifyChange"
                        @visible-change="classifyVisibleChange"
                ></el-cascader>
                <el-button @click="addClassifyInit" size="mini">管理分类</el-button>
            </el-form-item>
            <el-form-item label="库存">
                <el-input size="mini" v-model="goods.stock" placeholder="库存"></el-input>
            </el-form-item>
            <el-form-item label="发货地">
                <el-input size="mini" v-model="goods.deliveryPlace" placeholder="发货地"></el-input>
            </el-form-item>
            <el-form-item label="快递费">
                <el-input size="mini" type="number" v-model="goods.despatchMoney" placeholder="快递费"></el-input>
            </el-form-item>

            <el-form-item label="商品封面">
                <el-upload
                        class="upload-demo"
                        :action="coverUploadUrl"
                        :on-preview="handlePreview"
                        :on-remove="handleCoverImagesRemove"
                        :on-success="coverSuccess"
                        :file-list="coverImages"
                        :multiple="false"
                        :before-upload="beforeCoverUpload"
                        list-type="picture-card">
                    <img v-if="imageUrl" :src="imageUrl" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    <!--<el-button size="small" type="primary">点击上传</el-button>-->
                    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
                </el-upload>
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
            </el-form-item>
            <el-form-item label="商品详情" style="height:300px;">
                <div class="edit_container">
                    <quill-editor
                            v-model="goods.content"
                            ref="qe"
                            :options="editorOption"
                            @blur="onEditorBlur($event)" @focus="onEditorFocus($event)"
                            @change="onEditorChange($event)">
                    </quill-editor>
                </div>
            </el-form-item>
            <el-form-item size="small">
                <el-button type="primary" size="mini" @click="onSubmit(goods)">提交</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";
    import {quillEditor, Quill} from 'vue-quill-editor'
    import {container, ImageExtend, QuillWatch} from 'quill-image-extend-module'

    Quill.register('modules/ImageExtend', ImageExtend);

    export default {
        name: "GoodsEdit",
        components: {quillEditor},
        data() {
            return {
                saleMessageShow: false,
                saleType: '请选择',
                promotionOptions: [{
                    value: '0',
                    label: '请选择促销分类'
                }, {
                    value: '1',
                    label: '活动商品'
                }, {
                    value: '2',
                    label: '精选商品'
                }, {
                    value: '3',
                    label: '包邮商品'
                }, {
                    value: '4',
                    label: '新店必备'
                }],
                saleTypePlaceholder: '请选择促销分类',
                editStatus: {},
                delClassifyForm: {
                    clFid: 0,
                    clId: 0,
                },
                editClassifyShow: false,
                delClassifyShow: false,
                delClassifyFid: [],
                delClassifyId: [],
                addClassifyShow: false,
                fClassifys: [],
                classifyOptions: [],
                content: null,
                addClassify: {
                    clId: null,
                    clName: '',
                    clGrade: '0',
                    clFid: 0,
                    clDel: 0,
                },
                editorOption: {
                    modules: {
                        ImageExtend: {
                            loading: true,
                            name: 'file',
                            action: httpUtil.baseurl("goods") + 'picturesUpload',
                            response: (res) => {
                                console.log(res)
                                return httpUtil.baseurl('goods') + 'pictures_upload/' + res.data.fileName;
                            }
                        },
                        toolbar: {
                            container: container,
                            handlers: {
                                'image': function () {
                                    QuillWatch.emit(this.quill.id)
                                }
                            }
                        }
                    }
                },
                infoForm: {
                    a_title: '',
                    a_source: '',
                    a_content: '',
                },
                imageUrl: '',
                disable: false,
                coverUploadUrl: httpUtil.baseurl('goods') + 'coverUpload',
                picturesUploadUrl: httpUtil.baseurl('goods') + 'picturesUpload',
                coverUrl: httpUtil.baseurl('goods') + 'cover_upload/',
                picturesUrl: httpUtil.baseurl('goods') + 'pictures_upload/',
                loading: true,
                title: '',
                message: '',
                eqData: [],
                param: {},
                coverImages: [],
                picturesImages: [],
                userStatusSelect: {lable: '启用', value: 0},
                isShow: true,
                currPhone: '',  // 点击编辑后对应用户的手机号
                // enterpriseList:[],
                goods: {
                    title: "",
                    describe: "",
                    originalPrice: 0.00,
                    salePrice: 0.00,
                    saleStatus: 1,
                    saleStatusShow: false,
                    status: 0,
                    saleType: 0,
                    statusShow: true,
                    stock: 0,
                    deliveryPlace: "本地",
                    despatchMoney: 0.00,
                    coverImages: [],
                    cover: '',
                    pictures: '',
                    picturesImages: [],
                    content: '',
                    clId: 0,
                    clFid: 0,
                    saleMessage: '',

                }
            }
        },
        mounted() {

            this.titleSelect();
            // this.loadClassifyTree();
            const cacheGoods = sessionStorage.getItem("cacheGoods");
            // 加载缓存数据
            if (cacheGoods !== null && cacheGoods !== "" && cacheGoods !== undefined) {
                console.log('edit')
                this.$set(this.$data, "goods", JSON.parse(cacheGoods))
                this.$set(this.$data, "picturesImages", this.$data.goods.picturesImages)
                this.$set(this.$data, "coverImages", this.$data.goods.coverImages)
            } else {
                console.log('add')
                const goods = {
                    title: "",
                    describe: "",
                    originalPrice: 0.00,
                    salePrice: 0.00,
                    saleStatus: 0,
                    saleStatusShow: false,
                    status: 0,
                    statusShow: true,
                    stock: 0,
                    saleType: 0,
                    deliveryPlace: "本地",
                    despatchMoney: 0.00,
                    coverImages: "",
                    cover: '',
                    pictures: '',
                    picturesImages: [],
                    content: '',
                    clId: 0,
                    clFid: 0,
                };
                this.$set(this.$data, "goods", goods)
            }
        },
        computed: {
            editor() {
                return this.$refs.myQuillEditor.quill
            }
        },
        watch: {
            saleType(val) {
                if (val === "1" || val === "2" || val === "3" || val === "4" || val === "0") {
                    this.goods.saleType = val
                }
            }
        },
        methods: {
            classifyVisibleChange(e) {
                console.log(e);
                if (e) {
                    this.loadClassifyTree();
                }
            },
            addClassifyInit() {
                window.open('/#/goods/classify')
            },
            loadClassifyTree() {
                const that = this;
                httpUtil.post(this, "goods", "classifyTree", {}, function (resp) {
                    that.classifyOptions = resp.data.data;
                })

            },
            classifyChange(e) {
                console.log(e);
                this.$data.goods.clId = e[1];
            },
            beforePicturesUpload(file) {
                return this.imgUploadRule(file)
            },
            beforeCoverUpload(file) {
                //当封面有一张上传的图片时禁止上传
                if (this.coverImages.length >= 1) {
                    this.$message.error('封面只能上传一张！');
                    return false;
                }
                return this.imgUploadRule(file)
            },

            imgUploadRule(file) {
                const isImage = file.type === 'image/png' || file.type === 'image/jpeg';
                const isLt10M = file.size / 1024 / 1024 < 10;

                if (!isImage) {
                    this.$message.error('上传头像图片只能是 JPG / PNG 格式!');
                }
                if (!isLt10M) {
                    this.$message.error('上传头像图片大小不能超过 10MB!');
                }
                return isImage && isLt10M;
            },


            handlePreview() {

            },
            handleCoverImagesRemove(file, fileList) {
                const list = [];
                for (var i = 0; i < fileList.length; i++) {
                    if (fileList[i].uid !== file.uid) {
                        list.push(fileList[i])
                    }
                }
                this.$set(this.$data, "coverImages", list);
                this.$set(this.$data.goods, "coverImages", list);
            },
            handlePicturesImagesRemove(file, fileList) {
                const list = [];
                for (var i = 0; i < fileList.length; i++) {
                    if (fileList[i].uid !== file.uid) {
                        list.push(fileList[i])
                    }
                }
                this.$set(this.$data, "picturesImages", list);
                this.$set(this.$data.goods, "picturesImages", list);
            },
            onEditorBlur() {//失去焦点事件
            },
            onEditorFocus() {//获得焦点事件
            },
            onEditorChange() {//内容改变事件
            },
            coverSuccess(response, file, fileList) {
                const data = response.data;
                this.coverImages.push({
                    name: data.fileName,
                    url: httpUtil.baseurl('goods') + "cover/" + data.fileName,
                });

            },
            picturesSuccess(response, file, fileList) {
                const data = response.data;

                this.picturesImages.push({
                    name: data.fileName,
                    url: httpUtil.baseurl('goods') + "pictures_upload/" + data.fileName,
                });


            },
            // 根据点击添加用户或者编辑用户，标题的替换。
            titleSelect() {
                const goodsId = this.$route.query.goodsId;
                console.log("goodsId:" + goodsId)

                this.$set(this.$data.goods, 'goodsId', goodsId === 'undefined' ? null : goodsId);

                if (goodsId !== 'undefined' && goodsId !== '' && goodsId !== undefined) {
                    //this.$set(this.$data.user, 'uid', uid);
                    this.title = '编辑商品';
                    this.loadData();
                } else {
                    this.title = '添加商品';
                    this.goods.goodsId = null
                }

                // clear goodsIdEdit
                sessionStorage.setItem("goodsIdEdit", 'undefined');

            },
            // 企业关键词搜索，下拉
            loadAll() {
                const that = this;
                if (this.$data.user.epShortname !== undefined && this.$data.user.epShortname.length > 0) {
                    httpUtil.post(this, 'goods', "info", this.$data.goods.goodsId, function (resp) {
                        let epArr = JSON.parse(resp.body.data);
                        that.eqData = epArr;
                    })
                }
            },
            // 下拉选择某一项
            changeone(index) {
                this.user.enterprise = this.eqData[index].eid;
                this.user.epShortname = this.eqData[index].epShortName;
                this.user.eid = this.eqData[index].eid;
                this.eqData = [];
            },

            // 提交按钮，针对添加用户和编辑用户的判断。
            onSubmit() {
                if (this.$data.title === "编辑商品") {
                    console.log(this.picturesImages)
                    this.$set(this.$data.goods, "coverImages", this.coverImages === undefined ?
                        '' : this.coverImages);
                    this.$set(this.$data.goods, "picturesImages", this.picturesImages === undefined ?
                        '' : this.picturesImages);
                    this.$set(this.$data.goods, "saleStatus", this.$data.goods.saleStatusShow ? 1 : 0)
                    this.$set(this.$data.goods, "status", this.$data.goods.statusShow ? 0 : 1)
                    this.$set(this.$data.goods, "cover",
                        this.$data.goods.coverImages[0].url === undefined ?
                            '' : this.$data.goods.coverImages[0].url);
                    this.$set(this.$data.goods, "pictures", JSON.stringify(this.$data.goods.picturesImages === undefined ?
                        {} : this.$data.goods.picturesImages))
                    this.$set(this.$data.goods, "updateUser", this.$store.getters.user.uid === undefined ?
                        '' : this.$store.getters.user.uid)

                    // this.$set(this.$data.goods,"content", this.$refs.qe.editor.getContents())


                    const cacheGoods = JSON.stringify(this.goods);
                    console.log("cacheGoods:" + cacheGoods);
                    sessionStorage.setItem("cacheGoods", cacheGoods);
                    console.log(this.goods)

                    const that = this;
                    console.log("修改商品")
                    httpUtil.post(this, 'goods', 'updateGoods', this.$data.goods, function (resp) {
                        console.log(resp);
                        that.successMsg("修改成功！")
                        // 添加成功清楚缓存
                        sessionStorage.setItem("cacheGoods", "");
                    })
                } else {
                    // 缓存表单，如果出现提交失败还原表单
                    console.log(this.picturesImages)
                    this.$set(this.$data.goods, "coverImages", this.coverImages === undefined ?
                        '' : this.coverImages);
                    this.$set(this.$data.goods, "picturesImages", this.picturesImages === undefined ?
                        '' : this.picturesImages);
                    this.$set(this.$data.goods, "saleStatus", this.$data.goods.saleStatusShow ? 1 : 0)
                    this.$set(this.$data.goods, "status", this.$data.goods.statusShow ? 0 : 1)
                    this.$set(this.$data.goods, "cover",
                        this.$data.goods.coverImages[0].url === undefined ?
                            '' : this.$data.goods.coverImages[0].url);
                    this.$set(this.$data.goods, "pictures", JSON.stringify(this.$data.goods.picturesImages === undefined ?
                        {} : this.$data.goods.picturesImages))
                    this.$set(this.$data.goods, "updateUser", this.$store.getters.user.uid === undefined ?
                        '' : this.$store.getters.user.uid)

                    // this.$set(this.$data.goods,"content", this.$refs.qe.editor.getContents())


                    const cacheGoods = JSON.stringify(this.goods);
                    console.log("cacheGoods:" + cacheGoods);
                    sessionStorage.setItem("cacheGoods", cacheGoods);
                    console.log(this.goods)

                    const that = this;
                    console.log("添加商品")

                    httpUtil.post(this, 'goods', 'add', this.$data.goods, function (resp) {
                        console.log(resp);
                        that.successMsg("添加成功！")
                        // 添加成功清楚缓存
                        sessionStorage.setItem("cacheGoods", "");
                        const goods = {
                            title: "",
                            describe: "",
                            originalPrice: 0.00,
                            salePrice: 0.00,
                            saleStatus: 0,
                            saleStatusShow: false,
                            status: 0,
                            statusShow: true,
                            stock: 0,
                            deliveryPlace: "本地",
                            saleType: 0,
                            despatchMoney: 0.00,
                            coverImages: "",
                            cover: '',
                            pictures: '',
                            picturesImages: [],
                            content: '',
                            clId: 0,
                            clFid: 0,
                        };
                        this.goods = goods;
                    })
                }
            },
            loadData() {
                const that = this;
                this.$set(this.$data, 'loading', true);
                const goodsIdEdit = this.$route.query['goodsId'];
                console.log(goodsIdEdit)
                httpUtil.post(this, 'goods', "info", {goodsId: goodsIdEdit}, function (resp) {
                    const data = resp.body.data;
                    that.$set(that.$data, "goods", data)
                    const coverD = [{'url': data.cover, 'uid': new Date().getTime(), 'name': '封面'}];
                    const pictureD = JSON.parse(data.pictures);
                    that.$set(that.$data, "coverImages", coverD)
                    that.$set(that.goods, "coverImages", coverD);
                    that.$set(that.$data, "picturesImages", pictureD)
                    that.$set(that.goods, "picturesImages", pictureD);
                    that.$set(that.goods, "saleStatusShow", that.goods.saleStatus === 1)
                    that.$set(that.goods, "statusShow", that.goods.status === 0)
                    that.$set(that.goods, "updateUser", that.$store.getters.user.uid)
                    if (that.goods.saleType === 0) {
                        that.saleType = '请选择促销分类';
                    }
                    if (that.goods.saleType === 1) {
                        that.saleType = '活动商品';

                    }
                    if (that.goods.saleType === 2) {
                        that.saleType = '精选商品';

                    }
                    if (that.goods.saleType === 3) {
                        that.saleType = '包邮商品';

                    }
                    if (that.goods.saleType === 4) {
                        that.saleType = '新店必备';
                    }
                    console.log(that.classifyOptions)
                    console.log(that.goods);
                    // 重现分类
                    let timer = window.setInterval(function () {
                        console.log("interval----------")

                        if (that.classifyOptions !== []) {
                            let f = "";
                            let j1 = "";
                            for (let i = 0; i < that.classifyOptions.length; i++) {

                                if (that.goods.clFid === that.classifyOptions[i].value) {
                                    f = that.classifyOptions[i].label;
                                    for (let j = 0; j < that.classifyOptions[i].children.length; j++) {
                                        console.log(that.goods.clId);
                                        console.log(that.classifyOptions[i].children[j].value)
                                        if (that.goods.clId === that.classifyOptions[i].children[j].value) {
                                            j1 = that.classifyOptions[i].children[j].label
                                        }
                                    }
                                }
                            }
                            that.$set(that.$data, "classifyPlaceholder", f + "/" + j1);
                            clearInterval(timer);
                        }

                    }, 1000);

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
    .spterp_info_item {
        margin: 20px;
    }

    .el-input {
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

    .quill-editor {
        height: 250px;
    }

    .el-card__body {
        height: calc(100% - 39px);
        /*overflow: scroll;*/
    }
</style>