package com.ssafy.kkalong.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClothingDto {
    private int clothing_id;
    private int closet_id;
    private String img;
    private int mainCategory;
    private int subCategory;
    private boolean spring;
    private boolean summer;
    private boolean fall;
    private boolean winter;
    private String style;
    private String color;
    private String gender;
    private int brand_id;
    private String url;
}
