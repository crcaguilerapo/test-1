package org.crcaguilerapo.franquicia.domain.usecases;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import org.crcaguilerapo.franquicia.domain.dtos.Product;
import org.crcaguilerapo.franquicia.domain.dtos.ProductWithBranch;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.ports.in.IBranchRepository;
import org.crcaguilerapo.franquicia.domain.ports.in.IProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class ProductUsecase {
    private final IProductRepository productRepository;
    private final IBranchRepository branchRepository;


    public ProductUsecase(
            IProductRepository productRepository,
            IBranchRepository branchRepository
    ) {
        this.productRepository = productRepository;
        this.branchRepository = branchRepository;
    }

    public Mono<Void> create(Product product) {
        return Mono.just(product).flatMap(v -> {
            if (hasErrorInFields(v)) {
                return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
            } else {
                return Mono.just(v);
            }
        }).flatMap(v -> productRepository.findById(v.id()).hasElement().flatMap(b -> {
            if (b) {
                return Mono.error(new UseCaseException(ErrorCode.PRODUCT_ALREADY_EXISTS));
            } else {
                return Mono.just(v);
            }
        })).flatMap(v -> branchRepository.findById(v.fk_branch()).hasElement().flatMap(b -> {
            if (!b) {
                return Mono.error(new UseCaseException(ErrorCode.BRANCH_DOES_NOT_EXISTS));
            } else {
                return Mono.just(v);
            }
        })).flatMap(productRepository::save);
    }

    public Mono<Void> delete(int productId) {
        return Mono.just(productId).flatMap(v -> {
            if (v < 0) {
                return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
            } else {
                return Mono.just(v);
            }
        }).flatMap(productRepository::delete);
    }

    public Mono<Product> update(Product product) {
        return Mono.just(product).flatMap(v -> {
            if (hasErrorInFields(v)) {
                return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
            } else {
                return Mono.just(v);
            }
        }).flatMap(v -> productRepository.findById(v.id()).hasElement().flatMap(b -> {
            if (!b) {
                return Mono.error(new UseCaseException(ErrorCode.PRODUCT_DOES_NOT_EXISTS));
            } else {
                return Mono.just(v);
            }
        })).flatMap(productRepository::update);
    }

    private Mono<Branch> getBranch(int branchId) {
        return Mono.just(branchId)
                .flatMap(
                        v -> branchRepository
                                .findById(v)
                                .switchIfEmpty(
                                        Mono.error(new UseCaseException(ErrorCode.BRANCH_DOES_NOT_EXISTS))
                                )
                );
    }

    private Mono<ProductWithBranch> mapperProductToProductWithBranch(Product product) {
        return Mono.just(product.fk_branch())
                .flatMap(v -> getBranch(product.fk_branch()))
                .map(v -> new ProductWithBranch(product.id(), product.name(), product.stock(), v));
    }

    public Flux<ProductWithBranch> getMaxStockProductsByBranch(int franchiseId) {
        return productRepository
                .getMaxStockProductsByBranch(franchiseId)
                .flatMap(this::mapperProductToProductWithBranch);
    }

    private boolean hasErrorInFields(Product product) {
        return product.id() <= 0 || product.name() == null || product.stock() < 0 || product.fk_branch() <= 0;
    }

}
