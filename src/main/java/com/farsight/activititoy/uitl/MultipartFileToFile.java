package com.farsight.activititoy.uitl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class MultipartFileToFile {
    public static File MultipartFileToFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        File file = null;
        try {
            file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);
            return file;
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}