package org.zerock.b01.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UpDownController {
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

//    @ApiOperation(value = "Upload Post", notes = "POST 방식으로 파일 등록")
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public String upload(UploadFileDTO uploadFileDTO) {
//        log.info("UploadFileDTO : " + uploadFileDTO);
//
//        if (uploadFileDTO.getFiles() != null) {
//            uploadFileDTO.getFiles().forEach(multipartFile -> {
////                log.info(multipartFile.getOriginalFilename());
//                String originalName = multipartFile.getOriginalFilename();
//                log.info("originalName : " + originalName);
//
//                String uuid = UUID.randomUUID().toString();
//                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
//
//                try {
//                    multipartFile.transferTo(savePath);
//
//                    if (Files.probeContentType(savePath).startsWith("image")) {
//                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
//                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        return null;
//    }

    @ApiOperation(value = "Upload Post", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
        log.info("UploadFileDTO : " + uploadFileDTO);

        if (uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {
//                log.info(multipartFile.getOriginalFilename());
                String originalName = multipartFile.getOriginalFilename();
                log.info("originalName : " + originalName);

                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);

                boolean image = false;
                try {
                    multipartFile.transferTo(savePath);

                    if (Files.probeContentType(savePath).startsWith("image")) {
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200, 200);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder().uuid(uuid).fileName(originalName).img(image).build());
            });
            return list;
        }
        return null;
    }
}
