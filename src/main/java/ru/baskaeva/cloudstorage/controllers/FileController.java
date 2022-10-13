package ru.baskaeva.cloudstorage.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.baskaeva.cloudstorage.errors.Error;
import ru.baskaeva.cloudstorage.models.File;
import ru.baskaeva.cloudstorage.models.UserFile;
import ru.baskaeva.cloudstorage.repositories.DAOUserFileRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/")
public class FileController {

    @Autowired
    private DAOUserFileRepository repository;


    @GetMapping("/file")
    public ResponseEntity<?> fileGet(@RequestParam String hash){
        Optional<UserFile> fileOptional = repository.fileGet(hash);

        if(fileOptional.isPresent()){
            UserFile file = fileOptional.get();
            return new ResponseEntity<>(new File(file.getHash() ,file.getFile()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Error("Error input data", 0), HttpStatus.BAD_REQUEST);
    }

    @ModelAttribute(name = "file")
    public UserFile getFile() {
        return new UserFile();
    }

    @PostMapping("/file")
    public ResponseEntity<Error> filePost(@ModelAttribute UserFile file) {
        log.info("File: {}", file);
        try {
            repository.filePost(file);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Error("Error input data", 0), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public ResponseEntity<Error> fileDelete(@RequestParam String fileName){
        try {
            repository.fileDelete(fileName);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Error("Error input data", 0), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping (value = "/file")
    public void filePut(@RequestParam String fileName, @RequestBody Map<String, String> name){
        repository.filePut(fileName, name.get("name"));
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam Integer limit){
        if(limit < 0)
            return new ResponseEntity<>(new Error("Error input data", 0), HttpStatus.BAD_REQUEST);

        List userFileList = repository.fileList(limit);
        return new ResponseEntity<>(userFileList, HttpStatus.OK);
    }
}
