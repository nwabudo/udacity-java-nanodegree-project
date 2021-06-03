package com.udacity.jwdnd.course1.cloudstorage.dto;


public class FileForm {

    private Integer fileId;
    private String fileName;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileForm() {
    }

    public FileForm(Integer fileId, String fileName) {
        this.fileId = fileId;
        this.fileName = fileName;
    }
}
