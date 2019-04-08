package com.moss.common.init;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.util.List;
import java.util.Map;

public class InitMoss {

    static String flagCreate = "create";
    static String flagNone = "none";

    public static void init(JdbcTemplate jdbcTemplate, String package_, String rootPath, String schema, Map<String,String> map ) throws Exception {

        String sql = "select table_name from information_schema.tables  where table_schema='"+schema+"'";

        List<String> tables = jdbcTemplate.queryForList(sql, String.class ) ;
        System.out.println(tables);

        for( String table : tables) {

            initTable( table, jdbcTemplate,  package_,  rootPath,  schema, map);

        }

    }

    public static void initTable(String table,JdbcTemplate jdbcTemplate, String package_, String rootPath, String schema, Map<String,String> map) throws Exception {

        String sql = "SELECT column_name columnName, data_type dataType, extra"
                + " FROM information_schema.COLUMNS"
                + " WHERE table_name = '" + table + "' AND table_schema = '" + schema + "' ORDER BY ordinal_position";

        List<Map<String, Object>> coleumns = jdbcTemplate.queryForList(sql);

        if(coleumns.size()<1){
            return;
        }

        System.out.println(map);
        //create model file
        if(flagCreate.equals(map.get("model") ) ){
            createModel( table, rootPath,  package_,  coleumns);
        }


        //create dao file
        if(flagCreate.equals(map.get("dao") ) ) {
            createDao(table, rootPath, package_, coleumns);
        }

        //create service fil
        if(flagCreate.equals(map.get("service") ) ) {
            createService(table, rootPath, package_, coleumns);
        }

        //modify controller file
        if(flagCreate.equals(map.get("controller") ) ) {
            createController(table, rootPath, package_, coleumns);
        }

    }

