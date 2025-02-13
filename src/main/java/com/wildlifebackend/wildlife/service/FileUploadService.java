package com.wildlifebackend.wildlife.service;



import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {
    private final String UploadDir="upload/";

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("fill cannot be empty");
        }

        //ensure it is image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        //generate unique name
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + fileExtension;
        Path filePath = Paths.get(UploadDir, fileName);


        //create directory
        Files.createDirectories(filePath.getParent());

        //save file
        Files.write(filePath,file.getBytes());

        return fileName;
    }

    private String getFileExtension(String fileName){
        if(fileName==null||!fileName.contains(".")){
            throw new IllegalArgumentException("Invalid file name");
        }

        return fileName.substring(fileName.lastIndexOf(".")+1);
    }
}
