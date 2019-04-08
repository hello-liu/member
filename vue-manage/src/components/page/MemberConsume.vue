<template>
    <div>
        <el-form ref="form" :model="form" label-width="100px">
            <el-form-item label="id" :hidden="true">
                <el-input v-model="form.memberId"></el-input>
            </el-form-item>
            <el-form-item label="账号（卡号）">
                <el-input ref="id_account" v-model="form.account" @keyup.enter.native="getMember"></el-input>
            </el-form-item>
            <el-form-item label="手机">
                <el-input v-model="form.phone" @keyup.enter.native="getMember"></el-input>
            </el-form-item>
            <el-form-item label="姓名" >
                <el-input v-model="form.name" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="商户" >
                <el-input v-model="form.merchantName" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="余额" >
                <el-input v-model="form.currMoney" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="积分" >
                <el-input v-model="form.currIntegral" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="付款方式">
                <!--1-余额 2-微信 3-支付宝 4-现金 5-刷卡 6-积分-->
                <el-select v-model="form.payway" placeholder="请选择付款方式">
                    <el-option label="余额" value="1"></el-option>
                    <el-option label="微信" value="2"></el-option>
                    <el-option label="支付宝" value="3"></el-option>
                    <el-option label="现金" value="4"></el-option>
                    <el-option label="刷卡" value="5"></el-option>
                    <el-option label="其他" value="0"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="消费金额">
                <el-input ref="id_money" v-model.number="form.money" @keyup.enter.native="pwdFocus"></el-input>
            </el-form-item>
            <el-form-item label="密码">
                <el-input ref="id_pwd" type="password" v-model="form.pwd" @keyup.enter.native="onSubmit"></el-input>
            </el-form-item>
            <el-form-item label="操作员" >
                <el-input v-model="form.operator" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="备注">
                <el-input ref="id_content" v-model="form.content" type="textarea" ></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onSubmit">消费</el-button>
                <el-button @click="onCancel">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                form: {
                    memberId: '',
                    account: '',
                    phone: '',
                    name: '',
                    merchantName: '',
                    money: '',
                    pwd: '',
                    currMoney: '',
                    currIntegral: '',
                    payway: '1',
                    content: '会员消费',
                    operator:''
                },
                // formEmpty: JSON.parse(JSON.stringify(this.form)),
                formEmpty: {
                    memberId: '',
                    account: '',
                    phone: '',
                    name: '',
                    merchantName: '',
                    money: '',
                    pwd: '',
                    currMoney: '',
                    currIntegral: '',
                    payway: '1',
                    content: '会员消费',
                    operator:''
                },

            }
        },
        created(){
            let user = localStorage.getItem('user') ;
            let userData =JSON.parse(user);
            this.form.operator = userData.nickname;
            this.formEmpty.operator = userData.nickname;
        },
        methods: {
            onSubmit() {
                if(this.form.money){

                }else{
                    return ;
                }
                //消费时金额为负
                this.form.money = -this.form.money;
                this.$http.post('busi.consume', this.form, this).then(respose => {
                    let data = respose.data
                    if("ok" == data.code){
                        //成功
                        this.$alert(data.msg, "成功！", {confirmButtonText: '确定',
                            callback: action => {
                                this.onCancel()
                                this.accountFocus()
                            }
                        });
                    }else{
                        //失败，进行提示
                        this.$alert(data.msg+data.data, "错误！", {confirmButtonText: '确定',
                            callback: action => {
                                this.onCancel()
                                this.accountFocus()
                            }});
                    }
                    //将焦点回到账号

                })

            },
            getMember() {

                let params = {}
                if(this.form.account){
                    params.account = this.form.account
                }else if(this.form.phone){
                    params.phone = this.form.phone
                }else {
                    return ;
                }
                this.$http.post('member.get', params, this).then(respose => {
                    let data = respose.data
                    if("ok" == data.code){
                        //成功
                        if(data.data){
                            this.form.name = data.data.nickname
                            this.form.memberId = data.data.id
                            this.form.merchantName = data.data.merchantName
                            this.form.currMoney = data.data.money
                            this.form.currIntegral = data.data.integral
                            this.moneyFocus()
                        }else{
                            this.onCancel();
                            this.$alert("未查询到会员！", "错误！", {confirmButtonText: '确定',
                                callback: action => {
                                    // this.accountFocus()
                                }
                            });
                        }
                        //将焦点放到金额输入框

                    }else{
                        this.onCancel();
                        //失败，进行提示
                        this.$alert(data.msg+data.data, "错误！", {confirmButtonText: '确定',
                            callback: action => {
                                // this.accountFocus()
                            }
                        });
                    }

                })
            },
            onCancel(){
                this.form = JSON.parse(JSON.stringify(this.formEmpty))
            },
            moneyFocus(){
                this.$refs.id_money.focus()
            },
            accountFocus(){
                this.$refs.id_account.focus()
            },
            pwdFocus(){
                this.$refs.id_pwd.focus()
            },



        }
    }
</script>