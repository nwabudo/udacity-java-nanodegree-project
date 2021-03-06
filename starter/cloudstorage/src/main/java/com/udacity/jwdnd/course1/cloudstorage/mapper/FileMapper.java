package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFile(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE filename = #{name}")
    File getFileByName(String name);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            " VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);

    //@Update("UPDATE FILES SET filename = #{fileName}, contenttype = #{contentType}, " +
    //        " filesize = #{fileSize}, filedata = #{fileData} WHERE fileId = #{fileId}")
    //int updateUser(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(Integer fileId);
}
