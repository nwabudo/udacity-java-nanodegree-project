package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/note")
public class NoteController {

    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @RequestMapping(value = "/notes/add", method = RequestMethod.POST)
    public String postNewNote(NoteForm note, Model model,
                              Authentication authentication){
        boolean noteSaved = false;
        String errorMsg = "Unable to Save Note";
        int success;
        if (note.getNoteId() != null) {
            success = this.noteService.updateNote(note);
        } else {
            User user = this.userService.getSignedInUser(authentication);
            success = this.noteService.saveNote(note, user.getUserId());
        }
        if(success > 0) noteSaved = true;

        model.addAttribute("success", noteSaved);
        model.addAttribute("errResponse", errorMsg);
        model.addAttribute("name", "/home#nav-notes");
        return "result";
    }

    @RequestMapping(value="/notes/delete", method= RequestMethod.GET)
    public String deleteNote(@Param(value="id") Integer id,
                             Model model) {
        this.noteService.removeNote(id);
        model.addAttribute("success", true);
        model.addAttribute("name", "/home#nav-notes");
        return "result";
    }


}
