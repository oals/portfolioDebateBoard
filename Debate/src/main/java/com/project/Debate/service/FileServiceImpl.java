package com.project.Debate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileServiceImpl implements FileService{


    @Override
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileDate) {

        UUID uuid = UUID.randomUUID();

        // .파일확장자 찾기
//        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid + originalFileName; // 난수에 확장자 붙이기
        String fileUploadFullUrl = uploadPath + "/"+ savedFileName;

        String fileUrl = fileUploadFullUrl.replace("c:/Debate/Board/","/images/");


        // 결과 : 업로드할 경로 + / + uuid값 + .파일확장자

        try {
            FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
            fos.write(fileDate);
            fos.close();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //업로드 완료
        return fileUrl;


    }

    @Override
    public void deleteFolder(String delPath) {


        File delFolder = new File(delPath);
        String[] filenames = delFolder.list();

            if(filenames != null) {
                if (filenames.length != 0) {
                    for (int i = 0; i < filenames.length; i++) {

                        File deleteFile = new File(delFolder + "/" + filenames[i]);
                        log.info("삭제할 폴더의 하위 파일 : " + deleteFile.toString());
                        deleteFile.delete();
                    }
                }
            }
        delFolder.delete();





    }

    @Override
    public void deleteFile(String delPath) {
        File delFile = new File(delPath);


        delFile.delete();







    }
}
