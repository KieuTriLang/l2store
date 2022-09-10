package com.ktl.l2store.service.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ktl.l2store.common.ProductFilterProps;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface ProductService {

    Page<Product> getProducts(Pageable pageable);

    Page<Product> getProductsExceptLocked(Pageable pageable);

    Page<Product> getProductsWithFilter(ProductFilterProps filterProps, Pageable pageable);

    Product getProduct(Long id) throws ItemNotfoundException;

    Product saveProduct(Product product);

    Product updateProduct(Product product);

    void changeLocked(Long id);
}
