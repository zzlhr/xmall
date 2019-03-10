<template>
  <div>
    <x-header :left-options="{showBack: true}">编辑收货地址</x-header>


    <group>
      <x-input title="收货人" v-model="address.consignee"></x-input>
      <x-input title="手机号码" v-model="address.linkTel"></x-input>
      <popup-picker title="所在地区" :data="cityData" :columns="3" v-model="cityVal" show-name
                    ref="citySelect" @on-change="cityChange"></popup-picker>
      <x-input title="详细地址" v-model="address.addr"></x-input>
      <x-switch title="设为默认" :value-map="['0', '1']" v-model="address.defaultStatus"></x-switch>
    </group>
    <div class="add_btn">
      <x-button type="primary" @click.native="addAddr">保存地址</x-button>
    </div>
  </div>
</template>

<script>
  import {Group, XInput, PopupPicker, XSwitch} from 'vux'
  import http from "../util/HttpUtil";

  export default {
    name: "EditAddress",
    components: {Group, XInput, PopupPicker, XSwitch},
    data() {
      return {
        address: {
          consignee: '',
          linkTel: '',
          addr: '',
          province: '',
          city: '',
          country: '',
          defaultStatus: 0,
        },
        cityData: [],
        cityVal: [],
      }
    },
    mounted() {
      this.getCityPicker()
    },
    methods: {
      cityChange(val) {
        this.address.province = val[0];
        this.address.city = val[1];
        this.address.country = val[2];
      },
      getCityPicker() {
        const that = this;
        http.get(this, 'city', 'getCityPicker', function (data) {
          const dt = data.data;
          that.cityData = dt.data;
        })
      },
      addAddr() {
        const that = this;
        this.address.token = localStorage.getItem("token");
        http.postForm(this, "address", "add", this.address
          , function (resp) {
            if (resp.data.code === 0) {
              that.$vux.toast.show({
                type: "success",
                text: "添加收货地址成功！",
              });
              // that.loadData();
              that.$router.push({name: 'Address'})

            } else {
              that.$vux.toast.show({
                type: "warn",
                text: resp.data.msg,
              });
            }
          }
        )
      },
    }
  }
</script>

<style scoped>
  .add_btn {
    padding: 10px;
  }
</style>
