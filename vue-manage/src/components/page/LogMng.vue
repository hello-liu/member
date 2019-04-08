<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 系统日志</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-input v-model="select_word" placeholder="账号" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
            </div>
            <el-table :data="data" stripe border class="table" ref="multipleTable" size="mini">
                <el-table-column type="selection" width="55" height="20" align="center"></el-table-column>
                <el-table-column prop="token" label="token" show-overflow-tooltip>
                </el-table-column>
                <el-table-column prop="userIp" label="ip地址" >
                </el-table-column>
                <el-table-column prop="json" label="请求数据" show-overflow-tooltip>
                </el-table-column>
                <el-table-column prop="result" label="返回数据" show-overflow-tooltip>
                </el-table-column>
                <el-table-column prop="useTime" label="时间花费">
                </el-table-column>
                <el-table-column prop="method" label="请求方法">
                </el-table-column>
                <el-table-column prop="createTime" label="交易时间" :formatter="formatterDate">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background @current-change="handleCurrentChange" :page-size="pageSize"
                               layout="prev, pager, next" :total="total">
                </el-pagination>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: 'basetable',
        data() {
            return {
                tableData: [],
                cur_page: 1,
                pageSize:8,
                total:0,
                select_word: '',
            }
        },
        created() {
            this.getData();
        },
        computed: {
            data() {
                return this.tableData;
            }
        },
        methods: {
            // 分页导航
            handleCurrentChange(val) {
                this.cur_page = val;
                this.getData();
            },
            // 获取 easy-mock 的模拟数据
            getData() {
                let params = {pageNo:this.cur_page,pageNum:this.pageSize};
                if(this.select_word){
                    params.account = this.select_word;
                }
                this.$http.post('log.list', params, this).then(respose => {
                    let data = respose.data
                    if("ok" == data.code){
                        //成功
                        this.tableData = data.data.list;
                        this.total = data.data.total;
                    }else{
                        //失败，进行提示
                    }
                })

            },
            search() {
                this.getData();
            },
            formatterDate(row, column) {
                return (""+row.createTime).substring(0,10);
            },
            // 1-充值 2-消费 3-退款 4-积分兑换
            formatterType(row, column) {
                switch (row.type) {
                    case "1":return '充值';
                    case "2":return '消费';
                    case "3":return '退款';
                    case "4":return '积分兑换';
                    default :return '其他';

                }
            },
            formatterPayway(row, column) {
                // 1-余额 2-微信 3-支付宝 4-现金 5-刷卡 6-积分
                switch (row.payway) {
                    case "1":return '余额';
                    case "2":return '微信';
                    case "3":return '支付宝';
                    case "4":return '现金';
                    case "5":return '刷卡';
                    case "6":return '积分';
                    default :return '其他';

                }
            },
            formatterMoney(money){
                if(money>0){
                    return "<font color='red'>+" +money + "</fontb>";
                }else{
                    return "<font color='green'>" +money + "</fontb>";
                }
             }

        }
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }
    .table{
        width: 100%;
        font-size: 14px;
    }
    .red{
        color: #ff0000;
    }
    .mr10{
        margin-right: 10px;
    }
</style>
