package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT c.* FROM CREDENTIALS c INNER JOIN USERS u ON c.userid = u.userid WHERE u.username = #{username}")
    List<Credential> getAllCredentialByUser(String username);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    Credential getCredentialById(int credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId} and userId = #{userId}")
    int delete(int credentialId, int userId);

    @Insert("INSERT INTO CREDENTIALS (url, username,key,password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insert(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialId} and userId = #{userId}")
    int update(Credential credential);


}
