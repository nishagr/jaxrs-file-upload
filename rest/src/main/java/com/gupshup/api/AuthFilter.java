package com.gupshup.api;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

@Provider
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Context
	private ResourceInfo resourceInfo;
	private final String AUTH_HEADER = "Authorization";
	private final String AUTH_HEADER_PREFIX = "Basic ";
//	private final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
//			.entity("Access blocked for all users !!").build();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

//		System.out.println(requestContext.getUriInfo().getPath());
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid);

		List<String> authHeaders = requestContext.getHeaders().get(AUTH_HEADER);
		if (authHeaders == null || authHeaders.isEmpty()) {
			requestContext.abortWith(
					Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
			return;
		}

		String token = authHeaders.get(0);
		token = token.replace(AUTH_HEADER_PREFIX, "");
		String decodedToken = Base64.decodeAsString(token);
		StringTokenizer tokenizer = new StringTokenizer(decodedToken, ":");
		String username = tokenizer.nextToken();
		String password = tokenizer.nextToken();
		if ((!username.equals("admin") || !password.equals("admin"))) {
			requestContext.abortWith(
					Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build());
			return;
		}

	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		System.out.println(responseContext.getStatusInfo() + " : " + responseContext.getEntity());
	}

}