package com.temm.skillify.controller;

import com.temm.skillify.dto.UserImageRequestDto;
import com.temm.skillify.dto.UserImageResponseDto;
import com.temm.skillify.service.UserImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userImage")
public class UserImageController {

    @Autowired
    private UserImageService userImageService;

    @GetMapping("/{id}")
    public ResponseEntity getUserImageByUserId(@PathVariable("id") String id){
        UserImageResponseDto userImage = userImageService.getUserImageByUserId(id);
        return new ResponseEntity<>(userImage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity createUserImage(@RequestBody UserImageRequestDto body){
        System.out.println(body);
        userImageService.createUserImage(body);
        return ResponseEntity.ok("Imagem criada/atualizada com sucesso");
    }
}