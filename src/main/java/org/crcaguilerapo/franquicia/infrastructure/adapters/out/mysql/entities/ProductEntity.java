package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class ProductEntity {
    public static Table<Record> table = table("PRODUCT");
    public static Field<Integer> id = field("product_id", SQLDataType.INTEGER);
    public static Field<String> name = field("product_name", SQLDataType.VARCHAR(30));
    public static Field<Integer> stock = field("product_stock", SQLDataType.INTEGER);
    public static Field<Integer> fkBranch = field("product_fk_branch", SQLDataType.INTEGER);
}
