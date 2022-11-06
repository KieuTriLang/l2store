package com.ktl.l2store.api;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ktl.l2store.dto.EvaluateDto;
import com.ktl.l2store.entity.Evaluate;
import com.ktl.l2store.service.Evaluate.EvaluateService;
import com.ktl.l2store.utils.PagingParam;

@RestController
@RequestMapping("/api/evaluates")
public class EvaluateApi {

    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private ModelMapper mapper;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> getEvaluate(
            @PagingParam(sortTar = "postedTime", sortDir = "desc") Pageable pageable) {

        Page<Evaluate> evaluates = evaluateService.getAll(pageable);

        List<EvaluateDto> evaluateDtos = evaluates.stream()
                .map(item -> mapper.map(item, EvaluateDto.class)).toList();

        Page<EvaluateDto> resPageDto = new PageImpl<>(evaluateDtos, pageable,
                evaluates.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<Object> searchEvaluateContainsContent(@RequestParam String search,
            @PagingParam(sortTar = "postedTime", sortDir = "desc") Pageable pageable) {

        Page<Evaluate> evaluates = evaluateService.searchContentContains(search, pageable);

        List<EvaluateDto> evaluateDtos = evaluates.stream()
                .map(item -> mapper.map(item, EvaluateDto.class)).toList();

        Page<EvaluateDto> resPageDto = new PageImpl<>(evaluateDtos, pageable,
                evaluates.getTotalElements());

        return ResponseEntity.status(HttpStatus.OK).body(resPageDto);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteEvaluate(@RequestParam List<Long> ids) {

        evaluateService.deleteMultiEvaluate(ids);

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

}
