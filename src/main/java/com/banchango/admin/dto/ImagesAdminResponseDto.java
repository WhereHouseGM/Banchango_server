package com.banchango.admin.dto;

import com.banchango.images.dto.ImageInfoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ImagesAdminResponseDto {
    private List<ImageInfoResponseDto> images;
}
