package com.zuul.apigateway.zuulapigateway.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class PostTimeFilter extends ZuulFilter {

    private static Logger log = LoggerFactory.getLogger(PostTimeFilter.class);
    @Override
    public String filterType() {
        return "post";
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
        Long initialTime = (Long) request.getAttribute("initialTime");
        Long finalTime = System.currentTimeMillis();
        Long deltaTime = finalTime - initialTime;
        request.setAttribute("initialTime", initialTime);
        log.info(String.format("The delta time of the request was %s", deltaTime));
        return null;
    }
}
