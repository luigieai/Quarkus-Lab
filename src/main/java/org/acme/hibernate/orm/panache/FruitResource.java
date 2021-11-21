package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class FruitResource {

    @GET
    public Uni<List<Fruit>> get() {
        return Fruit.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Uni<Fruit> getSingleFruit(Long id) {
        return Fruit.findById(id);
    }

    @GET
    @Path("/vermelhas")
    public Uni<List<Fruit>> getFruitVermalhas() {
        return Fruit.findFrutasVermelha();
    }

    @POST
    @ReactiveTransactional
    public Uni<Response> createFruit(Fruit fruit){
        return Panache.<Fruit>withTransaction(fruit::persist)
                .onItem().transform(inserted -> Response.created(URI.create("/fruits/" + inserted.id)).build());
    }

    @DELETE
    @ReactiveTransactional
    @Path("/{id}")
    public Uni<Response> deleteFruit(Long id) {
        return Fruit.delete("id", id)
                .onItem().transform(resposta -> {
                    Response response;
                    if (resposta.intValue() == 0){
                        response = Response.status(404, "Fruit not found").build();
                    } else {
                        response = Response.ok().build();
                    }
                    return response;
                });
    }
}
