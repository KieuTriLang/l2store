package com.ktl.l2store.service.Evaluate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ktl.l2store.entity.Evaluate;

public interface EvaluateService {

    Page<Evaluate> getEvaluateByUser(String username, Pageable pageable);

    Page<Evaluate> getEvaluateByProduct(Long id, Pageable pageable);

    void addEvaluateToProduct(Long pId, String username, Evaluate evaluate);

    Evaluate updateEvaluate(String username, Evaluate evaluate);
}