    static void createController(String table,String rootPath, String package_, List<Map<String, Object>> coleumns) throws Exception{

        String filePath = rootPath + File.separator + "controller" + File.separator + "ServiceMaper.java";
        String filePathNew = rootPath + File.separator + "controller" + File.separator + "ServiceMaper1.java";
        File file = new File(filePath);
        File fileNew = new File(filePathNew);
        if( !file.exists() ){
            System.out.println("not find ServiceMaper.java");
            return;
        }


        BufferedReader reader = new BufferedReader( new FileReader( file ) );
        BufferedWriter writer = new BufferedWriter( new FileWriter( fileNew ) );
        String line = "";
        while ( (line = reader.readLine() ) != null) {
            int index = line.indexOf("//@autoControllerCase");
            if ( index >=0 ){
                line = "\t\t\t//@autoControllerCase\n";
                line += "\t\t\t//"+getClassLow(table)+"\n";
                line += "\t\t\tcase \""+getClassLow(table)+".add\":return "+getClassLow(table)+"Service.add(json);\n";
                line += "\t\t\tcase \""+getClassLow(table)+".del\":return "+getClassLow(table)+"Service.del(json);\n";
                line += "\t\t\tcase \""+getClassLow(table)+".update\":return "+getClassLow(table)+"Service.update(json);\n";
                line += "\t\t\tcase \""+getClassLow(table)+".get\":return "+getClassLow(table)+"Service.get(json);\n";
                line += "\t\t\tcase \""+getClassLow(table)+".list\":return "+getClassLow(table)+"Service.list(json);\n";
                line += "\t\t\tcase \""+getClassLow(table)+".page\":return "+getClassLow(table)+"Service.page(json);\n";
            }
            index = line.indexOf("//@autoControllerAutowired");
            if ( index >=0 ){
                line = "\t//@autoControllerAutowired\n";
                line += "\t@Autowired\n";
                line += "\t"+getClass(table)+"Service "+getClassLow(table)+"Service;\n";
            }
            index = line.indexOf("//@autoControllerImport");
            if ( index >=0 ){
                line = "//@autoControllerImport\n";
                line += "import com.moss.server.service."+getClass(table)+"Service;";
            }
            writer.write(line);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        reader.close();

        file.delete();
        fileNew.renameTo(file);

    }

    static void createService(String table,String rootPath, String package_, List<Map<String, Object>> coleumns) throws Exception{

        String filePath = rootPath + File.separator + "service";
        File file = new File(filePath);
        if( !file.exists() ){
            file.mkdirs();
        }

        String className = getClass(table)+"Service";
        String modelPath = filePath + File.separator + className+".java";
        File model = new File(modelPath);
        BufferedWriter br = new BufferedWriter( new FileWriter( model));

        br.write("package "+package_+".service;");
        br.newLine();
        br.newLine();

        //import
        br.write("import "+package_+".model."+getClass(table)+";");
        br.newLine();
        br.write("import "+package_+".dao."+getClass(table)+"Dao;");
        br.newLine();
        br.write("import com.moss.common.model.PageModel;");
        br.newLine();
        br.write("import com.moss.common.model.BackModel;");
        br.newLine();
        br.write("import com.alibaba.fastjson.JSONObject;");
        br.newLine();
        br.write("import org.springframework.beans.factory.annotation.Autowired;");
        br.newLine();
        br.write("import org.springframework.stereotype.Service;");
        br.newLine();
        br.write("import org.springframework.transaction.annotation.Transactional;");
        br.newLine();
        br.write("import java.util.List;");
        br.newLine();

        br.newLine();
        br.write("@Service");
        br.newLine();
        br.write("public class "+getClass(table)+"Service {");
        br.newLine();
        br.newLine();
        br.write("\t@Autowired");
        br.newLine();
        br.write("\t"+getClass(table)+"Dao "+getClassLow(table)+"Dao;");
        br.newLine();

        //add
        br.newLine();
        br.write("\t@Transactional");
        br.newLine();
        br.write("\tpublic BackModel add(JSONObject json ) {");
        br.newLine();
        br.write("\t\t"+getClass(table)+" "+getClassLow(table)+" = JSONObject.toJavaObject(json,"+getClass(table)+".class);");
        br.newLine();
        br.write("\t\tint result = "+getClassLow(table)+"Dao.add("+getClassLow(table)+");");
        br.newLine();
        br.write("\t\treturn new BackModel(\"ok\",\"add "+getClassLow(table)+" success!\");");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //del
        br.newLine();
        br.write("\t@Transactional");
        br.newLine();
        br.write("\tpublic BackModel del(JSONObject json ) {");
        br.newLine();
        br.write("\t\tInteger id = json.getInteger(\"id\");");
        br.newLine();
        br.write("\t\tint result = "+getClassLow(table)+"Dao.del(id);");
        br.newLine();
        br.write("\t\treturn new BackModel(\"ok\",\"del "+getClassLow(table)+" success!\");");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //update
        br.newLine();
        br.write("\t@Transactional");
        br.newLine();
        br.write("\tpublic BackModel update(JSONObject json ) {");
        br.newLine();
        br.write("\t\t"+getClass(table)+" "+getClassLow(table)+" = JSONObject.toJavaObject(json,"+getClass(table)+".class);");
        br.newLine();
        br.write("\t\tint result = "+getClassLow(table)+"Dao.update("+getClassLow(table)+");");
        br.newLine();
        br.write("\t\treturn new BackModel(\"ok\",\"update "+getClassLow(table)+" success!\");");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //get
        br.newLine();
        br.write("\t@Transactional");
        br.newLine();
        br.write("\tpublic BackModel get(JSONObject json ) {");
        br.newLine();
        br.write("\t\tInteger id = json.getInteger(\"id\");");
        br.newLine();
        br.write("\t\t"+getClass(table)+" "+getClassLow(table)+" = "+getClassLow(table)+"Dao.get(id);");
        br.newLine();
        br.write("\t\treturn new BackModel(\"ok\",\"get "+getClassLow(table)+" success!\","+getClassLow(table)+");");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //list
        br.newLine();
        br.write("\t@Transactional");
        br.newLine();
        br.write("\tpublic BackModel list(JSONObject json ) {");
        br.newLine();
        br.write("\t\t"+getClass(table)+" "+getClassLow(table)+" = JSONObject.toJavaObject(json,"+getClass(table)+".class);");
        br.newLine();
        br.write("\t\tList<"+getClass(table)+"> "+getClassLow(table)+"s = "+getClassLow(table)+"Dao.list("+getClassLow(table)+");");
        br.newLine();
        br.write("\t\treturn new BackModel(\"ok\",\"list "+getClassLow(table)+" success!\","+getClassLow(table)+"s);");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //page
        br.newLine();
        br.write("\t@Transactional");
        br.newLine();
        br.write("\tpublic BackModel page(JSONObject json ) {");
        br.newLine();
        br.write("\t\t"+getClass(table)+" "+getClassLow(table)+" = JSONObject.toJavaObject(json,"+getClass(table)+".class);");
        br.newLine();
        br.write("\t\tInteger page = json.getInteger(\"page\");");
        br.newLine();
        br.write("\t\tInteger size = json.getInteger(\"size\");");
        br.newLine();
        br.write("\t\tPageModel pageDate = "+getClassLow(table)+"Dao.page("+getClassLow(table)+", page, size);");
        br.newLine();
        br.write("\t\treturn new BackModel(\"ok\",\"page "+getClassLow(table)+" success!\",pageDate);");
        br.newLine();
        br.write("\t}");
        br.newLine();

        br.newLine();
        br.write("}");

        br.flush();
        br.close();
    }

    static void createDao(String table,String rootPath, String package_, List<Map<String, Object>> coleumns) throws Exception{

        String filePath = rootPath + File.separator + "dao";
        File file = new File(filePath);
        if( !file.exists() ){
            file.mkdirs();
        }

        String className = getClass(table)+"Dao";
        String modelPath = filePath + File.separator + className+".java";
        File model = new File(modelPath);
        BufferedWriter br = new BufferedWriter( new FileWriter( model));

        br.write("package "+package_+".dao;");
        br.newLine();
        br.newLine();

        //import
        br.write("import "+package_+".model."+getClass(table)+";");
        br.newLine();
        br.write("import com.moss.common.model.PageModel;");
        br.newLine();
        br.write("import org.springframework.beans.factory.annotation.Autowired;");
        br.newLine();
        br.write("import org.springframework.jdbc.core.BeanPropertyRowMapper;");
        br.newLine();
        br.write("import org.springframework.jdbc.core.JdbcTemplate;");
        br.newLine();
        br.write("import org.springframework.stereotype.Repository;");
        br.newLine();
        br.write("import org.springframework.util.StringUtils;");
        br.newLine();
        br.write("import java.util.ArrayList;");
        br.newLine();
        br.write("import java.util.List;");
        br.newLine();

        br.newLine();
        br.write("@Repository");
        br.newLine();
        br.write("public class "+getClass(table)+"Dao {");
        br.newLine();
        br.newLine();
        br.write("\t@Autowired");
        br.newLine();
        br.write("\tprivate JdbcTemplate jdbcTemplate;");
        br.newLine();

        //add
        String[] strs = getFieldNotExtra(coleumns,"auto_increment");
        br.newLine();
        br.write("\tpublic int add("+getClass(table)+" obj ) {");
        br.newLine();
        br.write("\t\tString sql = \"insert into "+table+"("+strs[0]+") \" +");
        br.newLine();
        br.write("\t\t\t\" values("+strs[2]+") \";");
        br.newLine();
        br.write("\t\treturn jdbcTemplate.update(sql,");
        br.newLine();
        br.write("\t\t\t"+strs[1]+" );");
        br.newLine();
        br.write("\t}");

        //del
        br.newLine();
        br.newLine();
        br.write("\tpublic int del(Integer id ) {");
        br.newLine();
        br.write("\t\tString sql = \"delete from "+table+" where id = ? \";");
        br.newLine();
        br.write("\t\treturn jdbcTemplate.update(sql,id);");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //update
        String strUpdateSets = "";
        for(Map<String, Object> coleumn :coleumns ) {
            String dataType = (String) coleumn.get("dataType");
            String columnName = (String) coleumn.get("columnName");
            String extra1 = (String) coleumn.get("extra");
            if("auto_increment".equals(extra1 ) == false){
                strUpdateSets += ", "+columnName+" =?";
            }
        }
        strUpdateSets = strUpdateSets.substring(1,strUpdateSets.length() );

        br.newLine();
        br.write("\tpublic int update("+getClass(table)+" obj ) {");
        br.newLine();
        br.write("\t\tString sql =  \"update "+table+ "\" +");
        br.newLine();
        br.write("\t\t\t\" set "+strUpdateSets+ " where id=? \";");
        br.newLine();
        br.write("\t\treturn jdbcTemplate.update(sql,");
        br.newLine();
        br.write("\t\t\t"+strs[1]+", obj.getId() );");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //get
        String[] strs1 = getFieldNotExtra(coleumns,"all");

        br.newLine();
        br.write("\tpublic "+getClass(table)+" get(Integer id ) {");
        br.newLine();
        br.write("\t\tString sql = \"select "+strs1[0]+" from "+table+" where id = ? \";");
        br.newLine();
        br.write("\t\treturn jdbcTemplate.queryForObject(sql, new Object[]{id }, new BeanPropertyRowMapper<"+getClass(table)+">("+getClass(table)+".class) );");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //list
        br.newLine();
        br.write("\tpublic List<"+getClass(table)+"> list("+getClass(table)+" obj ) {");
        br.newLine();
        br.write("\t\tString sql = \"select "+strs1[0]+" from "+table+" where 1=1 \";");
        br.newLine();
        br.write("\t\tList<Object> sqlParams = new ArrayList<Object>();");
        br.newLine();
        for(Map<String, Object> coleumn :coleumns ){
            String dataType = (String) coleumn.get("dataType");
            String columnName = (String) coleumn.get("columnName");
            String extra = (String) coleumn.get("extra");
            if("auto_increment".equals(extra ) == false){
                br.write("\t\tif( obj.get"+getMethod(columnName)+"() != null){");
                br.newLine();
                br.write("\t\t\tsql += \" and "+columnName+" = ? \";");
                br.newLine();
                br.write("\t\t\tsqlParams.add(obj.get"+getMethod(columnName)+"() );");
                br.newLine();
                br.write("\t\t}");
                br.newLine();
            }
        }
        br.write("\t\tsql += \" limit 1000 \";");
        br.newLine();
        br.write("\t\tList<"+getClass(table)+"> list = jdbcTemplate.query(sql, sqlParams.toArray(), new BeanPropertyRowMapper<"+getClass(table)+">("+getClass(table)+".class) );");
        br.newLine();
        br.write("\t\treturn list;");
        br.newLine();
        br.write("\t}");
        br.newLine();

        //page
        br.newLine();
        br.write("\tpublic PageModel page("+getClass(table)+" obj, int page, int size ) {");
        br.newLine();
        br.write("\t\tint start = (page-1)*size;");
        br.newLine();
        br.write("\t\tString sql = \"select "+strs1[0]+" from "+table+" where 1=1 \";");
        br.newLine();
        br.write("\t\tString sqlCount = \"select count(*) from "+table+" where 1=1 \";");
        br.newLine();
        br.write("\t\tList<Object> sqlParams = new ArrayList<Object>();");
        br.newLine();
        br.write("\t\tList<Object> sqlCountParams = new ArrayList<Object>();");
        br.newLine();
        for(Map<String, Object> coleumn :coleumns ){
            String dataType = (String) coleumn.get("dataType");
            String columnName = (String) coleumn.get("columnName");
            String extra = (String) coleumn.get("extra");
            if("auto_increment".equals(extra ) == false){
                br.write("\t\tif( obj.get"+getMethod(columnName)+"() != null){");
                br.newLine();
                br.write("\t\t\tsql += \" and "+columnName+" = ? \";");
                br.newLine();
                br.write("\t\t\tsqlCount += \" and "+columnName+" = ? \";");
                br.newLine();
                br.write("\t\t\tsqlParams.add(obj.get"+getMethod(columnName)+"() );");
                br.newLine();
                br.write("\t\t\tsqlCountParams.add(obj.get"+getMethod(columnName)+"() );");
                br.newLine();
                br.write("\t\t}");
                br.newLine();
            }
        }
        br.write("\t\tsql += \" limit ?,? \";");
        br.newLine();
        br.write("\t\tsqlParams.add(start );");
        br.newLine();
        br.write("\t\tsqlParams.add(size );");
        br.newLine();
        br.write("\t\tint count = jdbcTemplate.queryForObject(sqlCount, sqlCountParams.toArray(), Integer.class );");
        br.newLine();
        br.write("\t\tList<"+getClass(table)+"> list = jdbcTemplate.query(sql, sqlParams.toArray(), new BeanPropertyRowMapper<"+getClass(table)+">("+getClass(table)+".class) );");
        br.newLine();
        br.write("\t\treturn new PageModel(count,list);");
        br.newLine();
        br.write("\t}");
        br.newLine();

        br.newLine();
        br.write("}");

        br.flush();
        br.close();

    }

    static void createModel(String table,String rootPath, String package_, List<Map<String, Object>> coleumns) throws Exception{

        //create model path
        String filePath = rootPath + File.separator + "model";
        File file = new File(filePath);
        if( !file.exists() ){
            file.mkdirs();
        }

        String className = getClass(table);
        String modelPath = filePath + File.separator + className+".java";
        File model = new File(modelPath);
        BufferedWriter br = new BufferedWriter( new FileWriter( model));

        br.write("package "+package_+".model;");
        br.newLine();
        br.newLine();
        br.write("import java.util.Date;");
        br.newLine();
        br.newLine();
        br.write("public class "+className+" {");
        br.newLine();
        br.newLine();

        for(Map<String, Object> coleumn :coleumns ){
            String dataType = (String) coleumn.get("dataType");
            String columnName = (String) coleumn.get("columnName");
            br.write("\tprivate "+getDateType(dataType) + " " +getName(columnName) + ";");
            br.newLine();

        }

        br.newLine();

        for(Map<String, Object> coleumn :coleumns ){
            String dataType = (String) coleumn.get("dataType");
            String columnName = (String) coleumn.get("columnName");
            br.write("\tpublic "+getDateType(dataType)+" get"+getMethod(columnName)+"() {");
            br.newLine();
            br.write("\t\treturn "+getName(columnName)+";");
            br.newLine();
            br.write("\t}");
            br.newLine();
            br.newLine();
            br.write("\tpublic void set"+getMethod(columnName)+"("+getDateType(dataType)+" "+getName(columnName)+") {");
            br.newLine();
            br.write("\t\tthis."+getName(columnName)+" = "+getName(columnName)+";");
            br.newLine();
            br.write("\t}");
            br.newLine();
            br.newLine();

        }


        br.write("}");
        br.newLine();

        br.flush();
        br.close();
    }

    // get field do not contain type
    // extra: auto_increment,varchar,int
    static String[] getFieldNotExtra(List<Map<String, Object>> coleumns,String extra){

        String result = "";
        String resultGets = "";
        String resultCount = "";
        for(Map<String, Object> coleumn :coleumns ) {
            String dataType = (String) coleumn.get("dataType");
            String columnName = (String) coleumn.get("columnName");
            String extra1 = (String) coleumn.get("extra");
            if(extra.equals(extra1 ) == false){
                result += ", "+columnName;
                resultGets += ", obj.get"+getMethod(columnName)+"() ";
                resultCount += ", ?";
            }
        }
        result = result.substring(1,result.length() );
        resultGets = resultGets.substring(1,resultGets.length() );
        resultCount = resultCount.substring(1,resultCount.length() );
        return new String[]{result, resultGets, resultCount };
    }

    // mysql type to java type
    static String getDateType(String type){

        if("int".equals(type )){
            return "Integer";
        }else if("varchar".equals(type )){
            return "String";
        }else if("datetime".equals(type )){
            return "Date";
        }
        return "String";

    }

    // mysql type to java type
    static String getName(String name){

        while (true){
            int index = name.indexOf("_");
            if(index >=0 ){
                name = name.substring(0,index)+ name.substring(index+1,index+2).toUpperCase() +name.substring(index+2,name.length() );
            }else{
                return name;
            }
        }
    }

    static String getMethod(String name){

        name = getName(name);
        name = name.substring(0,1).toUpperCase() + name.substring(1,name.length() );
        return name;
    }

    static String getClass(String name){

        int index = name.indexOf("_");
        if(index >= 0){
            name = name.substring(index+1,name.length());
        }
        name = name.substring(0,1).toUpperCase() + name.substring(1,name.length() );

        return name;
    }
    static String getClassLow(String name){

        int index = name.indexOf("_");
        if(index >= 0){
            name = name.substring(index+1,name.length());
        }

        return name;
    }



}
