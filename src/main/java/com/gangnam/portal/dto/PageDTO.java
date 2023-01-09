package com.gangnam.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class PageDTO {
    private int pageSize;
    private int pageNumber;
}
