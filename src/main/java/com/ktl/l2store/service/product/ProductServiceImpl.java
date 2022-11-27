package com.ktl.l2store.service.product;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ktl.l2store.common.ProductFilterProps;
import com.ktl.l2store.entity.FileDB;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.EvaluateRepo;
import com.ktl.l2store.repo.FileDBRepo;
import com.ktl.l2store.repo.ProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private EvaluateRepo evaluateRepo;
    @Autowired
    private FileDBRepo fileDBRepo;

    @Override
    public Page<Product> getProducts(Pageable pageable) {

        return productRepo.findAll(pageable);
    }

    @Override
    public Page<Product> getProductsExceptLocked(Pageable pageable) {

        return productRepo.findAllByLockedFalse(pageable);
    }

    @Override
    public Page<Product> getProductsWithFilter(ProductFilterProps filterProps, Pageable pageable) {

        String name = filterProps.getName();
        List<String> ctgs = filterProps.getCategoryNames();
        int star = filterProps.getStar();
        double priceStart = filterProps.getPriceStart();
        double priceEnd = filterProps.getPriceEnd();

        return productRepo
                .findWithFilter(name,
                        ctgs, star, priceStart, priceEnd, pageable);
    }

    @Override
    public Product getProduct(Long id) throws ItemNotfoundException {

        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
        product.setAmountOfEvaluate(evaluateRepo.findByProduct(product).size());
        return product;
    }

    @Override
    public Product saveProduct(Product product) {

        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Product product) {

        Product record = productRepo.findById(product.getId())
                .orElseThrow(() -> new ItemNotfoundException("Not found product"));
        record.setName(product.getName());
        record.setOverview(product.getOverview());
        record.setDetail(product.getDetail());
        record.setPrice(product.getPrice());
        record.setCategories(product.getCategories());
        record.setSalesoff(product.getSalesoff());
        if (product.getImage() != null) {

            record.getImage().setData(product.getImage().getData());
            record.getImage().setName(product.getImage().getName());
            record.getImage().setType(product.getImage().getType());
        }
        return productRepo.save(record);
    }

    @Override
    public void changeLocked(Long id) {

        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
        product.setLocked(!product.isLocked());
    }

    @Override
    public boolean addImage(Long id, MultipartFile file) throws IOException {
        // TODO Auto-generated method stub

        if (file != null) {
            // FileDB fileDB = fileDBRepo
            // .save(new FileDB(id, Calendar.getInstance().getTimeInMillis(),
            // file.getBytes(), file.getName(),
            // file.getContentType()));
            Product product = productRepo.getById(id);
            product.getImage().setData(file.getBytes());
            product.getImage().setFileCode(Calendar.getInstance().getTimeInMillis());
            product.getImage().setName(file.getName());
            product.getImage().setType(file.getContentType());
            productRepo.save(product);

        }
        return true;
    }

    @Override
    public void deleteProduct(Long id) {
        // TODO Auto-generated method stub
        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("not found product"));
        productRepo.delete(product);
    }

    @Override
    public List<Product> getAll() {
        // TODO Auto-generated method stub
        return productRepo.findAll();
    }

}
