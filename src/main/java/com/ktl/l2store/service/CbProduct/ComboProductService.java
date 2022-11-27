package com.ktl.l2store.service.CbProduct;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ktl.l2store.common.ComboProductFilterProps;
import com.ktl.l2store.dto.ReqCbProduct;
import com.ktl.l2store.entity.ComboProduct;

public interface ComboProductService {

    List<ComboProduct> getAll();

    Page<ComboProduct> getCombos(Pageable pageable);

    Page<ComboProduct> getCombosWithFilter(ComboProductFilterProps filterProps, Pageable pageable);

    ComboProduct getCbProductById(Long id);

    ComboProduct updateCbProduct(ReqCbProduct reqCbProduct);

    ComboProduct createCbProduct(ReqCbProduct reqCbProduct);

    void deleteCombo(Long id);

    Page<ComboProduct> getCombosByOwner(String username, Pageable pageable);

    // void addProductToCombo(Long productId, Long cbProductId);

    // void removeProductFromCombo(Long productId, Long cbProductId);

}
