package utils;

import java.util.ArrayList;
import java.util.List;

public class SqlQueryContext {
    private final List<String> whereParts = new ArrayList<>();
    private final List<Object> params = new ArrayList<>();
    private String orderBy = "";

    public void addWhere(String field, OperatorSQL op, Object value) {
        whereParts.add(field + " " + op.sql() + " ?");
        params.add(value);
    }

    public void setOrderBy(String field, Sort sort) {
        this.orderBy = " ORDER BY " + field + " " + sort.name();
    }

    public String apply(String baseSql) {
        StringBuilder sql = new StringBuilder(baseSql);

        if (!whereParts.isEmpty()) {
            sql.append(" WHERE ")
                    .append(String.join(" AND ", whereParts));
        }
        if (!baseSql.startsWith("SELECT COUNT(*)")) {
            sql.append(orderBy);
        }

        return sql.toString();
    }

    public Object[] getParams() {
        return params.toArray();
    }
}
