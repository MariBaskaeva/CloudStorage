package ru.baskaeva.cloudstorage.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.baskaeva.cloudstorage.dto.FileDTO;
import ru.baskaeva.cloudstorage.dto.FilenameDTO;
import ru.baskaeva.cloudstorage.dto.FilenameSizeDTO;
import ru.baskaeva.cloudstorage.errors.Error;
import ru.baskaeva.cloudstorage.exceptions.FailedDeletingException;
import ru.baskaeva.cloudstorage.exceptions.FailedDownloadingException;
import ru.baskaeva.cloudstorage.exceptions.FailedSavingException;
import ru.baskaeva.cloudstorage.exceptions.IncorrectInputData;
import ru.baskaeva.cloudstorage.service.FileService;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "${frontend.endpoint}")
@RestController
public class FileController {
    private final FileService service;

    public FileController(FileService fileService) {
        service = fileService;
    }

    @GetMapping("/file")
    public ResponseEntity<?> fileGet(@RequestParam String filename, Principal principal) throws FailedDownloadingException, FileNotFoundException, IncorrectInputData {
        log.info(principal.getName());

        return ResponseEntity.ok(service.download(principal.getName(), filename));
    }

    @ModelAttribute(name = "fileDTO")
    public FileDTO getFile() {
        return new FileDTO();
    }

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.OK)
    public void filePost(@ModelAttribute FileDTO fileDTO, Principal principal) throws FailedSavingException, IncorrectInputData {
        log.info("{}", fileDTO.getFilename());

        service.upload(principal.getName(), fileDTO.getFilename(), fileDTO.getFile());
    }

    @DeleteMapping("/file")
    public ResponseEntity<Error> fileDelete(@RequestParam String filename, Principal principal) throws FailedDeletingException, IncorrectInputData, FileNotFoundException {
        service.delete(principal.getName(), filename);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/file")
    public ResponseEntity<?> filePut(@RequestParam("filename") String filename, @RequestBody FilenameDTO name, Principal principal) throws FileNotFoundException, IncorrectInputData {
        service.rename(principal.getName(), filename, name.getFilename());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public List<FilenameSizeDTO> getList(@RequestParam Integer limit, Principal principal) {
        log.info(principal.getName());

        return service.getList(principal.getName(), limit);
    }


}
