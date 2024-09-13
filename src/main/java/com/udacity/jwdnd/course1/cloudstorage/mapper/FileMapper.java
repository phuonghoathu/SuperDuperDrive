package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT f.* FROM FILES f INNER JOIN USERS u ON f.userid = u.userid WHERE u.username = #{username}")
    List<File> getAllFileByUser(String username);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId} and userid = #{userId}")
    File getFileById(int fileId, int userId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName} and userid = #{userId}")
    File getFileByName(String fileName, int userId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId} and userid = #{userId}")
    int delete(int fileId, int userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(File file);


}
