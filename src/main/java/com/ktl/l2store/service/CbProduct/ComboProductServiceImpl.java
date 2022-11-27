package com.ktl.l2store.service.CbProduct;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktl.l2store.common.ComboProductFilterProps;
import com.ktl.l2store.dto.ReqCbProduct;
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

        return cbProductRepo.findWithFilter(nameCb, priceStart, priceEnd, pageable);
    }

    @Override
    public ComboProduct getCbProductById(Long id) {

        ComboProduct cbProduct = cbProductRepo.findById(id)
                .orElseThrow(() -> new ItemNotfoundException("Not found combo"));
        return cbProduct;
    }

    @Override
    public ComboProduct updateCbProduct(ReqCbProduct reqCbProduct) {
        Collection<Product> products = reqCbProduct.getProductIds().stream()
                .map(id -> productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("null")))
                .toList();

        ComboProduct record = cbProductRepo.findById(reqCbProduct.getId())
                .orElseThrow(() -> new ItemNotfoundException("not found combos"));

        record.setName(reqCbProduct.getName());
        record.setDescription(reqCbProduct.getDescription());
        record.setProducts(products);
        record.setSalesoff(reqCbProduct.getSalesoff());
        record.setTotalPrice(reqCbProduct.getTotal());
        return cbProductRepo.save(record);
    }

    @Override
    public ComboProduct createCbProduct(ReqCbProduct reqCbProduct) {

        Collection<Product> products = reqCbProduct.getProductIds().stream()
                .map(id -> Product.builder().id(id).build())
                .toList();

        ComboProduct comboProduct = ComboProduct.builder().name(reqCbProduct.getName())
                .description(reqCbProduct.getDescription())
                .products(products)
                .salesoff(reqCbProduct.getSalesoff())
                .totalPrice(reqCbProduct.getTotal())
                .build();
        return cbProductRepo.save(comboProduct);
    }

    // @Override
    // public void addProductToCombo(Long productId, Long cbProductId) {

    // ComboProduct cbProduct = getCbProductById(cbProductId);
    // Product product = productRepo.findById(productId)
    // .orElseThrow(() -> new ItemNotfoundException("Not found product"));
    // cbProduct.getProducts().add(product);
    // }

    // @Override
    // public void removeProductFromCombo(Long productId, Long cbProductId) {

    // ComboProduct cbProduct = getCbProductById(cbProductId);
    // Product product = productRepo.findById(productId)
    // .orElseThrow(() -> new ItemNotfoundException("Not found product"));
    // cbProduct.getProducts().remove(product);
    // }

    @Override
    public Page<ComboProduct> getCombosByOwner(String ownerName, Pageable pageable) {

        return cbProductRepo.findByOwnerUsername(ownerName, pageable);
    }

    @Override
    public void deleteCombo(Long id) {
        // TODO Auto-generated method stub
        ComboProduct record = cbProductRepo.findById(id)
                .orElseThrow(() -> new ItemNotfoundException("Not found collection"));
        cbProductRepo.delete(record);
    }

    @Override
    public List<ComboProduct> getAll() {
        // TODO Auto-generated method stub
        return cbProductRepo.findAll();
    }

}
