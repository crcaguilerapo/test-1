package org.crcaguilerapo.franquicia.domain.usecases;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.ports.in.IBranchRepository;
import org.crcaguilerapo.franquicia.domain.ports.in.IFranchiseRepository;
import reactor.core.publisher.Mono;

public class BranchUsecase {
    private final IBranchRepository branchRepository;
    private final IFranchiseRepository franchiseRepository;


    public BranchUsecase(
            IBranchRepository branchRepository,
            IFranchiseRepository franchiseRepository
    ) {
        this.branchRepository = branchRepository;
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Void> create(Branch branch) {
        return Mono.just(branch).flatMap(v -> {
                    if (hasErrorInFields(v)) {
                        return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
                    }
                    return Mono.just(v);
                }).flatMap(v -> franchiseRepository.findById(v.fk_franchise()).hasElement().flatMap(b -> {
                    if (!b) {
                        return Mono.error(new UseCaseException(ErrorCode.FRANCHISE_DOES_NOT_EXISTS));
                    } else {
                        return Mono.just(v);
                    }
                })
                ).flatMap(v -> branchRepository.findById(v.id()).hasElement().flatMap(b -> {
                    if (b) {
                        return Mono.error(new UseCaseException(ErrorCode.BRANCH_ALREADY_EXISTS));
                    } else {
                        return Mono.just(v);
                    }
                })).flatMap(v -> branchRepository.save(branch));
    }

    public Mono<Branch> update(Branch branch) {
        return Mono.just(branch).flatMap(v -> {
                    if (hasErrorInFields(v)) {
                        return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
                    }
                    return Mono.just(v);
                }).flatMap(v -> branchRepository.findById(v.id()).hasElement().flatMap(b -> {
                    if (!b) {
                        return Mono.error(new UseCaseException(ErrorCode.BRANCH_DOES_NOT_EXISTS));
                    } else {
                        return Mono.just(v);
                    }
                })).flatMap(v -> branchRepository.update(branch));
    }

    private boolean hasErrorInFields(Branch branch) {
        return branch.id() <= 0 || branch.name() == null || branch.fk_franchise() <= 0;
    }
}
