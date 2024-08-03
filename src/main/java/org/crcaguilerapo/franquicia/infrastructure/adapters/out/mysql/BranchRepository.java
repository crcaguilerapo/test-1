package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql;

import org.crcaguilerapo.franquicia.domain.dtos.Branch;
import org.crcaguilerapo.franquicia.domain.ports.in.IBranchRepository;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities.BranchEntity;
import org.jooq.DSLContext;
import reactor.core.publisher.Mono;

public class BranchRepository implements IBranchRepository {

    private final DSLContext ctx;

    public BranchRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Mono<Void> save(Branch branch) {
        return Mono.fromRunnable(() -> {
            ctx
                    .insertInto(BranchEntity.table, BranchEntity.id, BranchEntity.name, BranchEntity.fkFranchise)
                    .values(branch.id(), branch.name(), branch.fk_franchise())
                    .execute();
        });
    }

    @Override
    public Mono<Branch> update(Branch branch) {
        return Mono.fromRunnable(() -> {
            ctx
                    .update(BranchEntity.table)
                    .set(BranchEntity.name, branch.name())
                    .set(BranchEntity.fkFranchise, branch.fk_franchise())
                    .where(BranchEntity.id.eq(branch.id()))
                    .execute();
        });
    }

    @Override
    public Mono<Branch> findById(int branchId) {
        var branch = ctx.select(BranchEntity.id, BranchEntity.name, BranchEntity.fkFranchise)
                .from(BranchEntity.table)
                .where(BranchEntity.id.eq(branchId))
                .fetchOneInto(Branch.class);

        if (branch != null) {
            return Mono.just(branch);
        } else {
            return Mono.empty();
        }
    }

}
