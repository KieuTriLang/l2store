package com.ktl.l2store.service.CbProduct;

import java.util.List;

import com.ktl.l2store.entity.ComboProduct;

public interface CbProductService {

    List<ComboProduct> getAll();

    ComboProduct getCbProductById(Long id);

    ComboProduct saveCbProduct(ComboProduct comboProduct);

    void addProductToCombo(Long productId, Long cbProductId);

    void changePublished(Long id);
}
