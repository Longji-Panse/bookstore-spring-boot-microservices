package com.remnant.catalogservice.web.controllers;

import com.remnant.catalogservice.domain.PagedResult;
import com.remnant.catalogservice.domain.ProductDto;
import com.remnant.catalogservice.domain.ProductNotFoundException;
import com.remnant.catalogservice.domain.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Just a comment to update my gi push and trigger an action. Updated again

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    PagedResult<ProductDto> getProducts(@RequestParam(name = "page", defaultValue = "1") int pageNo) {
        return productService.getProducts(pageNo);
    }

    @GetMapping("/{code}")
    ResponseEntity<ProductDto> getProductByCode(@PathVariable("code") String code) {

        return productService
                .getProductByCode(code)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> ProductNotFoundException.forCode(code));
    }
}
