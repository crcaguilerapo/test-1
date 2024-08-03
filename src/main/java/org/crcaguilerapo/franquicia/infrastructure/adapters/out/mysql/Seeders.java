package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql;

import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities.BranchEntity;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities.FranchiseEntity;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities.ProductEntity;
import org.jooq.DSLContext;

public class Seeders {
    private final DSLContext ctx;

    public Seeders(DSLContext ctx) {
        this.ctx = ctx;
    }


    public void start() {
        ctx.dropTableIfExists(FranchiseEntity.table).execute();
        ctx.dropTableIfExists(BranchEntity.table).execute();
        ctx.dropTableIfExists(ProductEntity.table).execute();

        ctx
                .createTable(FranchiseEntity.table)
                .column(FranchiseEntity.id)
                .column(FranchiseEntity.name)
                .execute();


        ctx
                .insertInto(FranchiseEntity.table, FranchiseEntity.id, FranchiseEntity.name)
                .values(1, "Subway")
                .values(2, "Starbucks")
                .execute();

        ctx
                .createTable(BranchEntity.table)
                .column(BranchEntity.id)
                .column(BranchEntity.name)
                .column(BranchEntity.fkFranchise)
                .execute();


        ctx
                .insertInto(BranchEntity.table, BranchEntity.id, BranchEntity.name, BranchEntity.fkFranchise)
                .values(1, "Bogotá", 1)
                .values(2, "Medellin", 1)
                .values(3, "Bogotá", 2)
                .values(4, "Medellin", 2)
                .execute();

        ctx
                .createTable(ProductEntity.table)
                .column(ProductEntity.id)
                .column(ProductEntity.name)
                .column(ProductEntity.stock)
                .column(ProductEntity.fkBranch)
                .execute();

        ctx
                .insertInto(ProductEntity.table, ProductEntity.id, ProductEntity.name, ProductEntity.stock, ProductEntity.fkBranch)
                .values(1, "Emparedado", 50, 1)
                .values(2, "Ensalada", 20, 1)
                .values(3, "Bebida", 100, 1)
                .values(4, "Emparedado", 60, 2)
                .values(5, "Ensalada", 10, 2)
                .values(6, "Bebida", 80, 2)
                .values(7, "Postre", 80, 4)
                .values(8, "Cafe", 120, 4)
                .values(9, "Pan", 40, 4)
                .execute();

    }
}
