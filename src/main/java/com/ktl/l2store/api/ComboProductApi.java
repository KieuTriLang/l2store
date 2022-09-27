package com.ktl.l2store.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.common.ComboProductFilterProps;
import com.ktl.l2store.dto.ComboProductDto;
import com.ktl.l2store.entity.ComboProduct;
import com.ktl.l2store.provider.AuthorizationHeader;
import com.ktl.l2store.service.CbProduct.ComboProductService;
import com.ktl.l2store.utils.PagingParam;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping(value = "/api/combos")
@RequiredArgsConstructor
public class ComboProductApi {

    @Autowired
    private ComboProductService cbProductService;

    @Autowired
    private ModelMapper mapper;

    // Get list combo is published
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    public ResponseEntity<Object> getCbProductsPublished(
            @RequestPart(name = "filter", required = false) ComboProductFilterProps filterProps,
            @PagingParam Pageable pageable) {

        // Pageable pageable = PageReqBuilder.createReq(page, limited, sortTar,
        // sortDir);

        Page<ComboProduct> cbProducts = filterProps == null ? cbProductService.getCombos(pageable)
                : cbProductService.getCombosWithFilter(filterProps, pageable);

        List<ComboProductDto> cbProductDtos = cbProducts.stream().map(item -> mapper.map(item, ComboProductDto.class))
                .toList();

        Page<ComboProductDto> resPageDto = new PageImpl<>(cbProductDtos, pageable, cbProducts.getTotalElements());

        return ResponseEntity.ok().body(resPageDto);
    }

    // Get list combo of user
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<Object> getCbProductsOfUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PagingParam Pageable pageable,
            @RequestParam(name = "username", required = true) String username) {

        // Pageable pageable = PageReqBuilder.createReq(page, limited, sortTar,
        // sortDir);

        String sub = AuthorizationHeader.getSub(authorizationHeader);

        Page<ComboProduct> cbProducts = cbProductService.getCombosByOwner(sub, pageable);

        List<ComboProductDto> cbProductDtos = cbProducts.stream().map(item -> mapper.map(item, ComboProductDto.class))
                .toList();

        Page<ComboProductDto> resPageDto = new PageImpl<>(cbProductDtos, pageable, cbProducts.getTotalElements());

        return ResponseEntity.ok().body(resPageDto);
    }

    // Get combo
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getCbProduct(@PathVariable("id") Long id) {

        ComboProduct comboProduct = cbProductService.getCbProductById(id);

        ComboProductDto comboProductDto = mapper.map(comboProduct, ComboProductDto.class);

        return ResponseEntity.ok().body(comboProductDto);
    }

    // create new combo
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<Object> createNewCombo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestParam("name") String nameCb) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        ComboProduct cbProduct = ComboProduct.builder().name(nameCb).build();

        ComboProduct newCbProduct = cbProductService.saveCbProduct(username, cbProduct);

        ComboProductDto cbProductDto = mapper.map(newCbProduct, ComboProductDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(cbProductDto);
    }

    // update combo
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateCombo(@RequestBody ComboProductDto reqCbProduct) {
        ComboProduct comboProduct = ComboProduct.builder()
                .id(reqCbProduct.getId())
                .name(reqCbProduct.getName())
                .description(reqCbProduct.getDescription())
                .build();
        ComboProduct updatedCbProduct = cbProductService.updateCbProduct(comboProduct);

        ComboProductDto cbProductDto = mapper.map(updatedCbProduct, ComboProductDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(cbProductDto);
    }

    // Add product to combo
    @RequestMapping(value = "/{cbId}/product/{pId}", method = RequestMethod.POST)
    public ResponseEntity<Object> addProductToCombo(@PathVariable("cbId") Long cbId, @PathVariable("pId") Long pId) {
        cbProductService.addProductToCombo(pId, cbId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Remove product to combo
    @RequestMapping(value = "/{cbId}/product/{pId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> removeProductFromCombo(@PathVariable("cbId") Long cbId,
            @PathVariable("pId") Long pId) {
        cbProductService.removeProductFromCombo(pId, cbId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
