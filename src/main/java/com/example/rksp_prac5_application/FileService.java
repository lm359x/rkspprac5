package com.example.rksp_prac5_application;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    @Value("${app.path.upload.file}")
    private String uploadPath;

    private SimpleDateFormat formatForDateNow = new SimpleDateFormat("hh:mm dd.MM.yyyy");

    @PostConstruct
    public void init() {
        try {
            log.info("init directory");
            Files.createDirectories(Paths.get(uploadPath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
        log.info("directory init finished");
        formatForDateNow.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public void save(FileUploadDTO fileUploadDTO) {
        MultipartFile file = fileUploadDTO.getFile();
            String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            saveFile(file, newFileName);
    }

    public List<FileInfoDTO> getSentFiles() {
        log.info("getting list of all files (getSentFiles)");
        List<FileInfoDTO> fileInfoDTOList = new ArrayList<>();
        List<File> uploadFileList = getUploadFilesFromFolder();

        for (File file : uploadFileList) {
            log.info(" file {}",file.getAbsolutePath());
            FileInfoDTO fileInfoDTO = new FileInfoDTO();

            fileInfoDTO.setName(file.getName());
            fileInfoDTO.setDate(formatForDateNow.format(new Date(file.lastModified())));
            fileInfoDTO.setUrl("/upload-files/" + file.getName());
            fileInfoDTOList.add(fileInfoDTO);
        }
        return fileInfoDTOList;
    }

    private void saveFile(MultipartFile file, String fileName) {
        log.info("saving file {}",fileName);
        try {
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
            }
            Files.copy(file.getInputStream(), root.resolve(fileName));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " +
                    e.getMessage());
        }
    }

    private List<File> getUploadFilesFromFolder() {
        log.info("getting files getUploadFilesFromFolder");
        List<File> files = new ArrayList<>();
        File folder = new File(uploadPath);
        for (final File fileEntry : folder.listFiles()) {
            files.add(fileEntry);
        }
        return files;
    }
}
