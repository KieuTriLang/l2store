package com.ktl.l2store.service.Category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.Category;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.CategoryRepo;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Override
    public List<Category> getAll() {

        return categoryRepo.findAll();
    }

    @Override
    public Category getCategory(Long id) throws ItemNotfoundException {

        return categoryRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found category"));
    }

    @Override
    public Category saveCategory(Category category) {

        return categoryRepo.save(category);
    }

    @Override
    public void changeLocked(Long id) {

        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ItemNotfoundException("Not found category"));
        category.setLocked(!category.isLocked());
    }

}
