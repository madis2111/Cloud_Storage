package myproject.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class NamedFile {

    @Id
    private String filename;
    @Column(name = "hash")
    private String hash;
    @Column(name = "file")
    private String file;

    public NamedFile() {}

    public NamedFile(File file, String filename) {
        this.hash = file.getHash();
        this.file = file.getFile();
        this.filename = filename;
    }

    public NamedFile(String hash, String file, String filename) {
        this.hash = hash;
        this.file = file;
        this.filename = filename;
    }

}
