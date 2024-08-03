package org.crcaguilerapo.franquicia.domain.usecases;

import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.ports.in.IFranchiseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FranchiseUsecaseTest {

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private FranchiseUsecase franchiseUsecase;


    @Test
    void createFranchiseSuccessfully() {
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(franchiseRepository.findById(franchise.id())).thenReturn(Mono.empty());
        when(franchiseRepository.save(franchise)).thenReturn(Mono.empty());

        StepVerifier
                .create(franchiseUsecase.create(franchise))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(franchise.id());
        verify(franchiseRepository, times(1)).save(franchise);
    }

    @Test
    void createFranchiseWithIncorrectData() {
        Franchise franchise = new Franchise(-1, null);

        StepVerifier.create(franchiseUsecase.create(franchise))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.DATA_IS_INCORRECT)
                .verify();

        verify(franchiseRepository, never()).findById(anyInt());
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void createFranchiseWhenFranchiseAlreadyExists() {
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(franchiseRepository.findById(franchise.id())).thenReturn(Mono.just(franchise));

        StepVerifier
                .create(franchiseUsecase.create(franchise))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.FRANCHISE_ALREADY_EXISTS)
                .verify();

        verify(franchiseRepository, times(1)).findById(franchise.id());
        verify(franchiseRepository, never()).save(any());
    }

    @Test
    void updateFranchiseSuccessfully() {
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(franchiseRepository.findById(franchise.id())).thenReturn(Mono.just(franchise));
        when(franchiseRepository.update(franchise)).thenReturn(Mono.just(franchise));

        StepVerifier
                .create(franchiseUsecase.update(franchise))
                .expectNext(franchise)
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(franchise.id());
        verify(franchiseRepository, times(1)).update(franchise);
    }

    @Test
    void updateFranchiseWithIncorrectData() {
        Franchise franchise = new Franchise(-1, null);

        StepVerifier
                .create(franchiseUsecase.update(franchise))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.DATA_IS_INCORRECT)
                .verify();

        verify(franchiseRepository, never()).findById(anyInt());
        verify(franchiseRepository, never()).update(any());
    }

    @Test
    void updateFranchiseWhenFranchiseDoesNotExist() {
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(franchiseRepository.findById(franchise.id())).thenReturn(Mono.empty());

        StepVerifier.create(franchiseUsecase.update(franchise))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.FRANCHISE_DOES_NOT_EXISTS)
                .verify();

        verify(franchiseRepository, times(1)).findById(franchise.id());
        verify(franchiseRepository, never()).update(any());
    }
}

