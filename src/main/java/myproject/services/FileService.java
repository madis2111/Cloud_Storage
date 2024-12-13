package myproject.services;

import com.google.gson.Gson;
import myproject.entities.FileInfo;
import myproject.repositories.FileRepository;
import myproject.entities.NamedFile;
import myproject.exceptions.CRUDException;
import myproject.exceptions.FileExistsException;
import myproject.exceptions.InputDataException;
import myproject.exceptions.NoSuchFilenameException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public MultiValueMap getFile(String filename) throws NoSuchFilenameException, CRUDException {


        if (!fileRepository.existsById(filename)) {
            throw new NoSuchFilenameException();
        }

        Optional<NamedFile> namedFileOptional = fileRepository.findById(filename);

        if (namedFileOptional.isEmpty()) {
            throw new CRUDException();
        }

        NamedFile namedFile = namedFileOptional.get();

        MultipartBodyBuilder builder = new MultipartBodyBuilder();

        builder.part("metadata", String.format(
                "{ \"hash\": \"%s\" }",
                namedFile.getHash()
        )).header(HttpHeaders.CONTENT_TYPE, "application/json");

        builder.part("file", namedFile.getFileContent())
                .header(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=file");

        return builder.build();
    }


    public void deleteFile(String filename) throws NoSuchFilenameException, CRUDException {

        if (!fileRepository.existsById(filename)) {
            throw new NoSuchFilenameException();
        }

        fileRepository.deleteById(filename);

        if (fileRepository.existsById(filename)) {
            throw new CRUDException();
        }
    }

    public void uploadFile(String hash, MultipartFile multipartFile, String filename) throws FileExistsException, IOException {

        if (fileRepository.existsById(filename)) {
            throw new FileExistsException();
        }

        NamedFile namedFile = new NamedFile(hash, multipartFile.getBytes(), filename);
        fileRepository.save(namedFile);

    }

    public void putFile(String hash, MultipartFile multipartFile, String filename) throws NoSuchFilenameException, CRUDException, IOException {

        if (!fileRepository.existsById(filename)) {
            throw new NoSuchFilenameException();
        }

        NamedFile namedFile = new NamedFile(hash, multipartFile.getBytes(), filename);
        fileRepository.save(namedFile);

        Optional<NamedFile> savedFileOptional = fileRepository.findById(filename);
        if (savedFileOptional.isEmpty() ||
                !savedFileOptional.get().equals(namedFile)) {
            throw new CRUDException();
        }
    }

    public String getAllAsJsonList(int limit) throws CRUDException, InputDataException {

        if (limit == 0) {
            throw new InputDataException();
        }

        Page<NamedFile> namedFilesPage = fileRepository.findAll(PageRequest.of(0, limit));
        List<NamedFile> namedFiles = namedFilesPage.getContent();
        List<FileInfo> fileInfos = namedFiles.stream()
                .map(namedFile -> namedFile.getFileInfo())
                .toList();


        return new Gson().toJson(fileInfos);
    }
}