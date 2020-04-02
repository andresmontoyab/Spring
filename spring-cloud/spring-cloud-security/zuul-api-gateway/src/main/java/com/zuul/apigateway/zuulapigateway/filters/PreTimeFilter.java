package com.zuul.apigateway.zuulapigateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PreTimeFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PreTimeFilter.class);
    @Override
    public String filterType() {

        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        Long initialTime = System.currentTimeMillis();
        request.setAttribute("initialTime", initialTime);
        log.info(String.format("%s request route to %s", request.getMethod(), request.getRequestURL()));
        return null;
    }
}
