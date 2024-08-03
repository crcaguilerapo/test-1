package org.crcaguilerapo.franquicia.domain.usecases;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import org.crcaguilerapo.franquicia.domain.dtos.Product;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.ports.in.IBranchRepository;
import org.crcaguilerapo.franquicia.domain.ports.in.IProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductUsecaseTest {

    @Mock
    private IProductRepository productRepository;

    @Mock
    private IBranchRepository branchRepository;

    @InjectMocks
    private ProductUsecase productUsecase;


    @Test
    void createProductSuccessfully() {
        Product product = new Product(1, "ProductName", 10, 1);
        Branch branch = new Branch(1, "BranchName", 1);

        when(productRepository.findById(product.id())).thenReturn(Mono.empty());
        when(branchRepository.findById(product.fk_branch())).thenReturn(Mono.just(branch));
        when(productRepository.save(product)).thenReturn(Mono.empty());

        StepVerifier.create(productUsecase.create(product))
                .verifyComplete();

        verify(productRepository, times(1)).findById(product.id());
        verify(branchRepository, times(1)).findById(product.fk_branch());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void createProductWithIncorrectData() {
        Product product = new Product(-1, null, -10, -1);

        StepVerifier.create(productUsecase.create(product))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.DATA_IS_INCORRECT)
                .verify();

        verify(productRepository, never()).findById(anyInt());
        verify(branchRepository, never()).findById(anyInt());
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProductWhenProductAlreadyExists() {
        Product product = new Product(1, "ProductName", 10, 1);

        when(productRepository.findById(product.id())).thenReturn(Mono.just(product));

        StepVerifier.create(productUsecase.create(product))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.PRODUCT_ALREADY_EXISTS)
                .verify();

        verify(productRepository, times(1)).findById(product.id());
        verify(branchRepository, never()).findById(anyInt());
        verify(productRepository, never()).save(any());
    }

    @Test
    void createProductWhenBranchDoesNotExist() {
        Product product = new Product(1, "ProductName", 10, 1);

        when(productRepository.findById(product.id())).thenReturn(Mono.empty());
        when(branchRepository.findById(product.fk_branch())).thenReturn(Mono.empty());

        StepVerifier.create(productUsecase.create(product))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.BRANCH_DOES_NOT_EXISTS)
                .verify();

        verify(productRepository, times(1)).findById(product.id());
        verify(branchRepository, times(1)).findById(product.fk_branch());
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProductSuccessfully() {
        Product product = new Product(1, "ProductName", 10, 1);

        when(productRepository.findById(product.id())).thenReturn(Mono.just(product));
        when(productRepository.update(product)).thenReturn(Mono.just(product));

        StepVerifier
                .create(productUsecase.update(product))
                .expectNext(product)
                .verifyComplete();

        verify(productRepository, times(1)).findById(product.id());
        verify(productRepository, times(1)).update(product);
    }

    @Test
    void updateProductWithIncorrectData() {
        Product product = new Product(-1, null, -10, -1);

        StepVerifier.create(productUsecase.update(product))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.DATA_IS_INCORRECT)
                .verify();

        verify(productRepository, never()).findById(anyInt());
        verify(productRepository, never()).update(any());
    }

    @Test
    void updateProductWhenProductDoesNotExist() {
        Product product = new Product(1, "ProductName", 10, 1);

        when(productRepository.findById(product.id())).thenReturn(Mono.empty());

        StepVerifier.create(productUsecase.update(product))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.PRODUCT_DOES_NOT_EXISTS)
                .verify();

        verify(productRepository, times(1)).findById(product.id());
        verify(productRepository, never()).update(any());
    }

    @Test
    void deleteProductSuccessfully() {
        Product product = new Product(1, "ProductName", 10, 1);

        when(productRepository.delete(product.id())).thenReturn(Mono.empty());

        StepVerifier.create(productUsecase.delete(product.id()))
                .verifyComplete();

        verify(productRepository, times(1)).delete(product.id());
    }

    @Test
    void deleteProductWithIncorrectData() {
        int incorrectProductId = -1;

        StepVerifier.create(productUsecase.delete(incorrectProductId))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.DATA_IS_INCORRECT)
                .verify();

        verify(productRepository, never()).delete(anyInt());
    }

    @Test
    void getMaxStockProductsByBranchSuccessfully() {
        Product product = new Product(1, "ProductName", 10, 1);
        Branch branch = new Branch(1, "BranchName", 1);
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(productRepository.getMaxStockProductsByBranch(franchise.id())).thenReturn(Flux.just(product));
        when(branchRepository.findById(branch.id())).thenReturn(Mono.just(branch));

        StepVerifier
                .create(productUsecase.getMaxStockProductsByBranch(franchise.id()))
                .expectNextMatches(
                        productWithBranch -> productWithBranch.id() == product.id() &&
                        productWithBranch.branch().equals(branch)
                ).verifyComplete();

        verify(productRepository, times(1)).getMaxStockProductsByBranch(franchise.id());
        verify(branchRepository, times(1)).findById(branch.id());
    }

    @Test
    void getMaxStockProductsByBranchWhenBranchDoesNotExist() {
        Product product = new Product(1, "ProductName", 10, 1);
        Branch branch = new Branch(1, "BranchName", 1);
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(productRepository.getMaxStockProductsByBranch(franchise.id())).thenReturn(Flux.just(product));
        when(branchRepository.findById(branch.id())).thenReturn(Mono.empty());

        StepVerifier.create(productUsecase.getMaxStockProductsByBranch(franchise.id()))
                .expectErrorMatches(
                        e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.BRANCH_DOES_NOT_EXISTS)
                .verify();

        verify(productRepository, times(1)).getMaxStockProductsByBranch(franchise.id());
        verify(branchRepository, times(1)).findById(branch.id());
    }
}

