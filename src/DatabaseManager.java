import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static DatabaseManager instance;

    private final String url;
    private final String user;
    private final String password;

    private Connection conn;

    private DatabaseManager(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static synchronized DatabaseManager getInstance(String url, String user, String password) {
        if (instance == null) {
            instance = new DatabaseManager(url, user, password);
        }
        return instance;
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("DatabaseManager должен быть инициализирован");
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(true);
        }
        return conn;
    }

    public void begin() throws SQLException {
        getConnection().setAutoCommit(false);
    }

    public void commit() throws SQLException {
        getConnection().commit();
        getConnection().setAutoCommit(true);
    }

    public void rollback() {
        try {
            getConnection().rollback();
            getConnection().setAutoCommit(true);
        } catch (SQLException ignored) {}
    }

    public int execute(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params)) {
            return ps.executeUpdate();
        }
    }

    public long insertReturningId(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T fetchScalar(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? (T) rs.getObject(1) : null;
        }
    }

    public Map<String, Object> fetchOne(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next())
                return null;

            return row(rs);
        }
    }

    public List<Map<String,Object>> fetchAll(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepare(sql, params);
             ResultSet rs = ps.executeQuery()) {

            List<Map<String,Object>> list = new ArrayList<>();
            while (rs.next()) list.add(row(rs));
            return list;
        }
    }

    private PreparedStatement prepare(String sql, Object[] params) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement(sql);
        for (int i = 0; i < params.length; i++)
            ps.setObject(i + 1, params[i]);
        return ps;
    }

    private Map<String,Object> row(ResultSet rs) throws SQLException {
        Map<String,Object> map = new LinkedHashMap<>();
        ResultSetMetaData md = rs.getMetaData();
        for (int i = 1; i <= md.getColumnCount(); i++)
            map.put(md.getColumnLabel(i), rs.getObject(i));
        return map;
    }
}
