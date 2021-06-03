package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;

import java.util.List;

public interface NoteService {

    Note getNote(int noteId);

    List<Note> getNotesByUserId(int userId);

    void removeNote(int noteId);

    int saveNote(NoteForm noteForm, int userId);

    int updateNote(NoteForm noteForm);

    List<NoteForm> mapTo(List<Note> notes);
}
