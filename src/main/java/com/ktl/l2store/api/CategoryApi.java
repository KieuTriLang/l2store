package com.ktl.l2store.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.entity.Category;
import com.ktl.l2store.service.Category.CategoryService;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/api/categories")
public class CategoryApi {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok().body(categoryService.getAll());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Object> add(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(category));
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestBody Category category) {
        return ResponseEntity.ok().body(categoryService.saveCategory(category));
    }

    @RequestMapping(value = "/{id}/change-locked", method = RequestMethod.PUT)
    public ResponseEntity<Object> changeLocked(@PathVariable("id") Long id) {
        categoryService.changeLocked(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
