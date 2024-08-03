package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql;

import org.crcaguilerapo.franquicia.domain.dtos.Product;
import org.crcaguilerapo.franquicia.domain.ports.in.IProductRepository;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities.ProductEntity;
import org.jooq.DSLContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductRepository implements IProductRepository  {

    private final DSLContext ctx;

    public ProductRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Mono<Void> save(Product product) {
        return Mono.fromRunnable(() -> {
            ctx
                    .insertInto(ProductEntity.table, ProductEntity.id, ProductEntity.name, ProductEntity.stock, ProductEntity.fkBranch)
                    .values(product.id(), product.name(), product.stock(), product.fk_branch())
                    .execute();
        });
    }

    @Override
    public Mono<Void> delete(int productId) {
        return Mono.fromRunnable(() -> {
            ctx
                    .delete(ProductEntity.table)
                    .where(ProductEntity.id.eq(productId))
                    .execute();
        });
    }

    @Override
    public Mono<Product> update(Product product) {
        return Mono.fromRunnable(() -> {
            ctx
                    .update(ProductEntity.table)
                    .set(ProductEntity.name, product.name())
                    .set(ProductEntity.stock, product.stock())
                    .set(ProductEntity.fkBranch, product.fk_branch())
                    .where(ProductEntity.id.eq(product.id()))
                    .execute();
        });
    }

    @Override
    public Mono<Product> findById(int productId) {
        var product = ctx.select(ProductEntity.id, ProductEntity.name, ProductEntity.stock, ProductEntity.fkBranch)
                .from(ProductEntity.table)
                .where(ProductEntity.id.eq(productId))
                .fetchOneInto(Product.class);

        if (product != null) {
            return Mono.just(product);
        } else {
            return Mono.empty();
        }
    }

    @Override
    public Flux<Product> getMaxStockProductsByBranch(int franchiseId) {
        String sql = """
                SELECT
                    PRODUCT.product_id,
                    PRODUCT.product_name,
                    PRODUCT.product_stock,
                    PRODUCT.product_fk_branch
                FROM PRODUCT
                INNER JOIN BRANCH ON
                    PRODUCT.product_fk_branch = BRANCH.branch_id
                INNER JOIN (
                    SELECT
                        product_fk_branch,
                        MAX(product_stock) AS max_stock
                    FROM PRODUCT
                    GROUP BY product_fk_branch
                ) AS temp ON
                    PRODUCT.product_fk_branch = temp.product_fk_branch
                    AND PRODUCT.product_stock = temp.max_stock
                WHERE BRANCH.branch_fk_franchise = ?;
                """;
        var result = ctx.fetchStream(sql, franchiseId);

        var mapResult = result
                .map(record -> new Product(
                        record.get("product_id", Integer.class),
                        record.get("product_name", String.class),
                        record.get("product_stock", Integer.class),
                        record.get("product_fk_branch", Integer.class)
                ));

        return Flux.fromIterable(mapResult.toList());
    }
}
