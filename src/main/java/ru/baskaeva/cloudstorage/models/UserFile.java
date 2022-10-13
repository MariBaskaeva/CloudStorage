package ru.baskaeva.cloudstorage.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
public class UserFile {
    @Id
    private String hash;

    private String fileName;

    private String file;

    public UserFile(String hash, String file){
        this.hash = hash;
        this.file = file;
    }
}
