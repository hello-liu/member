<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-cascades"></i> 用户管理</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">
                <el-button type="primary" icon="el-icon-plus" class="handle-del mr10" @click="add">添加</el-button>
                <el-input v-model="select_word" placeholder="筛选关键词" class="handle-input mr10"></el-input>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
            </div>
            <el-table :data="data" border stripe class="table" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column prop="nickname" label="姓名" >
                </el-table-column>
                <el-table-column prop="account" label="账号" >
                </el-table-column>
                <el-table-column prop="merchantName" label="商户" >
                </el-table-column>
                <el-table-column prop="phone" label="手机" >
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" :formatter="formatter">
                </el-table-column>
                <el-table-column prop="flag" label="状态">
                    <template slot-scope="scope">
                        <el-switch v-if="(scope.row.flag)=='0'?state=true:state ='1'"
                                   v-model="state" inactive-text="注销" @change="changeFlag(scope.row)">
                        </el-switch>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="80" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination background @current-change="handleCurrentChange" :page-size="pageSize"
                               layout="prev, pager, next" :total="total">
                </el-pagination>
            </div>
        </div>

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑用户" :visible.sync="editVisible" width="50%">
            <el-form ref="form" :model="form" label-width="80px">
                <el-form-item label="id" :hidden="true">
                    <el-input v-model="form.id" ></el-input>
                </el-form-item>
                <el-form-item label="*姓名" 	>
                    <el-input v-model="form.nickname" ></el-input>
                </el-form-item>
                <el-form-item label="性别">
                    <el-select v-model="form.sex" placeholder="请选择">
                        <el-option key="f" label="女" value="女"></el-option>
                        <el-option key="m" label="男" value="男"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="年龄">
                    <el-input v-model.number="form.age"></el-input>
                </el-form-item>
                <el-form-item label="*密码">
                    <el-input type="password" v-model="form.pwd" :disabled="isEdit"></el-input>
                </el-form-item>
                <el-form-item label="*账号">
                    <el-input v-model="form.account" :disabled="isEdit"></el-input>
                </el-form-item>
                <el-form-item label="*手机">
                    <el-input v-model.number="form.phone"></el-input>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="form.email"></el-input>
                </el-form-item>
                <el-form-item label="身份证">
                    <el-input v-model="form.idnumber"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="form.address"></el-input>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
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
                multipleSelection: [],
                select_word: '',
                editVisible: false,
                isEdit:false,
                form: {
                    merchantId: '1',
                    nickname: '',
                    sex: '',
                    age: '',
                    pwd: '',
                    account: '',
                    phone: '',
                    email: '',
                    idnumber: '',
                    address: ''
                },
                formEmpty: {
                    merchantId: '1',
                    nickname: '',
                    sex: '',
                    age: '',
                    pwd: '',
                    account: '',
                    phone: '',
                    email: '',
                    idnumber: '',
                    address: ''
                }
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
                    params.nickname = this.select_word;
                    params.account = this.select_word;
                    params.phone = this.select_word;
                    params.idnumber = this.select_word;
                }
                this.$http.post('user.list', params, this).then(respose => {
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
            formatter(row, column) {
                return (""+row.createTime).substring(0,10);
            },
            handleEdit(index, row) {
                this.isEdit = true;
                //深拷贝
                let temp = JSON.parse(JSON.stringify(row));
                this.form = temp;
                this.editVisible = true;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 保存编辑
            saveEdit() {
                let method = 'user.add'
                if(this.form.id){
                    method = 'user.update'
                }
                this.$http.post(method, this.form, this).then(respose => {
                    let data = respose.data
                    if("ok" == data.code){
                        //添加成功
                        this.$alert(data.msg, "成功！", {confirmButtonText: '确定',
                            callback: action => {

                                this.editVisible = false;
                                //刷新列表
                                this.getData();
                            }
                        });
                    }else{
                        //失败，进行提示
                        this.$alert(data.msg+data.data, "错误！", {confirmButtonText: '确定',});
                    }
                })


            },
            //新增
            add() {
                this.form = JSON.parse(JSON.stringify(this.formEmpty))
                this.isEdit = false;
                this.editVisible = true;
            },
            changeFlag(row){

                let params = {id:row.id, flag: row.flag=='0'?'1':'0' }
                this.$http.post('user.updateFlag', params, this).then(respose => {
                    let data = respose.data
                    if("ok" == data.code){
                        //添加成功
                        this.getData();
                    }else{
                        //失败，进行提示
                        this.$alert(data.msg+data.data, "错误！", {confirmButtonText: '确定',});
                    }
                })
            },

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
