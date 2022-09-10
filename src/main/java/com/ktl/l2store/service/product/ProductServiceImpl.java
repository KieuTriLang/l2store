package com.ktl.l2store.service.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktl.l2store.common.ProductFilterProps;
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
    public Page<Product> getProducts(Pageable pageable) {
        // TODO Auto-generated method stub
        return productRepo.findAll(pageable);
    }

    @Override
    public Page<Product> getProductsExceptLocked(Pageable pageable) {
        // TODO Auto-generated method stub
        return productRepo.findAllByLockedFalse(pageable);
    }

    @Override
    public Page<Product> getProductsWithFilter(ProductFilterProps filterProps, Pageable pageable) {
        // TODO Auto-generated method stub
        String name = filterProps.getNameProduct();
        List<String> ctgs = filterProps.getCategoryNames();
        int star = filterProps.getStar();
        double priceStart = filterProps.getPriceStart();
        double priceEnd = filterProps.getPriceEnd();
        boolean locked = filterProps.isLocked();

        return productRepo
                .findByNameContainingAndCategoriesNameInAndAverageRateGreaterThanEqualAndPriceBetweenAndLocked(name,
                        ctgs, star, priceStart, priceEnd, locked, pageable);
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
    public Product updateProduct(Product product) {
        // TODO Auto-generated method stub
        Product record = productRepo.findById(product.getId())
                .orElseThrow(() -> new ItemNotfoundException("Not found product"));
        record.setName(product.getName());
        record.setOverview(product.getOverview());
        record.setDescription(product.getDescription());
        record.setPrice(product.getPrice());
        record.setCategories(product.getCategories());
        record.getImage().setData(product.getImage().getData());
        return productRepo.save(record);
    }

    @Override
    public void changeLocked(Long id) {
        // TODO Auto-generated method stub
        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
        product.setLocked(!product.isLocked());
    }

}
