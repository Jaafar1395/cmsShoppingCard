package com.jchaaban.cmsshoppingcard.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDirectory, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDirectory);

        if (!Files.exists(uploadPath))
            Files.createDirectories(uploadPath);

        try (InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception){
            throw new IOException("Could not save file " + fileName, exception);
        }
    }

    public static void cleanDirectory(String directory){
        Path directoryPath = Paths.get(directory);
        try {
            Files.list(directoryPath).forEach(file -> {
                if (!Files.isDirectory(file)){
                    try {
                        Files.delete(file);
                    } catch (IOException exception) {
                        System.out.println("Could not delete file: " + file);
                    }
                }
            });
        } catch (IOException exception) {
            System.out.println("Could not list the directory: " + directoryPath);
        }
    }

    public static void deleteDirectory(String directory){
        cleanDirectory(directory);
        try {
            File dir = new File(directory);
            dir.delete();
        } catch (Exception e) {
            System.out.println("Could not delete Directory: " + directory);
        }
    }






}
