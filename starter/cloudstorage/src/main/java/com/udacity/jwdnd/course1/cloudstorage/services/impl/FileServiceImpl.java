package com.udacity.jwdnd.course1.cloudstorage.services.impl;

import com.udacity.jwdnd.course1.cloudstorage.entity.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private FileMapper fileMapper;

    public FileServiceImpl(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    @Override
    public File getFile(int fileId) {
        return this.fileMapper.getFile(fileId);
    }

    @Override
    public File getFileByName(String fileName){
        return this.fileMapper.getFileByName(fileName);
    }

    @Override
    public List<File> getFilesByUserId(int userId) {
        return this.fileMapper.getFiles(userId);
    }

    @Override
    public void removeFile(int fileId) {
        this.fileMapper.deleteFile(fileId);
        log.info("File Deleted Successfully");
    }

    @Override
    public String uploadFile(MultipartFile file, int userId) {
        if(file == null) return "File cannot be empty or Null";

        File newFile = new File();
        File availFiles = this.fileMapper.getFileByName(file.getOriginalFilename());
        if(availFiles == null) try {
            newFile.setFileName(file.getOriginalFilename());
            newFile.setFileData(file.getBytes());
            newFile.setFileSize(String.valueOf(file.getSize()));
            newFile.setContentType(file.getContentType());
            newFile.setUserId(userId);
            log.info("Object Inserted");
            return this.fileMapper.insert(newFile) > 0 ? null : "File could not be saved";
        } catch (IOException e) {
            log.error(e.getMessage());
            return "error occurred during upload, check Logs";
        }
        return "File with Similar Name Exists";
    }
}
