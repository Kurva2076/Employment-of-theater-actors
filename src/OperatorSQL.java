public enum OperatorSQL {
    EQ("="),
    LIKE("LIKE"),
    GT(">"),
    LT("<"),
    GE(">="),
    LE("<=");

    private final String sql;

    OperatorSQL(String sql) {
        this.sql = sql;
    }

    public String sql() {
        return sql;
    }
}
