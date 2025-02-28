package com.temm.skillify.dto;
import com.temm.skillify.model.entity.UserImage;


public record UserImageResponseDto(
        String id,
        String userId,
        String content
) {
    public UserImageResponseDto(UserImage userImage) {
        this(
                userImage.getId(),
                userImage.getUser().getId(),
                userImage.getContent()
        );
    }
}
