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
                        :placeholder="classifyPlaceholder"
                        :options="classifyOptions"
                        size="mini"
                        change-on-select
                        @change="classifyChange"
                ></el-cascader>
                <el-button @click="addClassifyInit" size="mini">添加分类</el-button>
                <el-button @click="editClassifyInit" size="mini">修改分类</el-button>
                <el-button @click="delClassifyInit" size="mini">删除分类</el-button>

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
        <el-dialog
                title="添加分类"
                :visible.sync="addClassifyShow"
                width="30%">
            <div>
                <el-form ref="addClassify" :model="addClassify" label-width="80px">
                    <el-form-item size="mini" label="分类名称">
                        <el-input v-model="addClassify.clName"></el-input>
                    </el-form-item>
                    <el-form-item size="mini" label="分类等级">
                        <el-radio-group v-model="addClassify.clGrade">
                            <el-radio  label="0">一级</el-radio>
                            <el-radio  label="1">二级</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="父级分类" v-show="addClassify.clGrade === '1'">
                        <el-select v-model="addClassify.clFid" placeholder="请选择父级分类" value="请选择父级分类">
                            <el-option :label="classify.clName" :value="classify.clId" :key="classify.clId" v-for="classify in fClassifys"></el-option>
                        </el-select>
                    </el-form-item>

                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addClassifyShow = false">取 消</el-button>
                <el-button type="primary" @click="addClassifyForm()">添加</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="删除分类"
                :visible.sync="delClassifyShow"
                width="30%">
            <div>
                <el-form ref="addClassify" :model="delClassifyForm" label-width="80px">
                    <el-form-item label="父级分类">
                        <el-select v-model="delClassifyForm.clFid" @change="delClassifyFidChange" placeholder="请选择父级分类" value="请选择父级分类">
                            <el-option :label="classify.clName" :value="classify.clId" :key="classify.clId" v-for="classify in classifyOptions"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="子分类">
                        <el-select v-model="delClassifyForm.clId" @change="delClassifyIdChange" placeholder="请选择子分类" value="请选择子分类">
                            <el-option :label="classify.clName" :value="classify.clId" :key="classify.clId" v-for="classify in delClassifyId"></el-option>
                        </el-select>
                    </el-form-item>
                </el-form>


            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addClassifyShow = false">取 消</el-button>
                <el-button type="danger" @click="onDelClassify()">删除</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="修改分类"
                :visible.sync="editClassifyShow"
                width="30%">
            <div>
                <el-tree
                        :data="classifyOptions"
                        show-checkbox
                        node-key="id"
                        default-expand-all
                        :expand-on-click-node="false">
                  <span class="custom-tree-node" slot-scope="{ node, data }">
                    <!--<el-input size="mini" type="text">{{ node.label }}</el-input>-->
                    <span>{{node.label}}</span>
                    <span>
                      <el-button
                              type="text"
                              size="mini"
                                @click="() => editClassClick(data)">
                        编辑
                      </el-button>
                    </span>
                  </span>
                </el-tree>


            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addClassifyShow = false">取 消</el-button>
                <el-button type="danger" @click="onDelClassify()">删除</el-button>
            </span>
        </el-dialog>
    </el-card>
</template>

