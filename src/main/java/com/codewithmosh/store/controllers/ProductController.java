package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


    @GetMapping
    private List<ProductDto> GetAllProducts(
            @RequestHeader(name="auth-token", required = false) String authToken,
            @RequestParam(name="categoryId", required = false) Byte categoryId
    ){
        List<Product> products;
        if (categoryId != null){
            products=productRepository.findByCategoryId(categoryId);
//            for (int i = 0; i < products.toArray().length; i++) {
//                System.out.println("products :"+ products.get(i).getCategory().getId());
//            }
        }
        else{
            products=productRepository.findAllWithCategory();
        }
        return products.stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    private ResponseEntity<ProductDto> GetProduct(@PathVariable Long id){
        Product product =  productRepository.findById(id).orElse(null);
        if (product==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productMapper.toDto(product)) ;
    }

    @PostMapping("/createProduct")
    private ResponseEntity<ProductDto> CreateProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriComponentsBuilder){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }
        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        var uri = uriComponentsBuilder.path("/product/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto) ;
    }

    @PutMapping("/updateProduct/{id}")
    private ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category == null){
            return ResponseEntity.badRequest().build();
        }
        var product = productRepository.findById(id).orElse(null);
        if (product==null){
            return ResponseEntity.notFound().build();
        }
        productMapper.update(productDto,product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto) ;
    }

    @DeleteMapping("/deleteProduct/{id}")
    private ResponseEntity<ProductDto> CreateProduct(
            @PathVariable Long id){

        var product = productRepository.findById(id).orElse(null);
        if (product==null){
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build() ;
    }
}
