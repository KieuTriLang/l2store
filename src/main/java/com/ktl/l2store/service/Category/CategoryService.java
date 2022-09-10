package com.ktl.l2store.service.Category;

import java.util.List;

import com.ktl.l2store.entity.Category;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface CategoryService {

    List<Category> getAll();

    Category getCategory(Long id) throws ItemNotfoundException;

    Category saveCategory(Category category);

    void changeLocked(Long id);

}
