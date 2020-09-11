package com.centit.fileserver.dao;

import com.centit.fileserver.po.FileInfo;
import com.centit.fileserver.po.FileShowInfo;
import com.centit.fileserver.service.LocalFileManager;
import com.centit.framework.core.dao.CodeBook;
import com.centit.framework.jdbc.dao.BaseDaoImpl;
import com.centit.framework.jdbc.dao.DatabaseOptUtils;
import com.centit.support.algorithm.CollectionsOpt;
import com.centit.support.algorithm.NumberBaseOpt;
import com.centit.support.algorithm.StringBaseOpt;
import com.centit.support.database.utils.DBType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class FileInfoDao extends BaseDaoImpl<FileInfo, String> {

    @Override
    public Map<String, String> getFilterField() {
        Map<String, String> filterField = new HashMap<>();

        filterField.put("groupId" , CodeBook.EQUAL_HQL_ID);
        filterField.put("isValid" , CodeBook.EQUAL_HQL_ID);
        filterField.put("files", " fileId in (?) ");
        filterField.put("fileName",CodeBook.EQUAL_HQL_ID);
        filterField.put("fileShowPath",CodeBook.EQUAL_HQL_ID);
        filterField.put("fileState",CodeBook.EQUAL_HQL_ID);

        return filterField;
    }

    private static String trimFilePath(String filePath){
        if(StringUtils.isBlank(filePath)) {
            return "";
        }
        String tfp = filePath.trim();
        if(tfp.endsWith("/") || tfp.endsWith("\\")) {
            return tfp.substring(0,tfp.length()-1);
        }
        return tfp;
    }

    //subStr('你好.hell0.world,', length('你好.')+1, instr( subStr('你好.hell0.world,',length('你好.')+1),'.')-1)
    public Set<String> listUserDirectories(String userCode, String fileShowPath) {
        //StringUtils.indexOf(DatabaseOptUtils.getDialectName(),"Oracle")>=0
        //这个地方需要根据不同的数据库编写不同的sql语句
        Set<String> dirs = new HashSet<>();
        DBType dbt = DBType.mapDBType(this.getConnection());
        List<?> objects = null;
        if (StringUtils.isBlank(fileShowPath)) {
            String sqlsenOralce = "select distinct subStr( CONCAT(FILE_SHOW_PATH,'/'), 1,instr( CONCAT(FILE_SHOW_PATH,'/'),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_OWNER = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH is not null and FILE_SHOW_PATH<>'/'";
            String sqlsenMysql= "select distinct subString( CONCAT(FILE_SHOW_PATH,'/'), 1,instr( CONCAT(FILE_SHOW_PATH,'/'),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_OWNER = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH is not null and FILE_SHOW_PATH<>'' and FILE_SHOW_PATH<>'/'";
            objects = DatabaseOptUtils.listObjectsByNamedSql(this,
                    dbt==DBType.MySql?sqlsenMysql:sqlsenOralce,
                    CollectionsOpt.createHashMap("uc",userCode));
        } else {
            String fsp = trimFilePath(fileShowPath)+ LocalFileManager.FILE_PATH_SPLIT;
            String sqlsenOralce = "select distinct subStr(CONCAT(FILE_SHOW_PATH,'/'), length(:fsp)+1, " +
                    "instr( subStr(CONCAT(FILE_SHOW_PATH,'/'),length(:fsp)+1),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_OWNER = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH like :fspmatch";
            String sqlsenMysql = "select distinct subString(CONCAT(FILE_SHOW_PATH,'/'), length(:fsp)+1, " +
                    "instr( subString(CONCAT(FILE_SHOW_PATH,'/'),length(:fsp)+1),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_OWNER = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH like :fspmatch";
            objects = DatabaseOptUtils.listObjectsByNamedSql(this,
                    dbt==DBType.MySql?sqlsenMysql:sqlsenOralce,
                    CollectionsOpt.createHashMap(
                            "fsp",fsp,//".",
                            "uc",userCode,
                            "fspmatch",fsp+"%"));

        }
        if(objects !=null){
            for(Object obj:objects){
                String sd =StringBaseOpt.objectToString(obj);
                if(StringUtils.isNotBlank(sd)) {
                    dirs.add(sd);
                }
            }
        }
        return dirs;
    }

    //subStr('你好.hell0.world,', length('你好.')+1, instr( subStr('你好.hell0.world,',length('你好.')+1),'.')-1)
    public Set<String> listUnitDirectories(String unitCode, String fileShowPath) {
        //StringUtils.indexOf(DatabaseOptUtils.getDialectName(),"Oracle")>=0
        //这个地方需要根据不同的数据库编写不同的sql语句
        Set<String> dirs = new HashSet<>();
        DBType dbt = DBType.mapDBType(this.getConnection());
        List<?> objects = null;
        if (StringUtils.isBlank(fileShowPath)) {
            String sqlsenOralce = "select distinct subStr(CONCAT(FILE_SHOW_PATH,'/'), 1,instr( CONCAT(FILE_SHOW_PATH,'/'),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH is not null and FILE_SHOW_PATH<>'/'";
            String sqlsenMysql = "select distinct subString(CONCAT(FILE_SHOW_PATH,'/'), 1,instr( CONCAT(FILE_SHOW_PATH,'/'),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH is not null and FILE_SHOW_PATH<>'' and FILE_SHOW_PATH<>'/'";
            objects = DatabaseOptUtils.listObjectsByNamedSql(this,
                    dbt==DBType.MySql?sqlsenMysql:sqlsenOralce,
                    CollectionsOpt.createHashMap("uc",unitCode));
        } else {
            String fsp = trimFilePath(fileShowPath)+ LocalFileManager.FILE_PATH_SPLIT;
            String sqlsenOralce = "select distinct subStr(CONCAT(FILE_SHOW_PATH,'/'), length(:fsp)+1, " +
                    "instr( subStr(CONCAT(FILE_SHOW_PATH,'/'),length(:fsp)+1),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH like :fspmatch";
            String sqlsenMysql = "select distinct subString(CONCAT(FILE_SHOW_PATH,'/'), length(:fsp)+1, " +
                    "instr( subString(CONCAT(FILE_SHOW_PATH,'/'),length(:fsp)+1),'/')-1) " +
                    "from FILE_INFO " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH like :fspmatch";
            objects = DatabaseOptUtils.listObjectsByNamedSql(this,
                    dbt==DBType.MySql?sqlsenMysql:sqlsenOralce,
                    CollectionsOpt.createHashMap(
                            "fsp",fsp,// ".",
                            "uc",unitCode,
                            "fspmatch",fsp+"%"));
        }

        if(objects !=null){
            for(Object obj:objects){
                String sd =StringBaseOpt.objectToString(obj);
                if(StringUtils.isNotBlank(sd)) {
                    dirs.add(sd);
                }
            }
        }
        return dirs;
    }
    public List<FileShowInfo> listFolderFiles(Map<String, Object> searchColumn){
        String sqlsen = "select a.FILE_NAME, max(a.FILE_ID) as FILE_ID, " +
            "count(1) as FILE_SUM, min(a.ENCRYPT_TYPE) as ENCRYPT_TYPE, " +
            "max(a.CREATE_TIME) as CREATE_TIME, max(b.FILE_SIZE) as FILE_SIZE,"+
            "max(a.file_show_path) as file_show_path,max(c.favorite_id) as favorite_id,max(a.file_type) fileType,max(a.download_times) downloadTimes " +
            "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 "+
            "left join file_favorite c on a.file_id=c.file_id and c.favorite_user=:favoriteUser " +
            "where file_state='N' and parent_folder=:parentFolder and library_id=:libraryId " +
            "group by FILE_NAME";
        List<Object[]> objects =  DatabaseOptUtils.listObjectsByNamedSql(this,
            sqlsen, searchColumn);
        List<FileShowInfo> files = new ArrayList<>();
        if(objects !=null){
            for(Object[] objs:objects){
                FileShowInfo file = new FileShowInfo();
                file.setCatalogType("p");
                file.setFileType(StringBaseOpt.objectToString(objs[8]));
                file.setFileName(StringBaseOpt.objectToString(objs[0]));
                file.setAccessToken(StringBaseOpt.objectToString(objs[1]));
                file.setVersions(NumberBaseOpt.castObjectToInteger(objs[2]));
                file.setEncrypt(StringUtils.equals(StringBaseOpt.objectToString(objs[3]),"D"));
                if(objs[4] instanceof java.util.Date ) {
                    file.setCreateTime((Date)objs[4]);
                }
                file.setFileSize(NumberBaseOpt.castObjectToLong(objs[5]));
                file.setFileShowPath(StringBaseOpt.objectToString(objs[6]));
                file.setFavoriteId(StringBaseOpt.objectToString(objs[7]));
                file.setDownloadTimes(NumberBaseOpt.castObjectToInteger(objs[9]));
                files.add(file);
            }
        }
        return files;

    }
    public List<FileShowInfo> listUserFiles(String userCode, String fileShowPath) {
        List<Object[]> objects = null;
        if (StringUtils.isBlank(fileShowPath) || StringUtils.equals(fileShowPath,".")) {
            String sqlsen = "select a.FILE_NAME, max(a.FILE_ID) as FILE_ID, " +
                    "count(1) as FILE_SUM, min(a.ENCRYPT_TYPE) as ENCRYPT_TYPE, " +
                    "max(a.CREATE_TIME) as CREATE_TIME, max(b.FILE_SIZE) as FILE_SIZE,max(a.file_type) fileType  " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                    "where FILE_OWNER = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and (FILE_SHOW_PATH is null or FILE_SHOW_PATH='' or FILE_SHOW_PATH='/') " +
                    "group by FILE_NAME";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "uc",userCode));
        }else{
            String fsp = trimFilePath(fileShowPath);//+ LocalFileManager.FILE_PATH_SPLIT;
            String sqlsen = "select a.FILE_NAME, max(a.FILE_ID) as FILE_ID, " +
                    "count(1) as FILE_SUM, min(a.ENCRYPT_TYPE) as ENCRYPT_TYPE, " +
                    "max(a.CREATE_TIME) as CREATE_TIME, max(b.FILE_SIZE) as FILE_SIZE,max(a.file_type) fileType  " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                    "where FILE_OWNER = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and (FILE_SHOW_PATH=:fsp or FILE_SHOW_PATH=:fsp2) " +
                    "group by FILE_NAME";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "uc",userCode,
                            "fsp",fsp,
                            "fsp2",fsp + LocalFileManager.FILE_PATH_SPLIT));
        }
        List<FileShowInfo> files = new ArrayList<>();
        if(objects !=null){
            for(Object[] objs:objects){
                FileShowInfo file = new FileShowInfo();
                file.setFileShowPath(fileShowPath);
                file.setCatalogType("p");
                file.setFileType(StringBaseOpt.objectToString(objs[6]));
                file.setFileName(StringBaseOpt.objectToString(objs[0]));
                file.setAccessToken(StringBaseOpt.objectToString(objs[1]));
                file.setVersions(NumberBaseOpt.castObjectToInteger(objs[2]));
                file.setEncrypt(StringUtils.equals(StringBaseOpt.objectToString(objs[3]),"D"));
                if(objs[4] instanceof java.util.Date ) {
                    file.setCreateTime((Date)objs[4]);
                }
                file.setFileSize(NumberBaseOpt.castObjectToLong(objs[5]));

                files.add(file);
            }
        }
        return files;
    }

    public List<FileShowInfo> listUnitFiles(String unitCode, String fileShowPath) {
        List<Object[]> objects = null;
        if (StringUtils.isBlank(fileShowPath) || StringUtils.equals(fileShowPath,"/")) {
            String sqlsen = "select a.FILE_NAME, max(a.FILE_ID) as FILE_ID ," +
                    "count(1) as FILE_SUM, min(a.ENCRYPT_TYPE) as ENCRYPT_TYPE, " +
                    "max(a.CREATE_TIME) as CREATE_TIME, max(b.FILE_SIZE) as FILE_SIZE " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and (FILE_SHOW_PATH is null or FILE_SHOW_PATH='' or FILE_SHOW_PATH='/') " +
                    "group by FILE_NAME";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "uc",unitCode));
        }else{
            String fsp = trimFilePath(fileShowPath);//+ LocalFileManager.FILE_PATH_SPLIT;
            String sqlsen = "select a.FILE_NAME, max(a.FILE_ID) as FILE_ID ," +
                    "count(1) as FILE_SUM, min(a.ENCRYPT_TYPE) as ENCRYPT_TYPE, " +
                    "max(a.CREATE_TIME) as CREATE_TIME, max(b.FILE_SIZE) as FILE_SIZE " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and (FILE_SHOW_PATH=:fsp or FILE_SHOW_PATH=:fsp2) " +
                    "group by FILE_NAME";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "uc",unitCode,
                            "fsp",fsp,
                            "fsp2",fsp + LocalFileManager.FILE_PATH_SPLIT));
        }
        List<FileShowInfo> files = new ArrayList<>();
        if(objects !=null){
            for(Object[] objs:objects){
                FileShowInfo file = new FileShowInfo();
                file.setFileShowPath(fileShowPath);
                file.setCatalogType("d");
                file.setFileType("f");
                file.setFileName(StringBaseOpt.objectToString(objs[0]));
                file.setAccessToken(StringBaseOpt.objectToString(objs[1]));
                file.setVersions(NumberBaseOpt.castObjectToInteger(objs[2]));
                file.setEncrypt(StringUtils.equals(StringBaseOpt.objectToString(objs[3]),"D"));
                if(objs[4] instanceof java.util.Date ) {
                    file.setCreateTime((Date)objs[4]);
                }
                file.setFileSize(NumberBaseOpt.castObjectToLong(objs[5]));

                files.add(file);
            }
        }
        return files;
    }

    public List<FileShowInfo> listUserFileVersions(String userCode, String fileShowPath,String fileName) {
        List<Object[]> objects = null;
        if (StringUtils.isBlank(fileShowPath) || StringUtils.equals(fileShowPath,".")) {
            String sqlsen = "select a.FILE_ID, a.ENCRYPT_TYPE, a.CREATE_TIME, b.FILE_SIZE,c.favorite_id,a.file_type,a.download_times " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                "left join file_favorite c on a.file_id=c.file_id and c.favorite_user=:favoriteUser " +
                "where FILE_OWNER = :uc and file_state='N' " +
                    "and (FILE_SHOW_PATH is null or FILE_SHOW_PATH='' or FILE_SHOW_PATH='/') " +
                    "and FILE_NAME=:fn";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "favoriteUser",userCode,
                            "fn",fileName));
        }else{
            fileShowPath="/"+fileShowPath;
            String sqlsen = "select a.FILE_ID, a.ENCRYPT_TYPE, a.CREATE_TIME, b.FILE_SIZE,c.favorite_id,a.file_type,a.download_times " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                "left join file_favorite c on a.file_id=c.file_id and c.favorite_user=:favoriteUser " +
                "where  file_state='N' " +
                    "and FILE_SHOW_PATH=:fsp " +
                    "and FILE_NAME=:fn";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "favoriteUser",userCode,
                            "fsp",fileShowPath,
                            "fn",fileName));
        }
        List<FileShowInfo> files = new ArrayList<>();
        if(objects !=null){
            for(Object[] objs:objects){
                FileShowInfo file = new FileShowInfo();
                file.setFileShowPath(fileShowPath);
                file.setCatalogType("p");
                file.setFileType(StringBaseOpt.objectToString(objs[5]));
                file.setFileName(fileName);
                file.setAccessToken(StringBaseOpt.objectToString(objs[0]));
                file.setVersions(1);
                file.setEncrypt(StringUtils.equals(StringBaseOpt.objectToString(objs[1]),"D"));
                if(objs[2] instanceof java.util.Date ) {
                    file.setCreateTime((Date)objs[2]);
                }
                file.setFileSize(NumberBaseOpt.castObjectToLong(objs[3]));
                file.setFavoriteId(StringBaseOpt.objectToString(objs[4]));
                file.setDownloadTimes(NumberBaseOpt.castObjectToInteger(objs[6]));
                files.add(file);
            }
        }
        return files;
    }

    public List<FileShowInfo> listUnitFileVersions(String unitCode, String fileShowPath,String fileName) {
        List<Object[]> objects = null;
        if (StringUtils.isBlank(fileShowPath) || StringUtils.equals(fileShowPath,".")) {
            String sqlsen = "select a.FILE_ID, a.ENCRYPT_TYPE, a.CREATE_TIME, b.FILE_SIZE " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and (FILE_SHOW_PATH is null or FILE_SHOW_PATH='' or FILE_SHOW_PATH='/') " +
                    "and FILE_NAME=:fn and FILE_STATE='A'";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "uc",unitCode,
                            "fn",fileName));
        }else{
            String sqlsen = "select a.FILE_ID, a.ENCRYPT_TYPE, a.CREATE_TIME, b.FILE_SIZE " +
                    "from FILE_INFO a join FILE_STORE_INFO b on a.FILE_MD5=b.FILE_MD5 " +
                    "where FILE_UNIT = :uc and OS_ID='FILE_SVR' and OPT_ID='LOCAL_FILE' " +
                    "and FILE_SHOW_PATH=:fsp " +
                    "and FILE_NAME=:fn and FILE_STATE='A'";
            objects = (List<Object[]>)DatabaseOptUtils.listObjectsByNamedSql(this,
                    sqlsen, CollectionsOpt.createHashMap(
                            "uc",unitCode,
                            "fsp",fileShowPath,
                            "fn",fileName));
        }


        List<FileShowInfo> files = new ArrayList<>();
        if(objects !=null){
            for(Object[] objs:objects){
                FileShowInfo file = new FileShowInfo();
                file.setFileShowPath(fileShowPath);
                file.setCatalogType("d");
                file.setFileType("f");
                file.setFileName(fileName);
                file.setAccessToken(StringBaseOpt.objectToString(objs[0]));
                file.setVersions(1);
                file.setEncrypt(StringUtils.equals(StringBaseOpt.objectToString(objs[1]),"D"));
                if(objs[2] instanceof java.util.Date ) {
                    file.setCreateTime((Date)objs[2]);
                }
                file.setFileSize(NumberBaseOpt.castObjectToLong(objs[3]));

                files.add(file);
            }
        }
        return files;
    }

    public List<FileInfo> listFileStoreInfo(String fileShowPath,String fileName) {
        List<FileInfo> objects = null;
        if (StringUtils.isBlank(fileShowPath) || StringUtils.equals(fileShowPath,".")) {
            String hqlsen =  "where (FILE_SHOW_PATH is null or FILE_SHOW_PATH='' or FILE_SHOW_PATH='/') " +
                    "and FILE_NAME = ?";
            objects =  this.listObjectsByFilter(hqlsen,new Object[]{fileName});
        }else{
            String hqlsen = "where  FILE_SHOW_PATH = ? and FILE_NAME = ?";
            objects = this.listObjectsByFilter(hqlsen,new Object[]{fileShowPath,fileName});
        }
        return objects;
    }
}