<script>
    import httpUtil from "../../util/HttpUtil.js";
    import {quillEditor, Quill} from 'vue-quill-editor'
    import {container, ImageExtend, QuillWatch} from 'quill-image-extend-module'

    Quill.register('modules/ImageExtend', ImageExtend)

    export default {
        name: "GoodsEdit",
        components: {quillEditor},
        data() {

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
                editClassClick(row){
                    console.log(row)
                    this.$data.editStatus[row.clId]

                },
                editStatus:{},
                delClassifyForm: {
                    clFid:0,
                    clId:0,
                },
                editClassifyShow: false,
                delClassifyShow: false,
                delClassifyFid: [],
                delClassifyId:[],
                classifyPlaceholder: '请选择分类',
                addClassifyShow: false,
                fClassifys:[],
                classifyOptions: [],
                content: null,
                addClassify: {
                    clId: null,
                    clName: '',
                    clGrade: '0',
                    clFid:0,
                    clDel:0,
                },
                editorOption: {
                    modules: {
                        ImageExtend: {
                            loading: true,
                            name: 'file',
                            action: httpUtil.baseurl("goods") + 'picturesUpload',
                            response: (res) => {
                                console.log(res)
                                return httpUtil.host + 'pictures_upload/' + JSON.parse(res.data).file;
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
                coverUploadUrl: httpUtil.baseurl() + 'goods/coverUpload',
                picturesUploadUrl: httpUtil.baseurl() + 'goods/picturesUpload',
                coverUrl: httpUtil.host + 'cover_upload/',
                picturesUrl: httpUtil.host + 'pictures_upload/',
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
                    saleMessage:'',

                }
            }
        },
        mounted() {
            // this.titleSelect();

            this.titleSelect()
            this.fClassLoad()
            this.loadClassifyTree()
            const cacheGoods = sessionStorage.getItem("cacheGoods");
            console.log("cacheGoods:" + cacheGoods)
            console.log(cacheGoods)
            // 加载缓存数据
            if (cacheGoods !== null && cacheGoods !== "" && cacheGoods !== undefined) {
                console.log('edit')
                this.$set(this.$data, "goods", JSON.parse(cacheGoods))
                this.$set(this.$data, "picturesImages", this.$data.goods.picturesImages)
                this.$set(this.$data, "coverImages", this.$data.goods.coverImages)
            }else{
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
                }
                this.$set(this.$data, "goods", goods)
            }
        },
        computed: {
            editor() {
                return this.$refs.myQuillEditor.quill
            }
        },
        watch:{
            saleType(val){
                if(val === "1" || val === "2" || val === "3" || val === "4" || val === "0"){
                    this.$set(this.$data.goods, 'saleType', val)
                }
            }
        },
        methods: {
            editClassifyInit(){
                this.$set(this.$data, "editClassifyShow", true)
            },
            delClassifyIdChange(selected){
                this.$set(this.$data.delClassifyForm, "clId", selected)

            },
            onDelClassify(){
                console.log(this.$data.delClassifyForm)
                const that = this;
                httpUtil.post(this, 'goods', 'delClassify', this.$data.delClassifyForm, function (resp) {
                    const data = resp.body
                    console.log(data)
                    if (data.code === 0){
                        that.$message("删除成功!")
                        that.fClassLoad()
                        that.loadClassifyTree()
                    }
                })
            },
            delClassifyFidChange(selected){
                console.log(selected)
                this.$set(this.$data.delClassifyForm, "clFid", selected)
                for (var i=0; i < this.$data.classifyOptions.length; i++){
                    if (this.$data.classifyOptions[i].clId === selected){
                        console.log(this.$data.classifyOptions[i].clId)
                        console.log(this.$data.classifyOptions[i].children)
                        this.$set(this.$data, 'delClassifyId', this.$data.classifyOptions[i].children);
                    }
                }
            },
            delClassifyInit(){
                this.$set(this.$data, 'delClassifyShow', true)
                console.log(this.$data.classifyOptions)
            },
            addClassifyInit(){
                const classifyForm ={
                    clId: null,
                        clName: '',
                        clGrade: '0',
                        clFid:0,
                        clDel:0,
                }
                this.$set(this.$data, 'addClassify', classifyForm)
                this.$set(this.$data, 'addClassifyShow' ,true)
            },
            loadClassifyTree(){
                const that = this;
                httpUtil.get(this, 'goods', 'classifyTree', function (resp) {
                    console.log(resp);
                    const data = JSON.parse(resp.body.data)
                    that.$set(that.$data,"classifyOptions", data)
                    let editStatus = [];
                    that.$set(that.$data, "editStatus", [])
                    for (let i = 0 ; i < data.length; i++){
                        const d = {clId:data.clId, status: false};
                        editStatus.push(d)
                    }
                    that.$set(that.$data, "editStatus", editStatus)

                })

            },
            fClassLoad(){
                const that = this;
                httpUtil.get(this, 'goods', 'fClassify', function (resp) {
                    console.log(resp);
                    const data = JSON.parse(resp.body.data)
                    that.$set(that.$data,"fClassifys", data)
                })
            },
            addClassifyForm(){
                const that = this;
                httpUtil.post(this, 'goods', 'addClassify', this.addClassify, function (resp) {
                    that.successMsg("添加分类成功！")
                    that.fClassLoad()
                    that.loadClassifyTree()
                })
            },
            classifyChange(e) {
                console.log(e)
                this.$set(this.$data.goods, "clFid", e[0])
                this.$set(this.$data.goods, "clId", e[1])
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
                const list = []
                for (var i = 0; i < fileList.length; i++) {
                    if (fileList[i].uid !== file.uid) {
                        list.push(fileList[i])
                    }
                }
                this.$set(this.$data, "coverImages", list);
                this.$set(this.$data.goods, "coverImages", list);
            },
            handlePicturesImagesRemove(file, fileList) {
                const list = []
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
                console.log("=====================")
                console.log(response)
                console.log(file)
                console.log(fileList)
                console.log("=====================")
                const data = JSON.parse(response.data);
                this.coverImages.push({
                    name: data.file,
                    url: "https://xuanhuobang.com/cover_upload/" + data.file,
                });

            },
            picturesSuccess(response, file, fileList) {
                const data = JSON.parse(response.data);

                this.picturesImages.push({
                    name: data.file,
                    url: "https://xuanhuobang.com/pictures_upload/" + data.file,
                });


            },
            // 根据点击添加用户或者编辑用户，标题的替换。
            titleSelect() {
                const goodsId = sessionStorage.getItem("goodsIdEdit");
                console.log("goodsId:" + goodsId)

                this.$set(this.$data.goods, 'goodsId', goodsId === 'undefined' ? null : goodsId);

                if (goodsId !== 'undefined' && goodsId !== '' && goodsId !== undefined) {
                    //this.$set(this.$data.user, 'uid', uid);
                    this.$set(this.$data, 'title', '编辑商品')
                    this.loadData();
                } else {
                    this.$set(this.$data, 'title', '添加商品')
                    this.$set(this.$data.goods, 'goodsId', null)
                }

                // clear goodsIdEdit
                sessionStorage.setItem("goodsIdEdit", 'undefined');

            },
            // 企业关键词搜索，下拉
            loadAll() {
                //console.log('loadAll');
                const that = this;
                //console.log(this.$data.user.epShortname);
                if (this.$data.user.epShortname !== undefined && this.$data.user.epShortname.length > 0) {
                    httpUtil.post(this, 'goods', "info", this.$data.goods.goodsId, function (resp) {
                        let epArr = JSON.parse(resp.body.data);
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
                }else {
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
                        }
                        this.$set(this.$data, "goods", goods)
                    })
                }
            },
            loadData() {
                const that = this;
                this.$set(this.$data, 'loading', true);
                const goodsIdEdit = sessionStorage.getItem("goodsIdEdit");
                httpUtil.post(this, 'goods', "info", {goodsId: goodsIdEdit}, function (resp) {
                    const data = JSON.parse(resp.body.data);
                    that.$set(that.$data, "goods", data)
                    const coverD = [{'url': data.cover, 'uid': new Date().getTime(), 'name': '封面'}];
                    const pictureD = JSON.parse(data.pictures);
                    that.$set(that.$data, "coverImages", coverD)
                    that.$set(that.$data.goods, "coverImages", coverD);
                    that.$set(that.$data, "picturesImages", pictureD)
                    that.$set(that.$data.goods, "picturesImages", pictureD);
                    that.$set(that.$data.goods, "saleStatusShow", that.$data.goods.saleStatus === 1)
                    that.$set(that.$data.goods, "statusShow", that.$data.goods.status === 0)
                    that.$set(that.$data.goods, "updateUser", that.$store.getters.user.uid)
                    if(that.$data.goods.saleType === 0){
                        that.$set(that.$data, 'saleType', '请选择促销分类')
                    }
                    if(that.$data.goods.saleType === 1){
                        that.$set(that.$data, 'saleType', '活动商品')
                    }
                    if(that.$data.goods.saleType === 2){
                        that.$set(that.$data, 'saleType', '精选商品')
                    }
                    if(that.$data.goods.saleType === 3){
                        that.$set(that.$data, 'saleType', '包邮商品')
                    }
                    if(that.$data.goods.saleType === 4){
                        that.$set(that.$data, 'saleType', '新店必备')
                    }
                    console.log(that.$data.classifyOptions)
                    console.log(that.$data.goods);
                    // 重现分类
                    let timer = window.setInterval(function () {
                        console.log("interval----------")

                        if(that.$data.classifyOptions !== []){
                            let f = "";
                            let j1 = "";
                            for (let i = 0; i < that.$data.classifyOptions.length; i++){

                                if (that.$data.goods.clFid === that.$data.classifyOptions[i].value){
                                    f = that.$data.classifyOptions[i].label
                                    for (let j = 0; j < that.$data.classifyOptions[i].children.length; j++){
                                        console.log(that.$data.goods.clId)
                                        console.log(that.$data.classifyOptions[i].children[j].value)
                                        if (that.$data.goods.clId === that.$data.classifyOptions[i].children[j].value){
                                            j1 = that.$data.classifyOptions[i].children[j].label
                                        }
                                    }
                                }
                            }
                            that.$set(that.$data, "classifyPlaceholder", f + "/" + j1);
                            clearInterval(timer);
                        }

                    },1000);

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
    .quill-editor{
        height: 250px;
    }
    .el-card__body{
        height: calc(100% - 39px);
        /*overflow: scroll;*/
    }
</style>