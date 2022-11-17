package ru.baskaeva.cloudstorage.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.baskaeva.cloudstorage.dto.FilenameSizeDTO;
import ru.baskaeva.cloudstorage.exceptions.FailedDeletingException;
import ru.baskaeva.cloudstorage.exceptions.FailedDownloadingException;
import ru.baskaeva.cloudstorage.exceptions.FailedSavingException;
import ru.baskaeva.cloudstorage.exceptions.IncorrectInputData;
import ru.baskaeva.cloudstorage.models.File;
import ru.baskaeva.cloudstorage.repositories.FileRepository;
import ru.baskaeva.cloudstorage.repositories.UserRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${storage.path}")
    private String PATH;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void upload(String login, String filename, MultipartFile data) throws UsernameNotFoundException, FailedSavingException, IncorrectInputData {
        if(fileRepository.existsByUser_LoginAndFilename(login, filename))
            throw new IncorrectInputData();
        //сохранение файла в базу данных
        log.info(PATH);
        File file;
        try {
            file = fileRepository.save(new File(null, filename, data.getSize(), userRepository.findByLogin(login)));
        } catch (Exception ex) {
            throw new IncorrectInputData();
        }
        //сохранение файла локально
        try {
            Files.createDirectories(Paths.get(PATH, login));
            Path path = Paths.get(PATH, login, file.getId().toString());
            Files.write(path, data.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new FailedSavingException();
        }
    }

    @Override
    public void delete(String login, String filename) throws FailedDeletingException, IncorrectInputData {
        Optional<File> optionalFile = fileRepository.getByUser_LoginAndFilename(login, filename);
        log.info("{}, {}, {}, {}", login, filename, optionalFile, optionalFile.isPresent());

        if (optionalFile.isEmpty())
            throw new IncorrectInputData();

        File file = optionalFile.get();
        //удаление файла локально
        try {
            Files.delete(Path.of(PATH, login, file.getId().toString()));
        } catch (IOException e) {
            throw new FailedDeletingException();
        }

        //удаление файла из базы данных
        fileRepository.delete(file);
    }

    @Transactional
    @Override
    public void rename(String login, String filename, String newFilename) throws IncorrectInputData {
        Optional<File> optionalFile = fileRepository.getByUser_LoginAndFilename(login, filename);

        if (optionalFile.isEmpty())
            throw new IncorrectInputData();

        //переименование файла в базе данных
        fileRepository.updateFilename(login, filename, newFilename);
    }

    @Override
    public byte[] download(String login, String filename) throws FailedDownloadingException, IncorrectInputData {
        Optional<File> optionalFile = fileRepository.getByUser_LoginAndFilename(login, filename);
        if (optionalFile.isEmpty())
            throw new IncorrectInputData();

        File file = optionalFile.get();
        Path path = Paths.get(PATH, login, file.getId().toString());
        byte[] mas;

        try {
            mas = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FailedDownloadingException();
        }

        return mas;
    }

    @Override
    public List<FilenameSizeDTO> getList(String username, int limit) {
        return fileRepository.findFilenameAndSizeByUser_Login(username, Pageable.ofSize(limit));
    }
}
