package com.ktl.l2store.service.Evaluate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.entity.Product;
import com.ktl.l2store.entity.User;
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
        // TODO Auto-generated method stub
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));
        return evaluateRepo.findByUser(user, pageable);
    }

    @Override
    public Page<Evaluate> getEvaluateByProduct(Long id, Pageable pageable) {
        // TODO Auto-generated method stub
        Product product = productRepo.findById(id).orElseThrow(() -> new ItemNotfoundException("Not found product"));
        return evaluateRepo.findByProduct(product, pageable);
    }

    @Override
    public void addEvaluateToProduct(Long pId, String username, Evaluate newEvaluate) {
        // TODO Auto-generated method stub
        Product product = productRepo.findById(pId).orElseThrow(() -> new ItemNotfoundException("Not found product"));

        User user = userRepo.findByUsername(username).orElseThrow(() -> new ItemNotfoundException("Not found user"));

        newEvaluate.setUser(user);

        newEvaluate.setProduct(product);

        evaluateRepo.save(newEvaluate);
    }

    @Override
    public Evaluate updateEvaluate(Evaluate evaluate) {
        // TODO Auto-generated method stub
        return evaluateRepo.save(evaluate);
    }

}
