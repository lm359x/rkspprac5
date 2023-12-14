package com.example.rksp_prac5_application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        FileUploadDTO fileUploadDTO = new FileUploadDTO(file);
        fileService.save(fileUploadDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FileInfoDTO>> getAllFiles() {
        List<FileInfoDTO> fileInfoDTOList = fileService.getSentFiles();
        return new ResponseEntity<>(fileInfoDTOList, HttpStatus.OK);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<?> getFile(@PathVariable String filename) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
