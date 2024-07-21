package com.cooperation.ecom.controller;

import com.cooperation.ecom.domain.request.SaveCategoryRequest;
import com.cooperation.ecom.domain.request.UpdateCategoryRequest;
import com.cooperation.ecom.domain.response.BodyResponse;
import com.cooperation.ecom.security.interceptor.AllowRoles;
import com.cooperation.ecom.service.CategoryService;
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
@RequestMapping(path = "api/category")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> getCategory() {
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", categoryService.getAllCategory()));
    }

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> saveCategory(@Valid @RequestBody SaveCategoryRequest request) {
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", categoryService.saveCategory(request)));
    }

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<?> updateCategory(@Valid @RequestBody UpdateCategoryRequest request) {
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", categoryService.updateCategory(request)));
    }

    @AllowRoles(roles = {"admin"})
    @RequestMapping(method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<?> deleteCategory(@RequestParam Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new BodyResponse(HttpStatus.OK.value(), "Success", id));
    }
}
