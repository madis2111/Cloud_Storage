package myproject.services;

import myproject.repositories.FileRepository;
import myproject.entities.File;
import myproject.entities.NamedFile;
import myproject.exceptions.CRUDException;
import myproject.exceptions.FileExistsException;
import myproject.exceptions.NoSuchFilenameException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;


public class FileServiceTest {

    @Test
    public void getFileTest() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);

        File file = new File("123","1010");
        NamedFile namedFile = new NamedFile(file,"myfilename.txt");

        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Mockito.when(fileRepository.findById("myfilename.txt")).thenReturn(Optional.of(namedFile));


        try {
            Assertions.assertEquals(file, fileService.getFile("myfilename.txt"));
        } catch (NoSuchFilenameException e) {
            e.printStackTrace();
        } catch (CRUDException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getFileWithWrongFilenameTest() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Assertions.assertThrows(FileExistsException.class, () ->
                fileService.uploadFile(new File("123","1010"),"myfilename.txt"));

    }

    @Test
    public void getFileCRUDErrorTest() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Mockito.when(fileRepository.findById("myfilename.txt")).thenReturn(Optional.ofNullable(null));
        Assertions.assertThrows(CRUDException.class, () ->
                fileService.getFile("myfilename.txt"));
    }

    @Test       // проверяем что нет исключения
    public void uploadFileTest() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.FALSE);
        Assertions.assertDoesNotThrow(() ->
                fileService.uploadFile(new File("123", "1010"), "myfilename.txt"));
    }

    @Test       // проверяем что нет исключения
    public void putFileTest() {
        File file = new File("123", "1010");
        NamedFile namedFile = new NamedFile(file,"myfilename.txt");
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Mockito.when(fileRepository.findById("myfilename.txt")).thenReturn(Optional.of(namedFile));
        Assertions.assertDoesNotThrow(() ->
                fileService.putFile(file, "myfilename.txt"));
    }
}
