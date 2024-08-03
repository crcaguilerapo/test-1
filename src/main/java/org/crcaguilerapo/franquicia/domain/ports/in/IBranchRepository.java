package org.crcaguilerapo.franquicia.domain.ports.in;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import reactor.core.publisher.Mono;

public interface IBranchRepository {
    Mono<Void> save(Branch branch);

    Mono<Branch> update(Branch branch);

    Mono<Branch> findById(int branchId);

}
