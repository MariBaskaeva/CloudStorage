package ru.baskaeva.cloudstorage.controllers;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import ru.baskaeva.cloudstorage.dto.FilenameSizeDTO;
import ru.baskaeva.cloudstorage.exceptions.FailedDownloadingException;
import ru.baskaeva.cloudstorage.exceptions.IncorrectInputData;
import ru.baskaeva.cloudstorage.service.FileService;
import ru.baskaeva.cloudstorage.service.FileServiceImpl;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileControllerTest {
    private final FileService service = Mockito.mock(FileServiceImpl.class);
    private final FileController controller = new FileController(service);


    @Test
    void fileGetSuccess() throws FileNotFoundException, IncorrectInputData, FailedDownloadingException {
        Mockito.when(service.download("Mari", "filename"))
                .thenReturn("qwe".getBytes());

        assertEquals(ResponseEntity.ok("qwe".getBytes()),
                controller.fileGet("filename", () -> "Mari"));
    }

    @Test
    void fileGetIncorrectInputDataException() throws FileNotFoundException, IncorrectInputData, FailedDownloadingException {
        Mockito.when(service.download("Mari", "filename"))
                .thenThrow(IncorrectInputData.class);

        assertThrows(IncorrectInputData.class, () -> controller.fileGet("filename", () -> "Mari"));
    }

    @Test
    void fileGetFailedDownloadingException() throws FileNotFoundException, IncorrectInputData, FailedDownloadingException {
        Mockito.when(service.download("Mari", "filename"))
                .thenThrow(FailedDownloadingException.class);

        assertThrows(FailedDownloadingException.class, () -> controller.fileGet("filename", () -> "Mari"));
    }

    @Test
    void getListSuccess() {
        Mockito.when(service.getList("Mari", 2))
                .thenReturn(List.of(new FilenameSizeDTO("file1", 3L),
                        new FilenameSizeDTO("file2", 2L)));

        assertEquals(List.of(new FilenameSizeDTO("file1", 3L), new FilenameSizeDTO("file2", 2L)),
                controller.getList(2, () -> "Mari"));
    }
}