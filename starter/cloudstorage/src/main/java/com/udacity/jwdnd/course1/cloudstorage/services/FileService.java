package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    File getFile(int fileId);

    File getFileByName(String fileName);

    List<File> getFilesByUserId(int userId);

    void removeFile(int fileId);

    String uploadFile(MultipartFile file, int userId);
}
