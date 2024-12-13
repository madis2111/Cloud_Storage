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
    private byte[] file;

    public NamedFile() {}

    public NamedFile(String hash, byte[] fileBytes, String filename) {
        this.hash = hash;
        this.file = fileBytes;
        this.filename = filename;
    }

    public byte[] getFileContent() {
        return file;
    }

    public FileInfo getFileInfo() {
        return new FileInfo(filename, file.length);
    }
}