package com.jenmann.controller;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * This utility exists to filter all requests, and specifically to add appropriate headers to thoroughly
 * allow cross origin resource sharing (CORS)
 *
 * @author jcmann
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    /**
     * This method is responsible for adding all headers necessary to enable cross origin resource sharing.
     *
     * @param requestContext effectively a servlet container context
     * @param responseContext our response context
     * @throws IOException because it works with the requests and responses
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }

}
