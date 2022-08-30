package com.ktl.l2store.service.product;

import java.util.List;

import com.ktl.l2store.entity.Product;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface ProductService {

    List<Product> getAll();

    Product getProduct(Long id) throws ItemNotfoundException;

    Product saveProduct(Product product);

    void setLockedProduct(Long id);
}
