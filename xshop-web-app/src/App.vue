<template>
    <div>
        <transition>
            <router-view/>
        </transition>
    </div>
</template>

<script>
    import {Drawer} from 'vux'

    export default {
        name: 'app',
        data() {
            return {
                showMenus: true
            }
        },
        components: {
            Drawer
        }
    }
</script>

<style lang="less">
    @import '~vux/src/styles/reset.less';
    @import '~vux/src/styles/1px.less';

    body {
        background-color: #fff;
    }

    .vux-header {
        background-color: #0084ff !important;
    }

    .tabbar {
        position: absolute;
        bottom: 0;
    }

    html, body {
        height: 100%;
        width: 100%;
        overflow-x: hidden;
        color: #333;
        font-family: Helvetica, Tahoma, Arial, STXihei, "华文细黑", "Microsoft YaHei", "微软雅黑", SimSun, "宋体", Heiti, "黑体", sans-serif;

    }

    .weui-tabbar__label {
        font-size: 15px !important;
    }

    /* 按钮主题色 */
    .weui-btn_primary {
        background-color: #0084ff !important;
    }

    .weui-btn_plain-primary {
        border-color: #0084ff !important;
        color: #0084ff !important;
    }
</style>
