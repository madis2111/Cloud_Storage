package myproject.services;

import myproject.repositories.FileRepository;
import myproject.entities.File;
import myproject.entities.NamedFile;
import myproject.exceptions.CRUDException;
import myproject.exceptions.FileExistsException;
import myproject.exceptions.InputDataException;
import myproject.exceptions.NoSuchFilenameException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void uploadFile(File file, String filename) throws FileExistsException {

        if (fileRepository.existsById(filename)) {
            throw new FileExistsException();
        }

        NamedFile namedFile = new NamedFile(file, filename);
        fileRepository.save(namedFile);

    }

    public File getFile(String filename) throws NoSuchFilenameException, CRUDException {


        if (!fileRepository.existsById(filename)) {
            throw new NoSuchFilenameException();
        }

        Optional<NamedFile> namedFileOptional = fileRepository.findById(filename);

        if (namedFileOptional.isEmpty()) {
            throw new CRUDException();
        }

            NamedFile namedFile = namedFileOptional.get();

        File file = new File(namedFile.getHash(), namedFile.getFile());
        return file;
    }

    public void deleteFile(String filename) throws NoSuchFilenameException,
                                                    CRUDException {


        if (!fileRepository.existsById(filename)) {
            throw new NoSuchFilenameException();
        }

        fileRepository.deleteById(filename);

        if (fileRepository.existsById(filename)) {
            throw new CRUDException();
        }
    }

    public void putFile(File file, String filename) throws NoSuchFilenameException, CRUDException {

        if (!fileRepository.existsById(filename)) {
            throw new NoSuchFilenameException();
        }

        NamedFile namedFile = new NamedFile(file,filename);
        fileRepository.save(namedFile);

        Optional<NamedFile> savedFileOptional = fileRepository.findById(filename);
        if (savedFileOptional.isEmpty() ||
            !savedFileOptional.get().equals(namedFile)) {
            throw new CRUDException();
        }
    }

    public List<File> getAll(int limit) throws CRUDException, InputDataException {

        if (limit == 0) {
            throw new InputDataException();
        }

        Page<NamedFile> namedFilesPage = fileRepository.findAll(PageRequest.of(0,limit));
        List<NamedFile> namedFiles = namedFilesPage.getContent();
        List<File> files = namedFiles
                                .stream()
                                .map(namedFile -> new File(namedFile.getHash(), namedFile.getFile()))
                                .toList();

        if (files == null) {
            throw new CRUDException();
        }

        return files;
    }
}
