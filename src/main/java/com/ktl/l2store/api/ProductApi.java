package com.ktl.l2store.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ktl.l2store.common.ProductFilterProps;
import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.dto.ProductDetailDto;
import com.ktl.l2store.dto.ProductOverviewDto;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.provider.AuthorizationHeader;
import com.ktl.l2store.provider.PageReqBuilder;
import com.ktl.l2store.service.Evaluate.EvaluateService;
import com.ktl.l2store.service.product.ProductService;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/api/products")
public class ProductApi {

    @Autowired
    private ProductService productService;

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private ModelMapper mapper;

    // Get list product
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(
            @RequestPart(name = "filter", required = false) ProductFilterProps filterProps,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "limited", required = false, defaultValue = "12") Integer limited,
            @RequestParam(name = "sortTar", required = false, defaultValue = "locked") String sortTar,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir) {

        Pageable pageable = PageReqBuilder.createReq(page, limited - 1, sortTar, sortDir);

        Page<Product> products = filterProps == null ? productService.getProducts(pageable)
                : productService.getProductsWithFilter(filterProps, pageable);

        List<ProductOverviewDto> productOverviewDtos = products.stream()
                .map(item -> mapper.map(item, ProductOverviewDto.class)).toList();

        Page<ProductOverviewDto> resPageDto = new PageImpl<>(productOverviewDtos, pageable,
                products.getTotalElements());

        return ResponseEntity.ok().body(resPageDto);
    }

    // Get product
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {

        Product product = productService.getProduct(id);

        ProductDetailDto productDetailDto = mapper.map(product, ProductDetailDto.class);

        return ResponseEntity.ok().body(productDetailDto);
    }

    // Change locked
    @RequestMapping(value = "/{id}/change-locked", method = RequestMethod.PUT)
    public ResponseEntity<Object> changeLock(@PathVariable("id") Long id) {

        productService.changeLocked(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Add
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Object> add(@RequestPart ProductDetailDto reqProduct,
            @RequestPart(required = false) MultipartFile file) throws IOException {

        Product product = Product.builder()
                .name(reqProduct.getName())
                .overview(reqProduct.getOverview())
                .description(reqProduct.getDescription())
                .price(reqProduct.getPrice())
                .categories(reqProduct.getCategories())
                .evaluates(new ArrayList<>())
                .build();

        if (file != null) {
            product.setImage(new FileDB(null, Calendar.getInstance().getTimeInMillis(), file.getBytes(),
                    reqProduct.getName(), file.getContentType()));
        }

        Product newProduct = productService.saveProduct(product);

        ProductDetailDto productDetailDto = mapper.map(newProduct, ProductDetailDto.class);

        return ResponseEntity.ok().body(productDetailDto);
    }

    // Update
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<Object> update(@RequestPart ProductDetailDto reqProduct, @RequestPart MultipartFile file)
            throws IOException {

        Product product = Product.builder()
                .id(reqProduct.getId())
                .name(reqProduct.getName())
                .overview(reqProduct.getOverview())
                .description(reqProduct.getDescription())
                .price(reqProduct.getPrice())
                .categories(reqProduct.getCategories())
                .build();

        if (file != null) {
            product.setImage(new FileDB(null, Calendar.getInstance().getTimeInMillis(), file.getBytes(),
                    reqProduct.getName(), file.getContentType()));
        }

        Product newProduct = productService.updateProduct(product);

        ProductDetailDto productDetailDto = mapper.map(newProduct, ProductDetailDto.class);

        return ResponseEntity.ok().body(productDetailDto);
    }

    // Get evaluates
    @RequestMapping(value = "/{id}/evaluates", method = RequestMethod.GET)
    public ResponseEntity<Object> getEvaluate(@PathVariable Long id,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "limited", required = false, defaultValue = "12") Integer limited,
            @RequestParam(name = "sortTar", required = false, defaultValue = "postedTime") String sortTar,
            @RequestParam(name = "sortDir", required = false, defaultValue = "desc") String sortDir) {

        Pageable pageable = PageReqBuilder.createReq(page, limited, sortTar, sortDir);

        Page<Evaluate> evaluates = evaluateService.getEvaluateByProduct(id, pageable);

        List<EvaluateDto> evaluateDtos = evaluates.stream().map(item -> mapper.map(item, EvaluateDto.class)).toList();

        Page<EvaluateDto> resPageDto = new PageImpl<>(evaluateDtos, pageable, evaluates.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    // Add evaluate
    @RequestMapping(value = "/{pId}/evaluates", method = RequestMethod.POST)
    public ResponseEntity<Object> addEvaluate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long pId, @RequestBody Evaluate evaluate) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        evaluateService.addEvaluateToProduct(pId, username, evaluate);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Update evaluate
    @RequestMapping(value = "/evaluates", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateEvaluate(@RequestBody Evaluate evaluate) {

        evaluateService.updateEvaluate(evaluate);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
