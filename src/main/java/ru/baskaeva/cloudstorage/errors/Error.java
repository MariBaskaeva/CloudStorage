package ru.baskaeva.cloudstorage.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Error {
    private String message;
    private Integer id;
}
