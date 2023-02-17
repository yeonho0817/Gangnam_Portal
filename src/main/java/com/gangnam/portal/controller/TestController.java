package com.gangnam.portal.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;

@RestController
@ApiIgnore
@RequestMapping("/img/test")
public class TestController {
    @PostMapping("")
    public void saveImage(@RequestBody MultipartFile multipartFile) {
        try {
            multipartFile.transferTo(new File("C:\\Users\\dusgh\\OneDrive\\사진\\"+1+".jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/d")
    public Resource returnImage() {
        String path = "C:\\Users\\dusgh\\OneDrive\\사진\\gangnam-portal\\정연호-yh.jung@twolinecode.com.jpg";
        Resource resource2 = new FileSystemResource(path);
        return resource2;
    }


}
