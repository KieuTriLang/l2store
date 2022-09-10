package com.ktl.l2store.service.CbProduct;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.ComboProductRepo;
import com.ktl.l2store.repo.ProductRepo;

@Service
public class CbProductServiceImpl implements CbProductService {

    @Autowired
    private ComboProductRepo cbProductRepo;

    @Autowired
    private ProductRepo productRepo;

    @Override
    public List<ComboProduct> getAll() {
        // TODO Auto-generated method stub
        return cbProductRepo.findAll();
    }

    @Override
    public ComboProduct getCbProductById(Long id) {
        // TODO Auto-generated method stub
        ComboProduct cbProduct = cbProductRepo.findById(id)
                .orElseThrow(() -> new ItemNotfoundException("Not found combo"));
        return cbProduct;
    }

    @Override
    public ComboProduct saveCbProduct(ComboProduct comboProduct) {
        // TODO Auto-generated method stub
        return cbProductRepo.save(comboProduct);
    }

    @Override
    public void changePublished(Long id) {
        // TODO Auto-generated method stub
        ComboProduct cbProduct = getCbProductById(id);
        cbProduct.setPublished(!cbProduct.isPublished());
    }

    @Override
    public void addProductToCombo(Long productId, Long cbProductId) {
        // TODO Auto-generated method stub
        ComboProduct cbProduct = getCbProductById(cbProductId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ItemNotfoundException("Not found product"));
        cbProduct.getProducts().add(product);
    }

}
