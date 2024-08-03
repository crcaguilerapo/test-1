package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class FranchiseEntity {

    public static Table<Record> table = table("FRANCHISE");
    public static Field<Integer> id = field("franchise_id", SQLDataType.INTEGER);
    public static Field<String> name = field("franchise_name", SQLDataType.VARCHAR(30));
}
