package com.lly835.bestpay.rest;

import com.lly835.bestpay.utils.JsonUtil;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

public class ResponseValue {

    private static final String EMPTY_STRING = "";
    private final HttpStatus httpStatus;
    private Object data;
    private URI uri;

    private ResponseValue(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    private ResponseValue(HttpStatus httpStatus, Object data) {
        this.httpStatus = httpStatus;
        this.data = data;
    }

    private ResponseValue(HttpStatus httpStatus, URI uri) {
        this.httpStatus = httpStatus;
        this.uri = uri;
    }

    public static ResponseValue ok(Object data) {
        if (data == null) {
            throw new NullPointerException("Data of response is null.");
        }

        return new ResponseValue(HttpStatus.OK, data);
    }

    public static ResponseValue created(URI uri) {
        return new ResponseValue(HttpStatus.CREATED, uri);
    }

    public static ResponseValue accepted() {
        return new ResponseValue(HttpStatus.ACCEPTED);
    }

    public static ResponseValue noContent() {
        return new ResponseValue(HttpStatus.NO_CONTENT);
    }

    public static ResponseValue seeOther(URI uri) {
        return new ResponseValue(HttpStatus.SEE_OTHER, uri);
    }

    public static ResponseValue badRequest() {
        return new ResponseValue(HttpStatus.BAD_REQUEST);
    }

    public static ResponseValue forbidden(Object data) {
        return new ResponseValue(HttpStatus.FORBIDDEN, data);
    }

    public static ResponseValue forbidden() {
        return new ResponseValue(HttpStatus.FORBIDDEN);
    }

    public static ResponseValue notFound() {
        return new ResponseValue(HttpStatus.NOT_FOUND);
    }

    public static ResponseValue internalServerError() {
        return new ResponseValue(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Response toResponse() {
        switch (this.httpStatus) {
            case OK:
                return Response.ok(this.data).build();
            case CREATED:
                return Response.created(this.uri).build();
            case ACCEPTED:
                return Response.accepted().build();
            case NO_CONTENT:
                return Response.noContent().build();
            case SEE_OTHER:
                return Response.seeOther(this.uri).build();
            case BAD_REQUEST:
                return Response.status(Status.BAD_REQUEST).entity(EMPTY_STRING).build();
            case FORBIDDEN:
                if (this.data == null) {
                    return Response.status(Status.FORBIDDEN).entity(EMPTY_STRING).build();
                } else {
                    return Response.status(Status.FORBIDDEN).entity(this.data).build();
                }
            case NOT_FOUND:
                return Response.status(Status.NOT_FOUND).entity(EMPTY_STRING).build();
            default:
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(EMPTY_STRING).build();
        }
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getData() {
        if (this.data == null) {
            return null;
        }

        if (this.data instanceof String) {
            return (String) this.data;
        } else {
            return JsonUtil.toJson(this.data);
        }
    }

}
