<template>
  <div>
    <x-header :left-options="{showBack: true}">关于我们</x-header>
    <div style="margin-top: 100px; text-align: center">
      <h3>选货邦</h3>
      <small>v1.0.0</small>
      <p v-html="link"></p>
      <p>版权所有 选货邦开发运营团队</p>
    </div>
  </div>
</template>

<script>
  import http from '../util/HttpUtil'
  export default {
    name: "About",
    data() {
      return {
        link: ''
      }
    },
    mounted(){
      const that = this;
      http.get(this, 'app', 'link', function (resp) {
        console.log(resp)
        that.$set(that.$data, 'link', resp.data.data)
      })
    }
  }
</script>

<style scoped>

</style>
