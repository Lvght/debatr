package br.ufscar.dc.dsw1.debatr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.ufscar.dc.dsw1.debatr.helper.ExternalImageServiceHelper;

@Controller
@RequestMapping("/test")
public class S3TestController {

    @GetMapping("/image-upload")
    public String imageUploadForm() {
        return "imageUploadTest";
    }

    @PostMapping("/image-upload")
    public String imageUpload(@RequestParam("file") MultipartFile file) {
        ExternalImageServiceHelper helper = new ExternalImageServiceHelper();
        helper.uploadImage(file, "myfile");

        return "imageUploadTest";
    }

}
