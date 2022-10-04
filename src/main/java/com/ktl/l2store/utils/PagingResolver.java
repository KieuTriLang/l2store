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

        return parameter.hasParameterAnnotation(PagingParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        PagingParam pagingParam = parameter.getParameterAnnotation(PagingParam.class);
        // Page
        String pageParam = webRequest.getParameter("page");
        String pageStr = pageParam == null ? "" : pageParam;
        Integer page = pageStr.isEmpty() || pageStr.matches("\\D+") ? pagingParam.page() : Integer.valueOf(pageStr);
        page = page < 1 ? pagingParam.page() : page;

        // Limited
        String limitedParam = webRequest.getParameter("limited");
        String limitedStr = limitedParam == null ? "" : limitedParam;
        Integer limited = limitedStr.isEmpty() || limitedParam.matches("\\D+") ? pagingParam.limited()
                : Integer.valueOf(limitedStr);
        limited = limited < 0 ? pagingParam.limited() : limited;

        // Sort target
        String sortTarParam = webRequest.getParameter("sortTar");
        String sortTarStr = sortTarParam == null ? "" : sortTarParam;
        String sortTar = sortTarStr.isEmpty() ? pagingParam.sortTar() : sortTarStr;

        // Sort direction
        String sortDirParam = webRequest.getParameter("sortDir");
        String sortDirStr = sortDirParam == null ? "" : sortDirParam;
        String sortDir = sortDirStr.isEmpty() ? pagingParam.sortDir() : sortDirStr;
        sortDir = sortDir.equals("asc") ? "asc" : "desc";

        return PageReqBuilder.createReq(page - 1, limited, sortTar, sortDir);
    }

}
