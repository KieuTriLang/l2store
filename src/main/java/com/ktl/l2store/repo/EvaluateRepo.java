package com.ktl.l2store.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktl.l2store.entity.Evaluate;

@Repository
public interface EvaluateRepo extends JpaRepository<Evaluate, Long> {

}
