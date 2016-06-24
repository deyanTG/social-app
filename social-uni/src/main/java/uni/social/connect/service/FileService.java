package uni.social.connect.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import uni.social.connect.exception.FileUploadException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Service
public class FileService {

    @Value("${cloverlead.files.root.dir}")
    private String filesRootDir;

    @Value("${cloverlead.files.url.prefix}")
    private String filesUrlPrefix;

    @Value("${cloverlead.files.name.size}")
    private int filesNameSize;

    public String upload(MultipartFile file) {
        String name = RandomStringUtils.random(filesNameSize, true, true);
        String url = filesUrlPrefix + "/" + name;
        String path = filesRootDir + "/" + name;
        String originalFileName = file.getOriginalFilename();
        if (!StringUtils.isBlank(originalFileName)) {
            String[] split = originalFileName.split("\\.");
            path += "." + split[split.length - 1];
        }
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
            FileCopyUtils.copy(file.getInputStream(), stream);
            stream.close();
            return url;
        } catch (Exception e) {
            throw new FileUploadException(e.getMessage());
        }
    }
}
