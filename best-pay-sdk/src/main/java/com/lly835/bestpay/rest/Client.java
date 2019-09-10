package com.lly835.bestpay.rest;

import com.lly835.bestpay.rest.param.FormParam;
import com.lly835.bestpay.rest.param.HeaderParam;
import com.lly835.bestpay.rest.param.Path;
import com.lly835.bestpay.rest.param.QueryParam;
import com.lly835.bestpay.rest.type.Delete;
import com.lly835.bestpay.rest.type.Get;
import com.lly835.bestpay.rest.type.Post;
import com.lly835.bestpay.rest.type.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import java.util.Objects;

public abstract class Client {

    protected WebTarget target;

    protected Logger logger = LoggerFactory.getLogger("best-pay");

    @Post
    public ResponseValue post() {
        return postForm(null, null, null, null);
    }

    @Post
    public ResponseValue post(Path path) {
        return postForm(path, null, null, null);
    }

    @Post
    public ResponseValue post(QueryParam queryParam) {
        return postForm(null, queryParam, null, null);
    }

    @Post
    public ResponseValue post(FormParam formParam) {
        return postForm(null, null, formParam, null);
    }

    @Post
    public ResponseValue post(HeaderParam headerParam) {
        return postForm(null, null, null, headerParam);
    }

    @Post
    public ResponseValue post(Path path, QueryParam queryParam) {
        return postForm(path, queryParam, null, null);
    }

    @Post
    public ResponseValue post(Path path, FormParam formParam) {
        return postForm(path, null, formParam, null);
    }

    @Post
    public ResponseValue post(Path path, HeaderParam headerParam) {
        return postForm(path, null, null, headerParam);
    }

    @Post
    public ResponseValue post(QueryParam queryParam, FormParam formParam) {
        return postForm(null, queryParam, formParam, null);
    }

    @Post
    public ResponseValue post(QueryParam queryParam, HeaderParam headerParam) {
        return postForm(null, queryParam, null, headerParam);
    }

    @Post
    public ResponseValue post(FormParam formParam, HeaderParam headerParam) {
        return postForm(null, null, formParam, headerParam);
    }

    @Post
    public ResponseValue post(QueryParam queryParam, FormParam formParam, HeaderParam headerParam) {
        return postForm(null, queryParam, formParam, headerParam);
    }

    @Post
    public ResponseValue post(Path path, FormParam formParam, HeaderParam headerParam) {
        return postForm(path, null, formParam, headerParam);
    }

    @Post
    public ResponseValue post(Path path, QueryParam queryParam, HeaderParam headerParam) {
        return postForm(path, queryParam, null, headerParam);
    }

    @Post
    public ResponseValue post(Path path, QueryParam queryParam, FormParam formParam) {
        return postForm(path, queryParam, formParam, null);
    }

    @Post
    public ResponseValue post(Path path, QueryParam queryParam, FormParam formParam, HeaderParam headerParam) {
        return postForm(path, queryParam, formParam, headerParam);
    }

