package ru.baskaeva.cloudstorage.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.baskaeva.cloudstorage.errors.Error;
import ru.baskaeva.cloudstorage.models.UserFile;
import ru.baskaeva.cloudstorage.repositories.DAOUserFileRepository;

@Slf4j
@RestController
@RequestMapping("/")
public class FileController {

    @Autowired
    private DAOUserFileRepository repository;


    @GetMapping("/file")
    public void fileGet(@RequestParam String url){
        repository.fileGet(url);
    }

    @ModelAttribute(name = "file")
    public UserFile getFile() {
        return new UserFile();
    }

    @PostMapping("/file")
    public ResponseEntity<Error> filePost(@ModelAttribute UserFile file) {
        log.info("Hash: {}", file.getHash());
        log.info("File: {}", file.getFile());
        log.info("FileName: {}", file.getFileName());

        try {
            repository.filePost(file);
        } catch (Exception ex) {
            return new ResponseEntity<>(new Error("Error input data", 0), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/file")
    public void fileDelete(@RequestParam String url){
        repository.fileDelete(url);
    }

    @PutMapping ("/file")
    public void filePut(@RequestParam String fileName){
        repository.filePut(fileName);
    }

    @GetMapping("/list")
    public void list(){

    }
}
