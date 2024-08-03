package org.crcaguilerapo.franquicia.infrastructure.adapters.in.http;

import org.crcaguilerapo.franquicia.domain.dtos.Error;
import org.crcaguilerapo.franquicia.domain.dtos.Product;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.usecases.ProductUsecase;
import org.crcaguilerapo.franquicia.infrastructure.service.Serialization;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/product", produces = "application/json")
public class ProductController {

    private final ProductUsecase productUsecase;
    private final Serialization serialization;

    public ProductController(ProductUsecase productUsecase, Serialization serialization) {
        this.productUsecase = productUsecase;
        this.serialization = serialization;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> createProduct(
            @RequestBody Product product
    ) {
        return productUsecase
                .create(product)
                .map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
                .onErrorResume(e -> {
                    if (e instanceof UseCaseException useCaseException) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(useCaseException.errorCode)))
                        );
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(ErrorCode.UNKNOWN)))
                        );
                    }
                });
    }

    @DeleteMapping("/{idProduct}")
    public Mono<ResponseEntity<String>> deleteProduct(
            @PathVariable int idProduct
    ) {
        return productUsecase
                .delete(idProduct)
                .map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
                .onErrorResume(e -> {
                    if (e instanceof UseCaseException useCaseException) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(useCaseException.errorCode)))
                        );
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(ErrorCode.UNKNOWN)))
                        );
                    }
                });
    }

    @PutMapping
    public Mono<ResponseEntity<String>> updateProduct(
            @RequestBody Product product
    ) {
        return productUsecase
                .update(product)
                .map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
                .onErrorResume(e -> {
                    if (e instanceof UseCaseException useCaseException) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(useCaseException.errorCode)))
                        );
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(ErrorCode.UNKNOWN)))
                        );
                    }
                });
    }

    @GetMapping("/franchise/{franchiseId}")
    public Mono<ResponseEntity<String>> getMaxStockProductsByBranch(
            @PathVariable int franchiseId
    ) {
        return productUsecase
                .getMaxStockProductsByBranch(franchiseId)
                .collectList()
                .map(v -> ResponseEntity.status(HttpStatus.OK).body(serialization.objectToJson(v)))
                .onErrorResume(e -> {
                    if (e instanceof UseCaseException useCaseException) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(useCaseException.errorCode)))
                        );
                    } else {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.OK)
                                        .body(serialization.objectToJson(new Error(ErrorCode.UNKNOWN)))
                        );
                    }
                });
    }

}
