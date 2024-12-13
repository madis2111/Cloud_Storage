package myproject.controllers;

import myproject.entities.Error;
import myproject.exceptions.CRUDException;
import myproject.exceptions.FileExistsException;
import myproject.exceptions.InputDataException;
import myproject.exceptions.NoSuchFilenameException;
import myproject.services.JwtService;
import myproject.services.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("file")
    public ResponseEntity<?> uploadFile(@RequestPart("hash") String hash, @RequestPart("file") MultipartFile file, @RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {

        if (!JwtService.tokenValid(token)) {
            return new ResponseEntity<>(new Error("Incorrect token", 401), HttpStatus.UNAUTHORIZED);
        }

        try {
            fileService.uploadFile(hash, file, filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (FileExistsException | IOException e) {
            return new ResponseEntity<>(new Error("Error input data", 400), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("file")
    public ResponseEntity<?> getFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {

        if (!JwtService.tokenValid(token)) {
            return new ResponseEntity<>(new Error("Incorrect token", 401), HttpStatus.UNAUTHORIZED);
        }

        try {
            return new ResponseEntity<>(fileService.getFile(filename), HttpStatus.OK);
        } catch (NoSuchFilenameException e) {
            Error error = new Error("No such filename", 400);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (CRUDException e) {
            Error error = new Error("Failed to get file", 500);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename, @RequestHeader("auth-token") String token) {

        if (!JwtService.tokenValid(token)) {
            return new ResponseEntity<>(new Error("Incorrect token", 401), HttpStatus.UNAUTHORIZED);
        }

        try {
            fileService.deleteFile(filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchFilenameException e) {
            Error error = new Error("No such filename", 400);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (CRUDException e) {
            Error error = new Error("Failed to delete file", 500);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("file")
    public ResponseEntity<?> putFile(@RequestParam("filename") String filename, @RequestPart("hash") String hash, @RequestPart("file") MultipartFile file, @RequestHeader("auth-token") String token) {

        if (!JwtService.tokenValid(token)) {
            return new ResponseEntity<>(new Error("Incorrect token", 401), HttpStatus.UNAUTHORIZED);
        }

        try {
            fileService.putFile(hash, file, filename);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchFilenameException | IOException e) {
            Error error = new Error("Error input data", 400);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (CRUDException e) {
            Error error = new Error("Failed to edit file", 500);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("list")
    public ResponseEntity<?> getAll(@RequestParam("limit") int limit, @RequestHeader("auth-token") String token) {

        if (!JwtService.tokenValid(token)) {
            return new ResponseEntity<>(new Error("Incorrect token", 401), HttpStatus.UNAUTHORIZED);
        }

        try {
            return new ResponseEntity<>(fileService.getAllAsJsonList(limit), HttpStatus.OK);
        } catch (InputDataException e) {
            Error error = new Error("Input data exception", 400);
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (CRUDException e) {
            Error error = new Error("Error getting file list", 500);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}