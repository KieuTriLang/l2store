package com.ktl.l2store.service.product;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ktl.l2store.common.ProductFilterProps;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface ProductService {

    List<Product> getAll();

    Page<Product> getProducts(Pageable pageable);

    Page<Product> getProductsExceptLocked(Pageable pageable);

    Page<Product> getProductsWithFilter(ProductFilterProps filterProps, Pageable pageable);

    Product getProduct(Long id) throws ItemNotfoundException;

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    void deleteProduct(Long id);

    void changeLocked(Long id);

    boolean addImage(Long id, MultipartFile file) throws IOException;
}
