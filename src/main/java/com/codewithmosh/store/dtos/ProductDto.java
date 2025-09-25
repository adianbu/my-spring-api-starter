package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    private int id;

    private String name;

    private String description;

    private String price;

    private int categoryId;



}
