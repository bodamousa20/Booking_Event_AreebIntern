package com.events.booking.web.Dto;

import lombok.Data;

@Data
public class ImageDto {
    private Long id;
    private String fileName;
    private String fileType;
    private String downloadUrl;
}
