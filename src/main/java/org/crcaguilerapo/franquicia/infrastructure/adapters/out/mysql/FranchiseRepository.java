package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql;

import org.crcaguilerapo.franquicia.domain.dtos.Franchise;
import org.crcaguilerapo.franquicia.domain.ports.in.IFranchiseRepository;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities.FranchiseEntity;
import org.jooq.DSLContext;
import reactor.core.publisher.Mono;

public class FranchiseRepository implements IFranchiseRepository {

    private final DSLContext ctx;


    public FranchiseRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Mono<Void> save(Franchise franchise) {
        return Mono.fromRunnable(() -> {
            ctx
                    .insertInto(FranchiseEntity.table, FranchiseEntity.id, FranchiseEntity.name)
                    .values(franchise.id(), franchise.name())
                    .execute();
        });
    }

    @Override
    public Mono<Franchise> update(Franchise franchise) {
        return Mono.fromRunnable(() -> {
            ctx
                    .update(FranchiseEntity.table)
                    .set(FranchiseEntity.name, franchise.name())
                    .where(FranchiseEntity.id.eq(franchise.id()))
                    .execute();
        });
    }

    @Override
    public Mono<Franchise> findById(int franchiseId) {
        var franchise = ctx.select(FranchiseEntity.id, FranchiseEntity.name)
                .from(FranchiseEntity.table)
                .where(FranchiseEntity.id.eq(franchiseId))
                .fetchOneInto(Franchise.class);

        if (franchise != null) {
            return Mono.just(franchise);
        } else {
            return Mono.empty();
        }
    }
}
