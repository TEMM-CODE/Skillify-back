package com.temm.skillify.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public class AvatarUpdateRequest {
    @NotBlank(message = "Image URL cannot be blank")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}