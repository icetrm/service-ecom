package com.cooperation.ecom.controller;

import com.cooperation.ecom.domain.request.SaveProductRequest;
import com.cooperation.ecom.domain.request.UpdateProductRequest;
import com.cooperation.ecom.domain.response.BodyResponse;
import com.cooperation.ecom.security.interceptor.AllowRoles;
import com.cooperation.ecom.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin
@RequestMapping(path = "api/product")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @AllowRoles(roles = {"admin"})
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getProductByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", productService.getProductByCategoryID(id)));
    }

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> saveCategory(@Valid @RequestBody SaveProductRequest request) {
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", productService.saveProduct(request)));
    }

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", productService.updateProduct(request)));
    }

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<?> deleteCategory(@RequestParam Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", id));
    }
}
