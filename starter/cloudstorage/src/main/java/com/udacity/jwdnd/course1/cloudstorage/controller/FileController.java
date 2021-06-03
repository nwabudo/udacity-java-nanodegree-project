package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file")
@Slf4j
public class FileController {

    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @RequestMapping(value = "/files/add", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public String postNewFile(@RequestParam("fileUpload") MultipartFile file, Model model,
                              Authentication authentication){
        if (file == null || file.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("errResponse", "File Cannot be null or empty");
        } else{
            User user = this.userService.getSignedInUser(authentication);
            boolean fileSaved = false;
            String fileUploadError = this.fileService.uploadFile(file, user.getUserId());
            log.info(fileUploadError);

            if(fileUploadError == null){
                fileSaved = true;
            }else {
                model.addAttribute("errResponse", fileUploadError);
            }
            model.addAttribute("success", fileSaved);
        }
        model.addAttribute("name", "/home#nav-files");
        return "result";
    }

    @RequestMapping(value="/files/download", method= RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@Param(value="id") Integer id) {
        File file = this.fileService.getFile(id);
        try{
            HttpHeaders header = new HttpHeaders();
            String[] mediaType = splitContentType(file.getContentType());
            header.setContentType(new MediaType(mediaType[0], mediaType[1]));
            header.setContentDispositionFormData(file.getFileName(), file.getFileName());
            header.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            ResponseEntity<byte[]> response = new ResponseEntity<>(file.getFileData(), header, HttpStatus.OK);
            return response;
        } catch (Exception ex) {
            log.info("Error writing file to output stream. Filename was '{}'", file.getFileName(), ex);
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @RequestMapping(value="/files/delete", method= RequestMethod.GET)
    public String deleteFile(@Param(value="id") Integer id,
                             Model model) {
        this.fileService.removeFile(id);
        model.addAttribute("success", true);
        model.addAttribute("name", "/home#nav-files");
        return "result";
    }

    private String[] splitContentType(String contentType){
        if(contentType == null || contentType.isEmpty() || !contentType.contains("/")) return null;
        return contentType.split("/");
    }
}
