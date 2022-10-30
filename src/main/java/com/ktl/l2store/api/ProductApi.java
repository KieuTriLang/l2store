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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktl.l2store.common.ProductFilterProps;
import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.dto.ProductDetailDto;
import com.ktl.l2store.dto.ProductOverviewDto;
import com.ktl.l2store.dto.ReqEvaluate;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.provider.AuthorizationHeader;
import com.ktl.l2store.service.Category.CategoryService;
import com.ktl.l2store.service.Evaluate.EvaluateService;
import com.ktl.l2store.service.product.ProductService;
import com.ktl.l2store.utils.PagingParam;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/products")
public class ProductApi {

    @Autowired
    private ProductService productService;

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper mapper;

    // Get list product
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> getAll(
            @RequestParam(name = "filter", required = false) String filter,
            @PagingParam Pageable pageable) throws JsonMappingException, JsonProcessingException {

        // Pageable pageable = PageReqBuilder.createReq(page, limited - 1, sortTar,
        // sortDir);
        ProductFilterProps filterProps = null;
        if (filter != null) {

            filterProps = new ObjectMapper().readValue(filter, ProductFilterProps.class);

            List<String> categoryNames = filterProps.getCategoryNames();
            filterProps.setCategoryNames(categoryNames.size() > 0 ? categoryNames
                    : categoryService.getAll().stream().map(c -> c.getName()).toList());

            double priceStart = filterProps.getPriceStart();
            double priceEnd = filterProps.getPriceEnd();
            if (priceStart > priceEnd) {
                double temp = priceStart;
                priceStart = priceEnd;
                priceEnd = temp;
            }
            filterProps.setPriceStart(priceStart <= 0 ? 0 : priceStart);
            filterProps.setPriceEnd(priceEnd <= 0 || priceEnd > 9_999_999 ? 9_999_999 : priceEnd);
        }
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
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> add(@RequestPart String bodyProduct,
            @RequestPart(required = false) MultipartFile file) throws IOException {

        ProductDetailDto reqProduct = new ObjectMapper().readValue(bodyProduct, ProductDetailDto.class);
        Product product = Product.builder()
                .name(reqProduct.getName())
                .overview(reqProduct.getOverview())
                .detail(reqProduct.getDetail())
                .image(new FileDB(null, Calendar.getInstance().getTimeInMillis(), null, null, null))
                .price(reqProduct.getPrice())
                .categories(reqProduct.getCategories())
                .salesoff(reqProduct.getSalesoff())
                .averageRate(5)
                .evaluates(new ArrayList<>())
                .build();

        if (file != null) {
            product.getImage().setData(file.getBytes());
            product.getImage().setName(file.getName());
            product.getImage().setType(file.getContentType());
        }

        Product newProduct = productService.saveProduct(product);

        ProductDetailDto productDetailDto = mapper.map(newProduct, ProductDetailDto.class);

        return ResponseEntity.ok().body(productDetailDto);
    }

    // Update
    @RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> update(@RequestPart String bodyProduct,
            @RequestPart(required = false) MultipartFile file)
            throws IOException {
        ProductDetailDto reqProduct = new ObjectMapper().readValue(bodyProduct, ProductDetailDto.class);
        Product product = Product.builder()
                .id(reqProduct.getId())
                .name(reqProduct.getName())
                .overview(reqProduct.getOverview())
                .detail(reqProduct.getDetail())
                .price(reqProduct.getPrice())
                .categories(reqProduct.getCategories())
                .salesoff(reqProduct.getSalesoff())
                .build();

        if (file != null) {
            product.setImage(new FileDB(null, Calendar.getInstance().getTimeInMillis(), file.getBytes(),
                    reqProduct.getName(), file.getContentType()));
        }

        Product updatedProduct = productService.updateProduct(product);

        ProductDetailDto productDetailDto = mapper.map(updatedProduct, ProductDetailDto.class);

        return ResponseEntity.ok().body(productDetailDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deteleProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // Get evaluates
    @RequestMapping(value = "/{id}/evaluates", method = RequestMethod.GET)
    public ResponseEntity<Object> getEvaluate(@PathVariable Long id,
            @PagingParam(sortTar = "postedTime", sortDir = "desc") Pageable pageable) {

        // Pageable pageable = PageReqBuilder.createReq(page, limited, sortTar,
        // sortDir);

        Page<Evaluate> evaluates = evaluateService.getEvaluateByProduct(id, pageable);

        List<EvaluateDto> evaluateDtos = evaluates.stream().map(item -> mapper.map(item, EvaluateDto.class)).toList();

        Page<EvaluateDto> resPageDto = new PageImpl<>(evaluateDtos, pageable, evaluates.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    // Add evaluate
    @RequestMapping(value = "/{pId}/evaluates", method = RequestMethod.POST)
    public ResponseEntity<Object> addEvaluate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @PathVariable Long pId, @RequestBody ReqEvaluate evaluate) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        evaluateService.addEvaluateToProduct(pId, username, evaluate);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Update evaluate
    @RequestMapping(value = "/evaluates", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateEvaluate(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader,
            @RequestBody ReqEvaluate evaluate) {

        String username = AuthorizationHeader.getSub(authorizationHeader);

        evaluateService.updateEvaluate(username, evaluate);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
