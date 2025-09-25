package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    private Long id;

    private String name;

    private String description;

    private String price;

    private Long categoryId;



}
