package org.acme.hibernate.orm.panache;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import io.smallrye.mutiny.Uni;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.List;

@Entity
@Cacheable
public class Fruit extends PanacheEntity {

    @Column(length = 40, unique = true)
    public String name;
    public boolean fruitIsVermelha;

    public static Uni<Fruit> findByName(String fruitName){
        return find("name", fruitName).firstResult();
    }

    public static Uni<List<Fruit>> findFrutasVermelha() {
        return list("fruitIsVermelha", true);
    }

}
