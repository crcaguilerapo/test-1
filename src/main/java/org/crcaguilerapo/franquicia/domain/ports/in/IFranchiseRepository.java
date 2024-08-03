package org.crcaguilerapo.franquicia.domain.ports.in;

import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface IFranchiseRepository {
    Mono<Void> save(Franchise franchise);

    Mono<Franchise> update(Franchise franchise);

    Mono<Franchise> findById(int franchiseId);
}
