package com.ktl.l2store.utils;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.ktl.l2store.provider.PageReqBuilder;

public class PagingResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // TODO Auto-generated method stub
        return parameter.hasParameterAnnotation(PagingParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        // TODO Auto-generated method stub

        PagingParam pagingParam = parameter.getParameterAnnotation(PagingParam.class);
        // Page
        String pageStr = webRequest.getParameter("page");
        Integer page = pageStr.isEmpty() ? pagingParam.page() : Integer.valueOf(pageStr);
        page = page < 1 ? pagingParam.page() : page;

        // Limited
        String limitedStr = webRequest.getParameter("limited");
        Integer limited = limitedStr.isEmpty() ? pagingParam.limited() : Integer.valueOf(limitedStr);
        limited = limited < 0 ? pagingParam.limited() : limited;

        // Sort target
        String sortTarStr = webRequest.getParameter("sortTar");
        String sortTar = sortTarStr.isEmpty() ? pagingParam.sortTar() : sortTarStr;

        // Sort direction
        String sortDirStr = webRequest.getParameter("sortDir");
        String sortDir = sortDirStr.isEmpty() ? pagingParam.sortDir() : sortDirStr;
        sortDir = sortDir.equals("asc") ? "asc" : "desc";
        return PageReqBuilder.createReq(page, limited, sortTar, sortDir);
    }

}
