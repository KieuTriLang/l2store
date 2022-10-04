package com.ktl.l2store.service.CbProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktl.l2store.common.ComboProductFilterProps;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.ComboProductRepo;
import com.ktl.l2store.repo.ProductRepo;
import com.ktl.l2store.repo.UserRepo;

@Service
public class ComboProductServiceImpl implements ComboProductService {

    @Autowired
    private ComboProductRepo cbProductRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Page<ComboProduct> getCombos(Pageable pageable) {

        return cbProductRepo.findAll(pageable);
    }

    @Override
    public Page<ComboProduct> getCombosWithFilter(ComboProductFilterProps filterProps, Pageable pageable) {

        String nameCb = filterProps.getName();
        double priceStart = filterProps.getPriceStart();
        double priceEnd = filterProps.getPriceEnd();

        return cbProductRepo.findByNameContainingAndTotalPriceBetween(nameCb, priceStart, priceEnd, pageable);
    }

    @Override
    public ComboProduct getCbProductById(Long id) {

        ComboProduct cbProduct = cbProductRepo.findById(id)
                .orElseThrow(() -> new ItemNotfoundException("Not found combo"));
        return cbProduct;
    }

    @Override
    public ComboProduct updateCbProduct(ComboProduct comboProduct) {

        ComboProduct record = cbProductRepo.findById(comboProduct.getId())
                .orElseThrow(() -> new ItemNotfoundException("Not found combo"));
        record.setName(comboProduct.getName());
        record.setDescription(comboProduct.getDescription());
        return cbProductRepo.save(record);
    }

    @Override
    public ComboProduct saveCbProduct(String username, ComboProduct comboProduct) {

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));
        comboProduct.setOwner(user);
        return cbProductRepo.save(comboProduct);
    }

    @Override
    public void addProductToCombo(Long productId, Long cbProductId) {

        ComboProduct cbProduct = getCbProductById(cbProductId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ItemNotfoundException("Not found product"));
        cbProduct.getProducts().add(product);
    }

    @Override
    public void removeProductFromCombo(Long productId, Long cbProductId) {

        ComboProduct cbProduct = getCbProductById(cbProductId);
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ItemNotfoundException("Not found product"));
        cbProduct.getProducts().remove(product);
    }

    @Override
    public Page<ComboProduct> getCombosByOwner(String ownerName, Pageable pageable) {

        return cbProductRepo.findByOwnerUsername(ownerName, pageable);
    }

}
