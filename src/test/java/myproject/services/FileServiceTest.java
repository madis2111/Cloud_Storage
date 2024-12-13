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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


public class FileServiceTest {

    @Test
    public void getFileTest() throws NoSuchFilenameException, CRUDException {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);

        byte[] mockFileAsBytes = {3, 90, -128, 6, 0, 127};
        NamedFile namedFile = new NamedFile("mockHash", mockFileAsBytes, "myfilename.txt");

        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Mockito.when(fileRepository.findById("myfilename.txt")).thenReturn(Optional.of(namedFile));

        HttpEntity httpEntity = (HttpEntity) fileService.getFile("myfilename.txt").getFirst("file");
        byte[] receivedBytes = (byte[]) httpEntity.getBody();

        Assertions.assertEquals(namedFile.getFileContent(), receivedBytes);
    }

    @Test
    public void uploadFileWithWrongFilenameTest() {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Assertions.assertThrows(FileExistsException.class, () ->
                fileService.uploadFile("mockHash", multipartFile, "myfilename.txt"));

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
    public void uploadFileTest() throws IOException {
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.FALSE);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        byte[] mockFileAsBytes = {3, 90, -128, 6, 0, 127};
        Mockito.when(multipartFile.getBytes()).thenReturn(mockFileAsBytes);

        Assertions.assertDoesNotThrow(() ->
                fileService.uploadFile("mockHash", multipartFile, "myfilename.txt"));
    }

    @Test       // проверяем что нет исключения
    public void putFileTest() throws IOException {
        byte[] mockFileAsBytes = {3, 90, -128, 6, 0, 127};

        NamedFile namedFile = new NamedFile("mockHash", mockFileAsBytes, "myfilename.txt");
        FileRepository fileRepository = Mockito.mock(FileRepository.class);
        FileService fileService = new FileService(fileRepository);
        Mockito.when(fileRepository.existsById("myfilename.txt")).thenReturn(Boolean.TRUE);
        Mockito.when(fileRepository.findById("myfilename.txt")).thenReturn(Optional.of(namedFile));

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getBytes()).thenReturn(mockFileAsBytes);

        Assertions.assertDoesNotThrow(() ->
                fileService.putFile("mockHash", multipartFile, "myfilename.txt"));
    }
}