package com.ktl.l2store.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.service.CbProduct.CbProductService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/api/combo")
public class CbProductApi {

    @Autowired
    private CbProductService cbProductService;

    // Get list combo is published
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    public ResponseEntity<Object> getCbProductsPublished() {
        return ResponseEntity.ok().body("body");
    }

    // Get combo
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getCbProduct(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body("body");
    }

    // Add product to combo
    @RequestMapping(value = "/{cbId}/product/{pId}", method = RequestMethod.POST)
    public ResponseEntity<Object> addProductToCombo(@PathVariable("cbId") Long cbId, @PathVariable("pId") Long pId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Remove product to combo
    @RequestMapping(value = "/{cbId}/product/{pId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeProductFromCombo(@PathVariable("cbId") Long cbId,
            @PathVariable("pId") Long pId) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Publish combo
    @RequestMapping(value = "/{id}/change-published", method = RequestMethod.PUT)
    public ResponseEntity<Object> changePublished(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
