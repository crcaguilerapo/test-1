package org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.entities;

import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class BranchEntity {
    public static Table<Record> table = table("BRANCH");
    public static Field<Integer> id = field("branch_id", SQLDataType.INTEGER);
    public static Field<String> name = field("branch_name", SQLDataType.VARCHAR(30));
    public static Field<Integer> fkFranchise = field("branch_fk_franchise", SQLDataType.INTEGER);
}
