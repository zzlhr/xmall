<template>
    <div>
        <x-header :left-options="{preventGoBack: true}" @on-click-back="goBack()">收货地址管理</x-header>
        <div>
            <group>
                <div class="address_list" v-for="(address, index) in addresses" :key="index">
                    <div class="address_item">
                        <div class="address_edit" @click="addressEdit(address.id)">
                            <font-awesome-icon icon="edit" fixed-width></font-awesome-icon>
                        </div>
                        <label>{{address.consignee}}
                            <small>{{address.linkTel}}</small>
                        </label>
                        <p class="address_describe">
                            <small>{{address.provinceName + address.cityName + address.countryName
                                + (address.townName || "") + address.addr}}
                            </small>
                        </p>
                    </div>
                </div>
            </group>
        </div>
        <div class="add_btn">
            <x-button type="primary" @click.native="goAddAddr">添加收货地址</x-button>
        </div>
        <toast v-transfer-dom v-model="showPositionValue" type="text" :time="800" is-show-mask :text="toastMsg"
               :position="position"></toast>
    </div>
</template>

<script>
    import http from '../util/HttpUtil'
    import {CellFormPreview} from 'vux'
    import {Cell} from 'vux'
    import {Group} from 'vux'
    import {XButton} from 'vux'
    import {XDialog} from 'vux'
    import {XInput, XSwitch, Selector, Icon} from 'vux'
    import AuthUtil from '../util/AuthUtil'

    export default {
        components: {
            CellFormPreview,
            Cell,
            Group,
            XButton,
            XDialog,
            XInput,
            XSwitch,
            Selector,
            Icon
        },
        name: "Address",
        data() {
            return {
                selectProvince: {},
                townOption: [],
                cityOption: [],
                countryOption: [],
                provinceOption: [],
                selectCity: {},
                selectTown: {},
                selectCountry: {},
                addresses: [],
                showToast1: false,
                showToast: false,
                toastMsg: '',
                showPositionValue: false,
                position: 'bottom',
                addForm: {
                    addr: '',
                    defaultStatus: false,
                    consignee: '',
                    linkTel: '',
                    province: '',
                    city: '',
                    country: '',
                    town: '',
                },
                upForm: {
                    id: 0,
                    addr: '',
                }
            }
        },
        mounted() {
            AuthUtil.pageAuth(this);
            const token = localStorage.getItem('token');
            if (token === undefined) {
                this.$router.push({path: 'my', params: {login: true}})
            }
            this.loadData();
        },
        methods: {
            goBack() {
                if (this.$route.params['from'] !== undefined) {
                    this.$router.push({name: this.$route.params['from']});
                }
                if (this.$route.query['from'] !== undefined) {
                    this.$router.push({name: this.$route.query['from']});
                }
                this.$router.push({name: 'My'});
            },
            goAddAddr() {
                // this.showToast = true
                this.$router.push({name: 'EditAddress'})

            },
            addressEdit(id) {
                this.$router.push({name: 'EditAddress', params: {id: id}})
                console.log('addd')
            },

            loadData() {
                const that = this;
                http.postForm(this, "address", "getAddress",
                    {token: localStorage.getItem("token")},
                    function (resp) {
                        const data = resp.data.data
                        that.$set(that.$data, 'addresses', data)
                    }
                )
            },
            setDefaultAddr(id) {
                const that = this;
                http.postForm(this, "address", "updateDefaultAddr",
                    {token: this.$store.state.user.token, addrId: id},
                    function (resp) {
                        console.log(resp)
                        if (resp.data.code === 0) {
                            that.$set(that.$data, 'position', 'bottom');
                            that.$set(that.$data, 'showPositionValue', true);
                            that.$set(that.$data, 'toastMsg', '修改成功');
                            that.loadData();
                        } else {
                            that.$set(that.$data, 'position', 'bottom');
                            that.$set(that.$data, 'showPositionValue', true);
                            that.$set(that.$data, 'toastMsg', '修改失败');
                        }
                    }
                )
            },
            delAddr(id) {
                const that = this;
                http.postForm(this, "address", "delAddr",
                    {token: this.$store.state.user.token, addrId: id},
                    function (resp) {
                        console.log(resp)
                        if (resp.data.code === 0) {
                            that.$set(that.$data, 'position', 'bottom');
                            that.$set(that.$data, 'showPositionValue', true);
                            that.$set(that.$data, 'toastMsg', '删除成功');
                            that.loadData();
                        } else {
                            that.$set(that.$data, 'position', 'bottom');
                            that.$set(that.$data, 'showPositionValue', true);
                            that.$set(that.$data, 'toastMsg', '删除失败');
                        }
                    }
                )
            },
            showUpdate(addr) {
                this.$set(this.$data.upForm, "id", addr.id);
                this.$set(this.$data.upForm, "addr", addr.addr);
                this.$set(this.$data, 'showToast1', true);

            },
            upAddr() {
                const form = {
                    addrId: this.$data.upForm.id,
                    address: this.$data.upForm.addr,
                };
                const that = this;
                const selectCity = this.$data.selectCity;
                const cityOption = this.$data.cityOption;
                const selectTown = this.$data.selectTown;
                const townOption = this.$data.townOption;
                for (let i = 0; i < cityOption.length; i++) {
                    console.log(cityOption[i].key + "==?" + selectCity);
                    if (cityOption[i].key === selectCity) {
                        for (let j = 0; j < townOption.length; j++) {
                            if (townOption[j].key === selectTown) {
                                form.address = cityOption[i].value + townOption[j].value + form.address
                            }
                        }
                    }
                }

                http.postForm(this, "address", "updateAddr",
                    form,
                    function (resp) {
                        console.log(resp)
                        if (resp.data.code === 0) {
                            that.$set(that.$data, 'position', 'bottom');
                            that.$set(that.$data, 'showPositionValue', true);
                            that.$set(that.$data, 'toastMsg', '修改成功');
                            that.$set(that.$data, 'showToast1', false)
                            that.loadData();
                        } else {
                            that.$set(that.$data, 'position', 'bottom');
                            that.$set(that.$data, 'showPositionValue', true);
                            that.$set(that.$data, 'toastMsg', '修改失败');
                        }
                    }
                )
            }
        }
    }
</script>

<style scoped>
    .add_btn {
        margin: 10px;
        position: fixed;
        bottom: 0;
        width: calc(100% - 20px);
    }

    .address_edit {
        float: right;
        margin-top: .5rem;
    }

    .address_describe {
        color: #999999;
    }

    .address_list {
        padding: 10px;
    }

    .do_btn {
        display: flex;
        flex-direction: column;
    }

</style>
