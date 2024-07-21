package com.cooperation.ecom.service;

import com.cooperation.ecom.domain.request.SaveCategoryRequest;
import com.cooperation.ecom.domain.request.SaveProductRequest;
import com.cooperation.ecom.domain.request.UpdateProductRequest;
import com.cooperation.ecom.entity.Category;
import com.cooperation.ecom.entity.Product;
import com.cooperation.ecom.exception.ResourceNotFoundException;
import com.cooperation.ecom.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductRepository productRepository;

    public List<Product> getProductByCategoryID(Long id) {
        List<Product> productList;
        try {
            productList = productRepository.findByCategoryID(id);
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        return productList;
    }

    public Product saveProduct(SaveProductRequest request) {
        Product product;
        String createAuthor = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            product = new Product(request.getTitle(), request.getDescription(), request.getPrice(), request.getDiscountPercentage(), request.getRating(), request.getStock(), request.getBrand(), request.getCategoryID(), createAuthor, new Date());
            product = productRepository.save(product);
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        return product;
    }

    public Product updateProduct(UpdateProductRequest request) {
        Product product;
        String updateAuthor = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            product = productRepository.findById(request.getId()).orElse(null);
            if (product != null) {
                product.setTitle(request.getTitle());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                product.setDiscountPercentage(request.getDiscountPercentage());
                product.setRating(request.getRating());
                product.setStock(request.getStock());
                product.setBrand(request.getBrand());
                product.setUpdateAuthor(updateAuthor);
                product.setUpdateDate(new Date());
                product = productRepository.save(product);
            } else {
                throw new ResourceNotFoundException("Product is null.");
            }
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
        return product;
    }

    public void deleteProduct(Long id) {
        try {
            productRepository.findById(id).ifPresent(product -> productRepository.delete(product));
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}
