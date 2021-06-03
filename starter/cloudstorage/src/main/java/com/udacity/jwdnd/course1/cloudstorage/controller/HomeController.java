package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;
import com.udacity.jwdnd.course1.cloudstorage.dto.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.dto.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.entity.Note;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
@Slf4j
public class HomeController {

    private UserService userService;
    private FileService fileService;
    private NoteService noteService;
    private CredentialService credService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credService = credService;
    }

    @GetMapping()
    public String loadHomePage(@ModelAttribute NoteForm note, Model model, Authentication authentication){
        User user = this.userService.getSignedInUser(authentication);
        List<FileForm> fileList = this.fileService.getFilesByUserId(user.getUserId()).stream()
                                      .map(f -> new FileForm(f.getFileId(), f.getFileName()))
                                      .collect(Collectors.toList());

//        List<NoteForm> noteList = this.noteService.getNotesByUserId(user.getUserId()).stream()
//                .map(n -> new NoteForm(n.getNoteId(), n.getNoteTitle(), n.getNoteDescription()))
//                .collect(Collectors.toList());

        List<Note> notes = this.noteService.getNotesByUserId(user.getUserId());
        List<NoteForm> noteList = this.noteService.mapTo(notes);

        List<Credential> credentials = this.credService.getCredentialsByUserId(user.getUserId());
        List<CredForm> creds = this.credService.mapTo(credentials);

        model.addAttribute("files", fileList);
        model.addAttribute("notes", noteList);
        model.addAttribute("creds", creds);
        return "home";
    }

}
