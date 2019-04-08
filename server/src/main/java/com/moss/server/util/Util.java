package com.moss.server.util;

import com.alibaba.fastjson.JSONObject;
import com.moss.common.model.BackModel;
import com.moss.common.model.CheckParamsModel;
import com.moss.server.common.Constant;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    //获得一个uuid
    public static String getUuid(){
        UUID uuid = UUID.randomUUID();
        return  uuid.toString().replace("-","");
    }

    //验证请求参数
    public static BackModel checkParams(JSONObject json, List<CheckParamsModel> cps ){

        if(cps == null || cps.size() <1){
            return null;
        }

        for(CheckParamsModel param : cps){
            String value = json.getString(param.getName() );
            //参数为空
            if(StringUtils.isEmpty( value ) ){
                return new BackModel("error","参数错误！", "["+ param.getName() + "] can not null");
            }
            //小于长度
            if( value.length() < param.getMinLength()  ){
                return new BackModel("error","参数错误！", "["+ param.getName() + "].length < " + param.getMinLength() );
            }
            //大于长度
            if( value.length() > param.getMaxLength()  ){
                return new BackModel("error","参数错误！", "["+ param.getName() + "].length > " + param.getMaxLength() );
            }
            //验证正则
            if( ! match(param.getReg(), value) ){
                return new BackModel("error","参数错误！", "["+ param.getName() + "] do not match " );
            }
        }

        return null;

    }

    //验证正则
    private static boolean match(String regex, String str) {
        if(StringUtils.isEmpty(regex) ){
            return true;
        }
        if(str == null){
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    //前端传过来的密文密码再加密，用于存库
    public static boolean checkPwd(String pwd, String dbPsw) throws Exception{
        pwd = getPwd(pwd);
        if(pwd.equals(dbPsw)){
            return true;
        }else{
            return false;
        }
    }

    //前端传过来的密文密码再加密，用于存库
    public static String getPwd(String pwd) throws Exception{
        return encoderByMd5(pwd+ Constant.SALT_4 );
    }

    public static String encoderByMd5(String str) throws Exception{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密后的字符串
        String newstr=base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    public static void main(String[] args){
        System.out.println( match("","") );
    }
}
