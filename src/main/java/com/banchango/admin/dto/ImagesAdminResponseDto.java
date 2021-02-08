package com.banchango.admin.dto;

import com.banchango.images.dto.ImageInfoResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ImagesAdminResponseDto {
    private String warehouseName;
    private List<ImageInfoResponseDto> images;
}
