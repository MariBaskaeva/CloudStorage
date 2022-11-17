package ru.baskaeva.cloudstorage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilenameSizeDTO {
    private String filename;
    private Long size;
}
