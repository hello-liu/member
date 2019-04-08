<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" background-color="#324157"
            text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
            <template v-for="item in getTtems">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template  slot="title">{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item :class="subItem.icon"  v-else :index="subItem.index" :key="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    import bus from '../common/bus';
    export default {
        data() {
            return {
                collapse: false,
                itemsAdmin: [
                    {
                        icon: 'el-icon-lx-home',
                        index: 'dashboard',
                        title: '系统首页'
                    },
                    {
                        icon: 'el-icon-lx-sort',
                        index: 'sysMng',
                        title: '系统管理',
                        subs: [
                            {
                                icon: "el-icon-lx-people",
                                index: 'userMng',
                                title: '用户管理'
                            },
                            {
                                icon: "el-icon-lx-home",
                                index: 'merchantMng',
                                title: '商户管理'
                            },
                            {
                                icon: "el-icon-lx-home",
                                index: 'logMng',
                                title: '系统日志'
                            }
                        ]

                    },
                    {
                        icon: 'el-icon-lx-group',
                        index: 'memberMng',
                        title: '会员管理',
                        subs: [
                            {
                                icon: "el-icon-lx-group",
                                index: 'memberMng',
                                title: '会员管理'
                            },
                            {
                                icon: "el-icon-lx-friendadd",
                                index: 'memberCharge',
                                title: '会员充值'
                            },
                            {
                                icon: "el-icon-lx-cart",
                                index: 'memberConsume',
                                title: '会员消费'
                            },
                            {
                                icon: "el-icon-lx-profile",
                                index: 'memberRefund',
                                title: '会员退款'
                            },
                            {
                                icon: "el-icon-lx-present",
                                index: 'memberIntegralToMoney',
                                title: '积分兑换'
                            },
                            {
                                icon: "el-icon-lx-calendar",
                                index: 'memberFlow',
                                title: '会员流水'
                            }
                        ]

                    },
                ],
                itemsUser: [
                    {
                        icon: 'el-icon-lx-home',
                        index: 'dashboard',
                        title: '系统首页'
                    },
                    {
                        icon: 'el-icon-lx-group',
                        index: 'memberMng',
                        title: '会员管理',
                        subs: [
                            {
                                icon: "el-icon-lx-group",
                                index: 'memberMng',
                                title: '会员管理'
                            },
                            {
                                icon: "el-icon-lx-friendadd",
                                index: 'memberCharge',
                                title: '会员充值'
                            },
                            {
                                icon: "el-icon-lx-cart",
                                index: 'memberConsume',
                                title: '会员消费'
                            },
                            {
                                icon: "el-icon-lx-profile",
                                index: 'memberRefund',
                                title: '会员退款'
                            },
                            {
                                icon: "el-icon-lx-present",
                                index: 'memberIntegralToMoney',
                                title: '积分兑换'
                            },
                            {
                                icon: "el-icon-lx-calendar",
                                index: 'memberFlow',
                                title: '会员流水'
                            }
                        ]

                    },
                ]
            }
        },
        computed:{
            onRoutes(){
                return this.$route.path.replace('/','');
            },
            getTtems(){
                let user = localStorage.getItem('user') ;
                let userData =JSON.parse(user);
                if(userData.account == 'admin'){
                    return this.itemsAdmin;
                }else{
                    return this.itemsUser;
                }

            }
        },
        created(){
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            bus.$on('collapse', msg => {
                this.collapse = msg;
            })
        }
    }
</script>

<style scoped>
    .sidebar{
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom:0;
        overflow-y: scroll;
    }
    .sidebar::-webkit-scrollbar{
        width: 0;
    }
    .sidebar-el-menu:not(.el-menu--collapse){
        width: 250px;
    }
    .sidebar > ul {
        height:100%;
    }
</style>
