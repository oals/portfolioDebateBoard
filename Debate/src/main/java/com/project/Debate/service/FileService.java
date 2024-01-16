package com.project.Debate.service;

public interface FileService {

    String uploadFile(String uploadPath,String originalFileName,byte[] fileDate);

    void deleteFolder(String delPath);


    void deleteFile(String delPath);





}
