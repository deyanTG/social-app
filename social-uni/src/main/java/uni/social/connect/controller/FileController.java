package uni.social.connect.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import uni.social.connect.exception.FileUploadException;
import uni.social.connect.service.FileService;

@RestController
public class FileController {


    @Autowired
    private FileService fileService;

    @RequestMapping(method = RequestMethod.POST, value = "/files/upload", consumes = "multipart/form-data")
    public Map<String, Object> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileUploadException("File is empty!");
        }
        String url = fileService.upload(file);

        Map<String, Object> result = new HashMap<>();
        result.put("url", url);
        return result;
    }

}
