package org.crcaguilerapo.franquicia.domain.usecases;

import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import org.crcaguilerapo.franquicia.domain.exceptions.ErrorCode;
import org.crcaguilerapo.franquicia.domain.exceptions.UseCaseException;
import org.crcaguilerapo.franquicia.domain.ports.in.IFranchiseRepository;
import reactor.core.publisher.Mono;

public class FranchiseUsecase {
    private final IFranchiseRepository franchiseRepository;

    public FranchiseUsecase(IFranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    public Mono<Void> create(Franchise franchise) {
        return Mono.just(franchise).flatMap(v -> {
            if (hasErrorInFields(franchise)) {
                return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
            } else {
                return Mono.just(v);
            }
        }).flatMap(v -> franchiseRepository.findById(v.id()).hasElement().flatMap(b -> {
            if (b) {
                return Mono.error(new UseCaseException(ErrorCode.FRANCHISE_ALREADY_EXISTS));
            } else {
                return Mono.just(v);
            }
        })).flatMap(franchiseRepository::save).then();

    }

    public Mono<Franchise> update(Franchise franchise) {
        return Mono.just(franchise).flatMap(v -> {
            if (hasErrorInFields(franchise)) {
                return Mono.error(new UseCaseException(ErrorCode.DATA_IS_INCORRECT));
            } else {
                return Mono.just(v);
            }
        }).flatMap(v -> franchiseRepository.findById(v.id()).hasElement().flatMap(b -> {
            if (!b) {
                return Mono.error(new UseCaseException(ErrorCode.FRANCHISE_DOES_NOT_EXISTS));
            } else {
                return Mono.just(v);
            }
        })).flatMap(franchiseRepository::update);
    }

    private boolean hasErrorInFields(Franchise franchise) {
        return franchise.id() <= 0 || franchise.name() == null || franchise.name().isEmpty();
    }
}
