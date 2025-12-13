import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ActorRepDB implements ActorRepository {
    private final DatabaseManager db;

    public ActorRepDB(DatabaseManager db) {
        this.db = db;
    }

    public Actor getById(long id) {
        try {
            Map<String, Object> row = db.fetchOne(
                    "SELECT * FROM actor WHERE actor_id = ?",
                    id
            );
            if (row == null) return null;

            return extractActor(row);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getById", e);
        }
    }

    public List<PublicActor> getKNShortList(int k, int n, SqlQueryContext ctx) {
        int offset = (k - 1) * n;
        String sql = ctx.apply("SELECT surname, firstname, patronymic, phone FROM actor") + " LIMIT ? OFFSET ?";

        Object[] allParams = Arrays.copyOf(ctx.getParams(), ctx.getParams().length + 2);
        allParams[ctx.getParams().length] = n;
        allParams[ctx.getParams().length + 1] = offset;

        try {
            List<Map<String, Object>> rows = db.fetchAll(sql, allParams);

            List<PublicActor> list = new ArrayList<>();
            for (var r : rows) {
                list.add(new PublicActor(
                        (String) r.get("surname"),
                        (String) r.get("firstname"),
                        (String) r.get("patronymic"),
                        (String) r.get("phone")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getKNShortList", e);
        }
    }

    public List<PublicActor> getKNShortList(int k, int n) {
        return getKNShortList(k, n, new SqlQueryContext());
    }

    public Actor add(Actor actor) {
        try {
            db.begin();

            long contractId = saveContract(actor.getContract());

            long actorId = db.insertReturningId(
                    "INSERT INTO actor (surname, firstname, patronymic, phone, work_experience, contract_id) " +
                            "VALUES (?, ?, ?, ?, ?, ?) RETURNING actor_id",
                    actor.getSurname(),
                    actor.getFirstname(),
                    actor.getPatronymic(),
                    actor.getPhone(),
                    actor.getWorkExperience().getDays(),
                    contractId
            );

            setPrivateField(actor, "actorId", (int) actorId);

            saveTitles(actorId, actor.getActorTitles());
            saveAwards(actorId, actor.getActorAwards());

            db.commit();
            return actor;
        } catch (Exception e) {
            db.rollback();
            throw new RuntimeException("Ошибка add", e);
        }
    }

    public boolean update(long id, Actor actor) {
        try {
            db.begin();

            long contractId = saveContract(actor.getContract());

            db.execute(
                    "UPDATE actor SET surname=?, firstname=?, patronymic=?, phone=?, work_experience=?, contract_id=? " +
                            "WHERE actor_id=?",
                    actor.getSurname(),
                    actor.getFirstname(),
                    actor.getPatronymic(),
                    actor.getPhone(),
                    actor.getWorkExperience().getDays(),
                    contractId,
                    id
            );

            db.execute("DELETE FROM actor_actor_title WHERE actor_id=?", id);
            db.execute("DELETE FROM actor_actor_award WHERE actor_id=?", id);

            saveTitles(id, actor.getActorTitles());
            saveAwards(id, actor.getActorAwards());

            db.commit();
        } catch (Exception e) {
            db.rollback();
            throw new RuntimeException("Ошибка update", e);
        }
        return true;
    }

    public boolean delete(long id) {
        try {
            db.execute("DELETE FROM actor WHERE actor_id=?", id);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка delete", e);
        }
        return true;
    }

    public long getCount(SqlQueryContext ctx) {
        String sql = ctx.apply("SELECT COUNT(*) FROM actor");

        try {
            Long count = db.fetchScalar(sql, ctx.getParams());
            return count != null ? count : 0L;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка getCount", e);
        }
    }

    public long getCount() {
        return getCount(new SqlQueryContext());
    }

    private Actor extractActor(Map<String, Object> row) throws SQLException {

        int actorId = (int) row.get("actor_id");

        int contractId = (int) row.get("contract_id");

        Contract contract = contractId == 0 ? null : getContract(contractId);

        List<ActorTitle> titles = getTitles(actorId);
        List<ActorAward> awards = getAwards(actorId);

        return new Actor(
                actorId,
                (String) row.get("surname"),
                (String) row.get("firstname"),
                (String) row.get("patronymic"),
                (String) row.get("phone"),
                new WorkExperience(((Number) row.get("work_experience")).intValue()),
                contract,
                titles,
                awards
        );
    }

    private long saveContract(Contract c) throws SQLException {
        if (c == null) return 0;

        long id = db.insertReturningId(
                "INSERT INTO contract(amount) VALUES(?) RETURNING contract_id",
                c.getAmount()
        );

        setPrivateField(c, "contractId", (int) id);

        return id;
    }

    private Contract getContract(long id) throws SQLException {
        Map<String, Object> row = db.fetchOne(
                "SELECT amount FROM contract WHERE contract_id=?",
                id
        );
        if (row == null) return null;

        return new Contract(id, row.get("amount").toString());
    }

    private void saveTitles(long actorId, List<ActorTitle> list) throws SQLException {
        if (list == null) return;

        for (ActorTitle t : list) {

            long titleId = db.insertReturningId(
                    "INSERT INTO actor_title(title_name) VALUES(?) " +
                            "ON CONFLICT(title_name) DO UPDATE SET title_name=EXCLUDED.title_name " +
                            "RETURNING title_id",
                    t.getTitleName()
            );

            setPrivateField(t, "titleId", (int) titleId);

            db.execute(
                    "INSERT INTO actor_actor_title(actor_id, title_id) VALUES(?, ?)",
                    actorId, titleId
            );
        }
    }

    private List<ActorTitle> getTitles(long actorId) throws SQLException {
        List<Map<String, Object>> rows = db.fetchAll(
                "SELECT t.title_id, t.title_name FROM actor_title t " +
                        "JOIN actor_actor_title a ON a.title_id = t.title_id " +
                        "WHERE a.actor_id=?",
                actorId
        );

        List<ActorTitle> list = new ArrayList<>();
        for (var r : rows) {
            list.add(new ActorTitle(
                    (int) r.get("title_id"),
                    r.get("title_name")
            ));
        }
        return list;
    }

    private void saveAwards(long actorId, List<ActorAward> list) throws SQLException {
        if (list == null) return;

        for (ActorAward t : list) {

            long id = db.insertReturningId(
                    "INSERT INTO actor_award(award_name) VALUES(?) " +
                            "ON CONFLICT(award_name) DO UPDATE SET award_name=EXCLUDED.award_name " +
                            "RETURNING award_id",
                    t.getAwardName()
            );

            setPrivateField(t, "awardId", (int) id);

            db.execute(
                    "INSERT INTO actor_actor_award(actor_id, award_id) VALUES(?, ?)",
                    actorId, id
            );
        }
    }

    private List<ActorAward> getAwards(long actorId) throws SQLException {
        List<Map<String, Object>> rows = db.fetchAll(
                "SELECT a.award_id, a.award_name FROM actor_award a " +
                        "JOIN actor_actor_award aw ON aw.award_id = a.award_id " +
                        "WHERE aw.actor_id=?",
                actorId
        );

        List<ActorAward> list = new ArrayList<>();
        for (var r : rows) {
            list.add(new ActorAward(
                    (int) r.get("award_id"),
                    r.get("award_name")
            ));
        }
        return list;
    }

    private void setPrivateField(Object obj, String name, int value) {
        try {
            var field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка присвоения приватного поля " + name, e);
        }
    }
}
