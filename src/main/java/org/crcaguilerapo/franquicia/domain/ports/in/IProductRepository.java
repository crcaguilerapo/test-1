package org.crcaguilerapo.franquicia.domain.ports.in;

import org.crcaguilerapo.franquicia.domain.dtos.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductRepository {
    Mono<Void> save(Product product);
    Mono<Void> delete(int productId);
    Mono<Product> update(Product product);
    Mono<Product> findById(int productId);

    Flux<Product> getMaxStockProductsByBranch(int franchiseId);

}
