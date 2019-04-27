<template>
    <div class="s-search">
        <form class="search-main" action="javascript: return true;" @submit="searchSubmit">
            <input type="search" class="search-input" v-model="searchVal" placeholder="请输入商品关键字">
        </form>
        <div class="search-right">
            <font-awesome-icon class="search-msg-icon" icon="bell"></font-awesome-icon>
        </div>
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

                this.$router.push({name: 'SaleList', params: {title: this.searchVal, cid: 5}})
            }
        }
    }
</script>

<style lang="scss" scoped>
    @import "../styles/xshop";

    .s-search {
        z-index: 1000;
        position: absolute;
        top: 0;
        height: 46px;
        background: $themeColor;
        border-bottom: 1px solid $themeColor;
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
        border: 2px solid $themeColor;
        text-indent: 10px;
        outline: none;
        padding-right: 10px;
    }

    .search-right {
        width: 60px;

        .search-msg-icon {
            width: 20px;
            height: 20px;
            color: #f5ede1;
            margin: 13px 20px;
        }
    }
</style>
