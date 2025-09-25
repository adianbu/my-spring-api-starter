package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping("/products")
    private List<ProductDto> GetAllProducts(){
        return productRepository.findAll().stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/product/{id}")
    private ResponseEntity<ProductDto> GetProduct(@PathVariable Long id){
        Product product =  productRepository.findById(id).orElse(null);
        if (product==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product)) ;
    }

}
