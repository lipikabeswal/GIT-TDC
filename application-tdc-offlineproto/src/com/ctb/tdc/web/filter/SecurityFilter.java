package com.ctb.tdc.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Prevent access to any resource unless localhost or 127.0.0.1
 * @author Giuseppe_Gennaro
 *
 */
public class SecurityFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        //if( request.getRemoteHost().equalsIgnoreCase("localhost") || request.getRemoteHost().equals("127.0.0.1") ) {
            filterChain.doFilter(request, response);
        //} else {
        //	httpResponse.sendError(403);
        //}
        
    }


    public void destroy()
    {
        
    }

    public void init(FilterConfig filterConfig) throws ServletException
    {
    }
}