    @Post
    private ResponseValue postForm(Path path, QueryParam queryParam, FormParam formParam, HeaderParam headerParam) {
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        Form form;
        if (formParam == null) {
            form = new Form();
        } else {
            form = formParam.toForm();
        }

        try {
            this.logger.debug("Prepare to post to {} with query {}, form {} and header {}.", path, queryParam, formParam, headerParam);
            Response r = request.post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to post.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Post
    public ResponseValue postJson(String jsonParam) {
        return postJson(null, null, jsonParam, null);
    }

    @Post
    public ResponseValue postJson(Path path, String jsonParam) {
        return postJson(path, null, jsonParam, null);
    }

    @Post
    public ResponseValue postJson(QueryParam queryParam, String jsonParam) {
        return postJson(null, queryParam, jsonParam, null);
    }

    @Post
    public ResponseValue postJson(String jsonParam, HeaderParam headerParam) {
        return postJson(null, null, jsonParam, headerParam);
    }

    @Post
    public ResponseValue postJson(Path path, QueryParam queryParam, String jsonParam) {
        return postJson(path, queryParam, jsonParam, null);
    }

    @Post
    public ResponseValue postJson(Path path, String jsonParam, HeaderParam headerParam) {
        return postJson(path, null, jsonParam, headerParam);
    }

    @Post
    public ResponseValue postJson(QueryParam queryParam, String jsonParam, HeaderParam headerParam) {
        return postJson(null, queryParam, jsonParam, headerParam);
    }

    @Post
    public ResponseValue postJson(Path path, QueryParam queryParam, String jsonParam, HeaderParam headerParam) {
        Objects.requireNonNull(jsonParam, "Json is null.");
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        try {
            this.logger.debug("Prepare to post to {} with query {}, body {} and header {}.", path, queryParam, jsonParam, headerParam);
            Response r = request.post(Entity.json(jsonParam), Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to post.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Post
    public ResponseValue postXml(String xmlParam) {
        return postXml(null, null, xmlParam, null);
    }

    @Post
    public ResponseValue postXml(Path path, String xmlParam) {
        return postXml(path, null, xmlParam, null);
    }

    @Post
    public ResponseValue postXml(QueryParam queryParam, String xmlParam) {
        return postXml(null, queryParam, xmlParam, null);
    }

    @Post
    public ResponseValue postXml(String xmlParam, HeaderParam headerParam) {
        return postXml(null, null, xmlParam, headerParam);
    }

    @Post
    public ResponseValue postXml(Path path, QueryParam queryParam, String xmlParam) {
        return postXml(path, queryParam, xmlParam, null);
    }

    @Post
    public ResponseValue postXml(Path path, String xmlParam, HeaderParam headerParam) {
        return postXml(path, null, xmlParam, headerParam);
    }

    @Post
    public ResponseValue postXml(QueryParam queryParam, String xmlParam, HeaderParam headerParam) {
        return postXml(null, queryParam, xmlParam, headerParam);
    }

    @Post
    public ResponseValue postXml(Path path, QueryParam queryParam, String xmlParam, HeaderParam headerParam) {
        Objects.requireNonNull(xmlParam, "Xml is null.");
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        try {
            this.logger.debug("Prepare to post to {} with query {}, body {} and header {}.", path, queryParam, xmlParam, headerParam);
            Response r = request.post(Entity.xml(xmlParam), Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to post.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Get
    public ResponseValue get() {
        return get(null, null, null);
    }

    @Get
    public ResponseValue get(Path path) {
        return get(path, null, null);
    }

    @Get
    public ResponseValue get(QueryParam queryParam) {
        return get(null, queryParam, null);
    }

    @Get
    public ResponseValue get(HeaderParam headerParam) {
        return get(null, null, headerParam);
    }

    @Get
    public ResponseValue get(Path path, QueryParam queryParam) {
        return get(path, queryParam, null);
    }

    @Get
    public ResponseValue get(Path path, HeaderParam headerParam) {
        return get(path, null, headerParam);
    }

    @Get
    public ResponseValue get(QueryParam queryParam, HeaderParam headerParam) {
        return get(null, queryParam, headerParam);
    }

    @Get
    public ResponseValue get(Path path, QueryParam queryParam, HeaderParam headerParam) {
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        try {
            this.logger.debug("Prepare to get from {} with query {} and header {}.", path, queryParam, headerParam);
            Response r = request.get(Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to get.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Put
    public ResponseValue put() {
        return putForm(null, null, null, null);
    }

    @Put
    public ResponseValue put(Path path) {
        return putForm(path, null, null, null);
    }

    @Put
    public ResponseValue put(QueryParam queryParam) {
        return putForm(null, queryParam, null, null);
    }

    @Put
    public ResponseValue put(FormParam formParam) {
        return putForm(null, null, formParam, null);
    }

    @Put
    public ResponseValue put(HeaderParam headerParam) {
        return putForm(null, null, null, headerParam);
    }

    @Put
    public ResponseValue put(Path path, QueryParam queryParam) {
        return putForm(path, queryParam, null, null);
    }

    @Put
    public ResponseValue put(Path path, FormParam formParam) {
        return putForm(path, null, formParam, null);
    }

    @Put
    public ResponseValue put(Path path, HeaderParam headerParam) {
        return putForm(path, null, null, headerParam);
    }

    @Put
    public ResponseValue put(QueryParam queryParam, FormParam formParam) {
        return putForm(null, queryParam, formParam, null);
    }

    @Put
    public ResponseValue put(QueryParam queryParam, HeaderParam headerParam) {
        return putForm(null, queryParam, null, headerParam);
    }

    @Put
    public ResponseValue put(FormParam formParam, HeaderParam headerParam) {
        return putForm(null, null, formParam, headerParam);
    }

    @Put
    public ResponseValue put(QueryParam queryParam, FormParam formParam, HeaderParam headerParam) {
        return putForm(null, queryParam, formParam, headerParam);
    }

    @Put
    public ResponseValue put(Path path, FormParam formParam, HeaderParam headerParam) {
        return putForm(path, null, formParam, headerParam);
    }

    @Put
    public ResponseValue put(Path path, QueryParam queryParam, HeaderParam headerParam) {
        return putForm(path, queryParam, null, headerParam);
    }

    @Put
    public ResponseValue put(Path path, QueryParam queryParam, FormParam formParam) {
        return putForm(path, queryParam, formParam, null);
    }

    @Put
    public ResponseValue put(Path path, QueryParam queryParam, FormParam formParam, HeaderParam headerParam) {
        return putForm(path, queryParam, formParam, headerParam);
    }

    @Put
    private ResponseValue putForm(Path path, QueryParam queryParam, FormParam formParam, HeaderParam headerParam) {
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        Form form;
        if (formParam == null) {
            form = new Form();
        } else {
            form = formParam.toForm();
        }

        try {
            this.logger.debug("Prepare to put to {} with query {}, form {} and header {}.", path, queryParam, formParam, headerParam);
            Response r = request.put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to put.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Put
    public ResponseValue putJson(String jsonParam) {
        return putJson(null, null, jsonParam, null);
    }

    @Put
    public ResponseValue putJson(Path path, String jsonParam) {
        return putJson(path, null, jsonParam, null);
    }

    @Put
    public ResponseValue putJson(QueryParam queryParam, String jsonParam) {
        return putJson(null, queryParam, jsonParam, null);
    }

    @Put
    public ResponseValue putJson(String jsonParam, HeaderParam headerParam) {
        return putJson(null, null, jsonParam, headerParam);
    }

    @Put
    public ResponseValue putJson(Path path, QueryParam queryParam, String jsonParam) {
        return putJson(path, queryParam, jsonParam, null);
    }

    @Put
    public ResponseValue putJson(Path path, String jsonParam, HeaderParam headerParam) {
        return putJson(path, null, jsonParam, headerParam);
    }

    @Put
    public ResponseValue putJson(QueryParam queryParam, String jsonParam, HeaderParam headerParam) {
        return putJson(null, queryParam, jsonParam, headerParam);
    }

    @Put
    public ResponseValue putJson(Path path, QueryParam queryParam, String jsonParam, HeaderParam headerParam) {
        Objects.requireNonNull(jsonParam, "Json is null.");
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        try {
            this.logger.debug("Prepare to put to {} with query {}, body {} and header {}.", path, queryParam, jsonParam, headerParam);
            Response r = request.put(Entity.json(jsonParam), Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to put.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Put
    public ResponseValue putXml(String xmlParam) {
        return putXml(null, null, xmlParam, null);
    }

    @Put
    public ResponseValue putXml(Path path, String xmlParam) {
        return putXml(path, null, xmlParam, null);
    }

    @Put
    public ResponseValue putXml(QueryParam queryParam, String xmlParam) {
        return putXml(null, queryParam, xmlParam, null);
    }

    @Put
    public ResponseValue putXml(String xmlParam, HeaderParam headerParam) {
        return putXml(null, null, xmlParam, headerParam);
    }

    @Put
    public ResponseValue putXml(Path path, QueryParam queryParam, String xmlParam) {
        return putXml(path, queryParam, xmlParam, null);
    }

    @Put
    public ResponseValue putXml(Path path, String xmlParam, HeaderParam headerParam) {
        return putXml(path, null, xmlParam, headerParam);
    }

    @Put
    public ResponseValue putXml(QueryParam queryParam, String xmlParam, HeaderParam headerParam) {
        return putXml(null, queryParam, xmlParam, headerParam);
    }

    @Put
    public ResponseValue putXml(Path path, QueryParam queryParam, String xmlParam, HeaderParam headerParam) {
        Objects.requireNonNull(xmlParam, "Xml is null.");
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        try {
            this.logger.debug("Prepare to put to {} with query {}, body {} and header {}.", path, queryParam, xmlParam, headerParam);
            Response r = request.put(Entity.xml(xmlParam), Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to put.", e);
            return ResponseValue.internalServerError();
        }
    }

    @Delete
    public ResponseValue delete() {
        return delete(null, null, null);
    }

    @Delete
    public ResponseValue delete(Path path) {
        return delete(path, null, null);
    }

    @Delete
    public ResponseValue delete(QueryParam queryParam) {
        return delete(null, queryParam, null);
    }

    @Delete
    public ResponseValue delete(HeaderParam headerParam) {
        return delete(null, null, headerParam);
    }

    @Delete
    public ResponseValue delete(Path path, QueryParam queryParam) {
        return delete(path, queryParam, null);
    }

    @Delete
    public ResponseValue delete(Path path, HeaderParam headerParam) {
        return delete(path, null, headerParam);
    }

    @Delete
    public ResponseValue delete(QueryParam queryParam, HeaderParam headerParam) {
        return delete(null, queryParam, headerParam);
    }

    @Delete
    public ResponseValue delete(Path path, QueryParam queryParam, HeaderParam headerParam) {
        WebTarget target = this.target;
        if (path != null) {
            target = path.appendToTarget(this.target);
        }

        if (queryParam != null) {
            target = queryParam.appendToTarget(target);
        }

        Builder request = target.request();
        if (headerParam != null) {
            request = headerParam.appendToRequest(request);
        }

        try {
            this.logger.debug("Prepare to delete {} with query {} and header {}.", path, queryParam, headerParam);
            Response r = request.delete(Response.class);
            return toResponseValue(r);
        } catch (ProcessingException | WebApplicationException e) {
            this.logger.error("Fail to delete.", e);
            return ResponseValue.internalServerError();
        }
    }

    private ResponseValue toResponseValue(Response response) {
        Status status = Status.fromStatusCode(response.getStatus());
        this.logger.debug("Response status is {}.", status.getStatusCode());
        Family family = response.getStatusInfo().getFamily();
        String data = response.readEntity(String.class);
        switch (family) {
            case SUCCESSFUL:
                if (status == Status.OK) {
                    this.logger.debug("Response data is {}.", data);
                    return ResponseValue.ok(data);
                } else if (status == Status.ACCEPTED) {
                    return ResponseValue.accepted();
                } else if (status == Status.NO_CONTENT) {
                    return ResponseValue.noContent();
                } else {
                    return ResponseValue.internalServerError();
                }
            case CLIENT_ERROR:
                if (status == Status.BAD_REQUEST) {
                    return ResponseValue.badRequest();
                } else if (status == Status.FORBIDDEN) {
                    this.logger.debug("Response data is {}.", data);
                    return ResponseValue.forbidden(data);
                } else if (status == Status.NOT_FOUND) {
                    return ResponseValue.notFound();
                } else {
                    return ResponseValue.internalServerError();
                }
            case INFORMATIONAL:
            case REDIRECTION:
            case SERVER_ERROR:
            case OTHER:
            default:
                return ResponseValue.internalServerError();
        }
    }

}
