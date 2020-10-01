package com.egomezle.blockchain.training.config;

import com.egomezle.blockchain.training.config.binders.CorsFilterConfigurationBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {
    private final CorsFilterConfigurationBinder corsFilterConfigurationBinder;

    public CorsFilter(CorsFilterConfigurationBinder corsFilterConfigurationBinder) {
        this.corsFilterConfigurationBinder = corsFilterConfigurationBinder;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        response.setHeader("Access-Control-Allow-Origin", corsFilterConfigurationBinder.getAllowedOrigin());
        response.setHeader("Access-Control-Allow-Methods", corsFilterConfigurationBinder.getAllowedMethod());
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", corsFilterConfigurationBinder.getAllowedHeader());

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}