package com.project.Debate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
@RequiredArgsConstructor
@Log4j2
public class CreateDirectoryServiceImpl implements CreateDirectoryService{


    @Value("${itemImgLocation}")
    private String itemImgLocation;
    @Override
    public void CreateDebatePostImageFolder(String path) {



        File DebateFolder = new File(path);
        DebateFolder.mkdir(); //폴더 생성합니다.


    }
}
