package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class FortuneDTO {
    private Boolean isOpened = true;
    private String message;
}
