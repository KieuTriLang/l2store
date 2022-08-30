package com.ktl.l2store.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.Product;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.ProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<Product> getAll() {
        // TODO Auto-generated method stub
        return productRepo.findAll();
    }

    @Override
    public Product getProduct(Long id) throws ItemNotfoundException {
        // TODO Auto-generated method stub
        return productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
    }

    @Override
    public Product saveProduct(Product product) {
        // TODO Auto-generated method stub
        return productRepo.save(product);
    }

    @Override
    public void setLockedProduct(Long id) {
        // TODO Auto-generated method stub
        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
        product.setLocked(!product.isLocked());
    }

}
