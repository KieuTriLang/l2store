package com.ktl.l2store.service.Evaluate;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktl.l2store.dto.ReqEvaluate;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;
import com.ktl.l2store.exception.CommonException;
import com.ktl.l2store.exception.ItemNotfoundException;
import com.ktl.l2store.repo.EvaluateRepo;
import com.ktl.l2store.repo.ProductRepo;
import com.ktl.l2store.repo.UserRepo;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    private EvaluateRepo evaluateRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Page<Evaluate> getEvaluateByUser(String username, Pageable pageable) {

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));
        return evaluateRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Evaluate> getEvaluateByProduct(Long id, Pageable pageable) {

        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
        return evaluateRepo.findByProduct(product, pageable);
    }

    @Override
    public void addEvaluateToProduct(Long pId, String username, ReqEvaluate evaluate) {

        Product product = productRepo.findById(pId).orElseThrow(() -> new ItemNotfoundException("Not found product"));

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));

        Evaluate newEvaluate = Evaluate.builder().user(user).product(product).star(evaluate.getStar())
                .content(evaluate.getContent()).postedTime(ZonedDateTime.now(ZoneId.of("Z"))).build();

        newEvaluate = evaluateRepo.save(newEvaluate);

        product.getEvaluates().add(newEvaluate);
        product.setAverageRate(
                evaluateRepo.findByProduct(product).stream().mapToDouble(e -> e.getStar()).average().orElse(5));
        productRepo.save(product);
    }

    @Override
    public Evaluate updateEvaluate(String username, ReqEvaluate evaluate) {

        Evaluate record = evaluateRepo.findById(evaluate.getId())
                .orElseThrow(() -> new ItemNotfoundException("Evaluate is not existed"));

        if (!record.getUser().getUsername().equals(username)) {
            throw new CommonException("You can not edit this evaluate!");
        }

        record.setContent(evaluate.getContent());
        record.setStar(evaluate.getStar());
        record.setPostedTime(ZonedDateTime.now(ZoneId.of("Z")));

        return evaluateRepo.save(record);
    }

    @Override
    public Page<Evaluate> getAll(Pageable pageable) {
        // TODO Auto-generated method stub
        return evaluateRepo.findAll(pageable);
    }

    @Override
    public void deleteMultiEvaluate(List<Long> ids) {
        // TODO Auto-generated method stub
        List<Product> record = productRepo.findByEvaluatesIdIn(ids);
        // record.stream().map();
        record.forEach(p -> p.getEvaluates().removeIf(e -> ids.contains(e.getId())));

        productRepo.saveAll(record);
    }

    @Override
    public Page<Evaluate> searchContentContains(String searchString, Pageable pageable) {
        // TODO Auto-generated method stub
        return evaluateRepo.findByContentContaining(searchString, pageable);
    }

    @Override
    public void saveMultiEvaluate(List<Evaluate> evaluates) {
        // TODO Auto-generated method stub
        evaluateRepo.saveAll(evaluates);
    }

}
