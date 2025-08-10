package ca.robertgleason.ecommbe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // file names of current file
        String originalFilename = file.getOriginalFilename();
        // generate a unique file name (UUID)
        String randomID = UUID.randomUUID().toString();
        assert originalFilename != null;
        String fileName = randomID.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;
        // check if path exists and create
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        // upload to server
        Files.copy(file.getInputStream(), Paths.get(filePath));
        // returning the file name
        return fileName;
    }
}
