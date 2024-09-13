package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NoteService {
    final NoteMapper noteMapper;

    public List<Note> getAllNoteByUsername(String username) {
        return noteMapper.getAllNoteByUser(username);
    }

    public int addNote(Note note) throws IOException {
        return noteMapper.insert(note);
    }

    public int updateNote(Note note) throws IOException {
        return noteMapper.update(note);
    }

    public int deleteNote(int noteId, int userId) {
        return noteMapper.delete(noteId, userId);
    }
}
