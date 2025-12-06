import java.sql.*;
import java.util.*;

public class ActorRepDB {

    private final String url;
    private final String user;
    private final String password;

    public ActorRepDB(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Получить Actor по ID
     */
    public Actor getById(long id) {
        String sql = "SELECT * FROM actor WHERE actor_id = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return extractActor(rs, conn);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getById", e);
        }
    }

    /**
     * k-й список n элементов (PublicActor)
     */
    public List<PublicActor> getKNShortList(int k, int n) {
        int offset = (k - 1) * n;

        String sql = """
            SELECT surname, firstname, patronymic, phone
            FROM actor
            ORDER BY actor_id
            LIMIT ? OFFSET ?
            """;

        List<PublicActor> list = new ArrayList<>();

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, n);
            ps.setInt(2, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new PublicActor(
                        rs.getString("surname"),
                        rs.getString("firstname"),
                        rs.getString("patronymic"),
                        rs.getString("phone")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getKNShortList", e);
        }

        return list;
    }

    /**
     * Добавить Actor (с авто-ID)
     */
    public Actor add(Actor actor) {
        String sqlActor = """
            INSERT INTO actor (surname, firstname, patronymic, phone, work_experience, contract_id)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING actor_id
            """;

        try (Connection conn = connect()) {
            conn.setAutoCommit(true);

            Long contractId = saveContract(actor.getContract(), conn);
            long actorId;

            try (PreparedStatement ps = conn.prepareStatement(sqlActor)) {
                ps.setString(1, actor.getSurname());
                ps.setString(2, actor.getFirstname());
                ps.setString(3, actor.getPatronymic());
                ps.setString(4, actor.getPhone());
                ps.setLong(5, actor.getWorkExperience().getDays());
                ps.setLong(6, contractId);

                ResultSet rs = ps.executeQuery();
                rs.next();
                actorId = rs.getLong(1);
            }

            System.out.println("actorID " + actorId);

            saveTitles(actorId, actor.getActorTitles(), conn);
            saveAwards(actorId, actor.getActorAwards(), conn);

            conn.commit();

            try {
                var field = Actor.class.getDeclaredField("actorId");
                field.setAccessible(true);
                field.set(actor, (int) actorId);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось установить новый actorId", e);
            }

            return actor;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка add", e);
        }
    }

    /**
     * Заменить Actor по ID
     */
    public void update(long id, Actor actor) {

        String sqlActor = """
            UPDATE actor
            SET surname=?, firstname=?, patronymic=?, phone=?, work_experience=?, contract_id=?
            WHERE actor_id=?
            """;

        try (Connection conn = connect()) {

            conn.setAutoCommit(false);

            Long contractId = saveContract(actor.getContract(), conn);

            try (PreparedStatement ps = conn.prepareStatement(sqlActor)) {
                ps.setString(1, actor.getSurname());
                ps.setString(2, actor.getFirstname());
                ps.setString(3, actor.getPatronymic());
                ps.setString(4, actor.getPhone());
                ps.setLong(5, actor.getWorkExperience().getDays());
                ps.setObject(6, contractId);
                ps.setLong(7, id);
                ps.executeUpdate();
            }

            // Очистить связи
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM actor_actor_title WHERE actor_id=?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM actor_actor_award WHERE actor_id=?")) {
                ps.setLong(1, id);
                ps.executeUpdate();
            }

            // Добавить новые
            saveTitles(id, actor.getActorTitles(), conn);
            saveAwards(id, actor.getActorAwards(), conn);

            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка update", e);
        }
    }

    /**
     * Удалить Actor
     */
    public void delete(long id) {
        String sql = "DELETE FROM actor WHERE actor_id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка delete", e);
        }
    }

    /**
     * Получить количество
     */
    public long getCount() {
        String sql = "SELECT COUNT(*) FROM actor";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getCount", e);
        }
    }

    /**
     * ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
     */
    private Actor extractActor(ResultSet rs, Connection conn) throws SQLException {
        long actorId = rs.getLong("actor_id");

        String surname = rs.getString("surname");
        String firstname = rs.getString("firstname");
        String patronymic = rs.getString("patronymic");
        String phone = rs.getString("phone");
        long work = rs.getLong("work_experience");
        long contractId = rs.getLong("contract_id");

        Contract contract = null;
        if (contractId != 0) {
            contract = getContract(contractId, conn);
        }

        List<ActorTitle> titles = getTitles(actorId, conn);
        List<ActorAward> awards = getAwards(actorId, conn);

        return new Actor(
                actorId,
                surname,
                firstname,
                patronymic,
                phone,
                new WorkExperience((int) work),
                contract,
                titles,
                awards
        );
    }

    private Long saveContract(Contract contract, Connection conn) throws SQLException {
        if (contract == null) return null;

        String sql = "INSERT INTO contract(amount) VALUES(?) RETURNING contract_id";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, contract.getAmount());
            ResultSet rs = ps.executeQuery();
            rs.next();
            long id = rs.getLong(1);
            try {
                var field = Contract.class.getDeclaredField("contractId");
                field.setAccessible(true);
                field.set(contract, (int) id);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось установить новый contractId", e);
            }
            return id;
        }
    }

    private Contract getContract(long id, Connection conn) throws SQLException {
        String sql = "SELECT amount FROM contract WHERE contract_id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            String amount = rs.getString("amount");
            return new Contract(id, amount);
        }
    }

    private void saveTitles(long actorId, List<ActorTitle> list, Connection conn) throws SQLException {
        if (list == null) return;

        for (ActorTitle t : list) {
            long titleId = insertOrGetTitle(t, conn);

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO actor_actor_title(actor_id, title_id) VALUES (?,?)"
            )) {
                ps.setLong(1, actorId);
                ps.setLong(2, titleId);
                long row = ps.executeUpdate();
                System.out.println("Число вставленных строк в actor_actor_title: " + row);
            }
        }
    }

    private long insertOrGetTitle(ActorTitle t, Connection conn) throws SQLException {

        String sql = """
        INSERT INTO actor_title(title_name)
        VALUES (?)
        ON CONFLICT(title_name)
        DO UPDATE SET title_name = EXCLUDED.title_name
        RETURNING title_id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getTitleName());
            ResultSet rs = ps.executeQuery();
            rs.next();
            long id = rs.getLong(1);

            // Присвоим настоящий ID объекту
            try {
                var field = ActorTitle.class.getDeclaredField("titleId");
                field.setAccessible(true);
                field.set(t, (int) id);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось установить titleId", e);
            }

            return id;
        }
    }

    private List<ActorTitle> getTitles(long actorId, Connection conn) throws SQLException {
        String sql = """
            SELECT t.title_id, t.title_name
            FROM actor_title t
            JOIN actor_actor_title aat ON t.title_id = aat.title_id
            WHERE aat.actor_id = ?
            """;

        List<ActorTitle> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, actorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long titleId = rs.getLong("title_id");
                String titleName = rs.getString("title_name");
                list.add(new ActorTitle(titleId, titleName));
            }
        }
        return list;
    }

    private void saveAwards(long actorId, List<ActorAward> list, Connection conn) throws SQLException {
        if (list == null) return;

        for (ActorAward a : list) {
            long awardId = insertOrGetAward(a, conn);

            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO actor_actor_award(actor_id, award_id) VALUES (?,?)"
            )) {
                ps.setLong(1, actorId);
                ps.setLong(2, awardId);
                long row = ps.executeUpdate();
                System.out.println("Число вставленных строк в actor_actor_award: " + row);
            }
        }
    }

    private long insertOrGetAward(ActorAward a, Connection conn) throws SQLException {

        String sql = """
        INSERT INTO actor_award(award_name)
        VALUES (?)
        ON CONFLICT(award_name)
        DO UPDATE SET award_name = EXCLUDED.award_name
        RETURNING award_id
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getAwardName());
            ResultSet rs = ps.executeQuery();
            rs.next();
            long id = rs.getLong(1);

            // Установим правильный ID в объект
            try {
                var field = ActorAward.class.getDeclaredField("awardId");
                field.setAccessible(true);
                field.set(a, (int) id);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось установить awardId", e);
            }

            return id;
        }
    }

    private List<ActorAward> getAwards(long actorId, Connection conn) throws SQLException {
        String sql = """
            SELECT a.award_id, a.award_name
            FROM actor_award a
            JOIN actor_actor_award aaa ON a.award_id = aaa.award_id
            WHERE aaa.actor_id = ?
            """;

        List<ActorAward> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, actorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long awardId = rs.getLong("award_id");
                String awardName = rs.getString("award_name");
                list.add(new ActorAward(awardId, awardName));
            }
        }
        return list;
    }
}
