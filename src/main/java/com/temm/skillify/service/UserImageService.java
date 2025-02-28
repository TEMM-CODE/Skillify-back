package com.temm.skillify.service;
import com.temm.skillify.dto.UserImageRequestDto;
import com.temm.skillify.dto.UserImageResponseDto;
import com.temm.skillify.model.entity.UserEntity;
import com.temm.skillify.model.entity.UserImage;
import com.temm.skillify.model.repository.UserImageRepository;
import com.temm.skillify.model.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserImageService {
    @Autowired
    private UserImageRepository repository;

    @Autowired
    private UserRepository userRepository;

    public String createUserImage(UserImageRequestDto data){
        UserEntity user = userRepository.findById(data.userId().toString()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserImage existingImage = repository.findByUser(user);
        if(existingImage != null){
            existingImage.setContent(data.content());
            repository.save(existingImage);
            return "OK";
        }
        UserImage newImage = new UserImage();
        newImage.setContent(data.content());
        newImage.setUser(user);
        repository.save(newImage);
        return "OK";
    }

    public UserImageResponseDto getUserImageByUserId(String userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        UserImage existingImage = repository.findByUser(user);
        return new UserImageResponseDto(existingImage);
    }
}
