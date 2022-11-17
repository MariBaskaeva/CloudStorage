package ru.baskaeva.cloudstorage.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import ru.baskaeva.cloudstorage.dto.FilenameSizeDTO;
import ru.baskaeva.cloudstorage.exceptions.FailedDeletingException;
import ru.baskaeva.cloudstorage.exceptions.FailedDownloadingException;
import ru.baskaeva.cloudstorage.exceptions.FailedSavingException;
import ru.baskaeva.cloudstorage.exceptions.IncorrectInputData;

import java.io.FileNotFoundException;
import java.util.List;

public interface FileService {
    void upload(String username, String filename, MultipartFile file)
            throws UsernameNotFoundException, FailedSavingException, IncorrectInputData;

    void delete(String username, String filename) throws FileNotFoundException, FailedDeletingException, IncorrectInputData;

    void rename(String username, String filename, String newFilename) throws FileNotFoundException, IncorrectInputData;

    byte[] download(String username, String filename) throws FailedDownloadingException, FileNotFoundException, IncorrectInputData;

    List<FilenameSizeDTO> getList(String username, int limit);
}
