package br.com.sharpfy.nextapi.resources;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import br.com.sharpfy.nextapi.NextApiClient;
import br.com.sharpfy.nextapi.models.Page;
import java.util.List;

abstract class Resource {
    private static final TypeFactory TYPES = TypeFactory.defaultInstance();
    private final NextApiClient client;

    Resource(NextApiClient client) { this.client = client; }

    <T> T get(String path, Class<T> type) { return client.request("GET", path, null, TYPES.constructType(type)); }

    <T> List<T> getList(String path, Class<T> item) {
        return client.request("GET", path, null, TYPES.constructCollectionType(List.class, item));
    }

    <T> Page<T> getPage(String path, Class<T> item) {
        return client.request("GET", path, null, TYPES.constructParametricType(Page.class, item));
    }

    <T> List<T> postList(String path, Object body, Class<T> item) {
        return client.request("POST", path, body, TYPES.constructCollectionType(List.class, item));
    }
}
