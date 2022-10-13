package ru.baskaeva.cloudstorage.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class File {
    private String hash;
    private String file;
}
