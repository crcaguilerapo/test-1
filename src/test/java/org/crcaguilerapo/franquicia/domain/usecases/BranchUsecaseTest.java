package org.crcaguilerapo.franquicia.domain.usecases;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.ports.in.IBranchRepository;
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
public class BranchUsecaseTest {

    @Mock
    private IBranchRepository branchRepository;

    @Mock
    private IFranchiseRepository franchiseRepository;

    @InjectMocks
    private BranchUsecase branchUsecase;


    @Test
    void createBranchSuccessfully() {
        Branch branch = new Branch(1, "BranchName", 1);
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(franchiseRepository.findById(branch.fk_franchise())).thenReturn(Mono.just(franchise));
        when(branchRepository.findById(branch.id())).thenReturn(Mono.empty());
        when(branchRepository.save(branch)).thenReturn(Mono.empty());

        StepVerifier.create(branchUsecase.create(branch))
                .verifyComplete();

        verify(franchiseRepository, times(1)).findById(branch.fk_franchise());
        verify(branchRepository, times(1)).findById(branch.id());
        verify(branchRepository, times(1)).save(branch);
    }

    @Test
    void createBranchWithIncorrectData() {
        Branch branch = new Branch(-1, null, -1);

        StepVerifier
                .create(branchUsecase.create(branch))
                .expectErrorMatches(
                        e -> e instanceof UseCaseException &&
                                ((UseCaseException) e).errorCode == ErrorCode.DATA_IS_INCORRECT
                ).verify();

        verify(franchiseRepository, never()).findById(anyInt());
        verify(branchRepository, never()).findById(anyInt());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranchWhenFranchiseDoesNotExist() {
        Branch branch = new Branch(1, "BranchName", 1);

        when(franchiseRepository.findById(branch.fk_franchise())).thenReturn(Mono.empty());

        StepVerifier.create(branchUsecase.create(branch))
                .expectErrorMatches(
                        e -> e instanceof UseCaseException &&
                                ((UseCaseException) e).errorCode == ErrorCode.FRANCHISE_DOES_NOT_EXISTS
                )
                .verify();

        verify(franchiseRepository, times(1)).findById(branch.fk_franchise());
        verify(branchRepository, never()).findById(anyInt());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void createBranchWhenBranchAlreadyExists() {
        Branch branch = new Branch(1, "BranchName", 1);
        Franchise franchise = new Franchise(1, "FranchiseName");

        when(franchiseRepository.findById(branch.fk_franchise())).thenReturn(Mono.just(franchise));
        when(branchRepository.findById(branch.id())).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUsecase.create(branch))
                .expectErrorMatches(e -> e instanceof UseCaseException &&
                        ((UseCaseException) e).errorCode == ErrorCode.BRANCH_ALREADY_EXISTS)
                .verify();

        verify(franchiseRepository, times(1)).findById(branch.fk_franchise());
        verify(branchRepository, times(1)).findById(branch.id());
        verify(branchRepository, never()).save(any());
    }

    @Test
    void updateBranchSuccessfully() {
        Branch branch = new Branch(1, "BranchName", 1);

        when(branchRepository.findById(branch.id())).thenReturn(Mono.just(branch));
        when(branchRepository.update(branch)).thenReturn(Mono.just(branch));

        StepVerifier.create(branchUsecase.update(branch))
                .expectNext(branch)
                .verifyComplete();

        verify(branchRepository, times(1)).findById(branch.id());
        verify(branchRepository, times(1)).update(branch);
    }

    @Test
    void updateBranchWithIncorrectData() {
        Branch branch = new Branch(-1, null, -1);

        StepVerifier.create(branchUsecase.update(branch))
                .expectErrorMatches(throwable -> throwable instanceof UseCaseException &&
                        ((UseCaseException) throwable).errorCode == ErrorCode.DATA_IS_INCORRECT)
                .verify();

        verify(branchRepository, never()).findById(anyInt());
        verify(branchRepository, never()).update(any());
    }

    @Test
    void updateBranchWhenBranchDoesNotExist() {
        Branch branch = new Branch(1, "BranchName", 1);

        when(branchRepository.findById(branch.id())).thenReturn(Mono.empty());

        StepVerifier.create(branchUsecase.update(branch))
                .expectErrorMatches(throwable -> throwable instanceof UseCaseException &&
                        ((UseCaseException) throwable).errorCode == ErrorCode.BRANCH_DOES_NOT_EXISTS)
                .verify();

        verify(branchRepository, times(1)).findById(branch.id());
        verify(branchRepository, never()).update(any());
    }
}


