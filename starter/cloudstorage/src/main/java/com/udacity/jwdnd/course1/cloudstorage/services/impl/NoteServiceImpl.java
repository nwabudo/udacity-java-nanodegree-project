package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private NoteMapper noteMapper;

    public NoteServiceImpl(NoteMapper noteMapper){
        this.noteMapper = noteMapper;
    }

    @Override
    public Note getNote(int noteId) {
        return this.noteMapper.getNote(noteId);
    }

    @Override
    public List<Note> getNotesByUserId(int userId) {
        return this.noteMapper.getNotes(userId);
    }

    @Override
    public void removeNote(int noteId) {
        this.noteMapper.deleteNote(noteId);
    }

    @Override
    public int saveNote(NoteForm noteForm, int userId) {
        Note note = new Note();
        BeanUtils.copyProperties(noteForm, note);
        note.setUserId(userId);
        return this.noteMapper.insert(note);
    }

    @Override
    public int updateNote(NoteForm noteForm) {
        Note modNote = this.noteMapper.getNote(noteForm.getNoteId());
        modNote.setNoteTitle(noteForm.getNoteTitle());
        modNote.setNoteDescription(noteForm.getNoteDescription());
        return this.noteMapper.updateNote(modNote);
    }

    public NoteForm mapTo(Note note){
        NoteForm noteForm = new NoteForm();
        BeanUtils.copyProperties(note, noteForm);
        return noteForm;
    }

    public List<NoteForm> mapTo(List<Note> notes){
        List<NoteForm> noteFormList = notes.stream()
                      .map(n -> mapTo(n))
                      .collect(Collectors.toList());
        return noteFormList;
    }

    @Override
    public void deleteAll() {
        this.noteMapper.deleteAll();
    }
}
