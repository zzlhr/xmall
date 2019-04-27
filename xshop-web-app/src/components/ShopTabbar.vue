<template>
  <div>
    <tabbar>
      <tabbar-item selected link="/home" :selected="selected === 'home'">
        <img slot="icon" src="../assets/home.png" alt="home">
        <span slot="label">首页</span>
      </tabbar-item>
      <tabbar-item link="/class" :selected="selected === 'class'">
        <img slot="icon" src="../assets/class.png">
        <span slot="label">分类</span>
      </tabbar-item>
      <tabbar-item link="/shoppingCart" :selected="selected === 'shoppingCart'">
        <img slot="icon" src="../assets/gwc.png">
        <span slot="label">购物车</span>
      </tabbar-item>
      <tabbar-item link="/my" :selected="selected === 'my'">
        <img slot="icon" src="../assets/my.png">
        <span slot="label">我的</span>
      </tabbar-item>
    </tabbar>
  </div>
</template>

<script>
  export default {
    name: "ShopTabbar",
    props: {
      selected: String
    }
  }
</script>

<style scoped>
  .weui-tabbar {
    position: fixed;
  }
</style>
