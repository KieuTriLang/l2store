package com.ktl.l2store.service.Category;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ktl.l2store.entity.Category;
import com.ktl.l2store.exception.ItemNotfoundException;

public interface CategoryService {

    List<Category> getAll();

    Page<Category> getAll(Pageable pageable);

    Category getCategory(Long id) throws ItemNotfoundException;

    Category saveCategory(Category category);

    void deleteCategory(Long id);

    void changeLocked(Long id);

}
