<template>
    <div id="app">
        <router-view></router-view>
    </div>
</template>

<script>
    export default {
        name: "App"
    }
</script>

<style>
    #app {
        font-family: Helvetica, sans-serif;
        height: 100%;
    }

    .el-header, .el-aside {
        background-color: #567390;
    }

    /*, .el-aside*/
    .el-container {
        height: 100%;
    }

    .el-menu {
        border-right: solid 0;
    }

    thead > tr > th {
        background-color: #dbe5ec !important;
        color: #666;
    }

    html, body {
        width: 100%;
        height: 100%;
        margin: 0;
        padding: 0;
    }

    /* .el-header {
        margin: 0; */
    /* padding: 0;
} */

    .el-pagination, .el-footer {
        text-align: center;
    }

    .el-footer {
        /*padding-top: 1rem;*/
        height: 1rem;
        padding-top: 1rem;
    }

    .right-menu-item {
        float: right !important;
    }

    .el-table td, .el-table th {
        padding: 5px 0;
    }

    /* 修复card scroll */
    .box-card {
        overflow-y: scroll;
    }
</style>
