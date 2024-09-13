package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT n.* FROM NOTES n INNER JOIN USERS u ON n.userid = u.userid WHERE u.username = #{username}")
    List<Note> getAllNoteByUser(String username);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} and userId = #{userId}")
    int delete(int noteId, int userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription,userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insert(Note file);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId} and userId = #{userId}")
    int update(Note note);


}
