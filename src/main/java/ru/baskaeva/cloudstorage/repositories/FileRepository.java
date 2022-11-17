package ru.baskaeva.cloudstorage.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.baskaeva.cloudstorage.dto.FilenameSizeDTO;
import ru.baskaeva.cloudstorage.models.File;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends PagingAndSortingRepository<File, Long> {
    Optional<File> getByUser_LoginAndFilename(String login, String filename);

    @Modifying
    @Query("update File f set f.filename = ?3 where f.filename = ?2 and f.user = (select u from User u where u.login = ?1)")
    void updateFilename(String login, String filename, String newFilename);

    List<FilenameSizeDTO> findFilenameAndSizeByUser_Login(String login, Pageable pageable);

    boolean existsByUser_LoginAndFilename(String login, String filename);
}