<template>
  <div class="s-search">
    <form class="search-main" action="javascrip: return true" @submit="searchSubmit">
      <input type="search" class="search-input" v-model="searchVal" placeholder="请输入商品关键字">
    </form>
  </div>
</template>

<script>
  export default {
    name: "s-search",
    data() {
      return {
        searchVal: '',
      }
    },
    methods: {
      searchSubmit() {
        console.log(this.searchVal)
        this.$router.push({name: 'SaleList', params: {title: this.searchVal, cid: 5}})
      }
    }
  }
</script>

<style scoped>
  .s-search {
    position: absolute;
    top: 0;
    height: 46px;
    background: #0084ff;
    width: 100%;
    display: flex;
    flex-flow: row nowrap;
  }

  .search-main {
    flex: 1;
    margin: auto 10px;
  }

  .search-input {
    width: 100%;
    line-height: 25px;
    border-radius: 25px;
    border: 2px solid #0084ff;
    text-indent: 4px;
  }
</style>
