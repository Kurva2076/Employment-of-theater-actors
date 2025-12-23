package repository.interactions;

import model.*;
import repository.interfaces.ActorRep;
import utils.DatabaseManager;
import utils.SqlQueryContext;

import java.sql.SQLException;
import java.util.*;

public class ActorRepDB implements ActorRep {
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
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<PublicActor> getKNShortList(int k, int n, SqlQueryContext ctx) {
        int offset = (k - 1) * n;
        String sql = ctx.apply("SELECT actor_id, surname, firstname, patronymic, phone FROM actor") + " LIMIT ? OFFSET ?";

        Object[] allParams = Arrays.copyOf(ctx.getParams(), ctx.getParams().length + 2);
        allParams[ctx.getParams().length] = n;
        allParams[ctx.getParams().length + 1] = offset;

        try {
            List<Map<String, Object>> rows = db.fetchAll(sql, allParams);

            List<PublicActor> list = new ArrayList<>();
            for (var r : rows) {
                list.add(new PublicActor(
                        (Integer) r.get("actor_id"),
                        (String) r.get("surname"),
                        (String) r.get("firstname"),
                        (String) r.get("patronymic"),
                        (String) r.get("phone")
                ));
            }
            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<PublicActor> getKNShortList(int k, int n) {
        return getKNShortList(k, n, new SqlQueryContext());
    }

    public Actor add(Actor actor) {
        try {
//            db.begin();
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
            return actor;
        } catch (Exception e) {
            db.rollback();
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean update(long id, Actor actor) {
        return update(id, actor.toSimpleMap());
    }

    public boolean update(long id, Map<String, Object> updatedFields) {
        Actor actor = getById(id);
        long contractId = actor.getContract().getContractId();

        Double amount;
        String surname, firstname, patronymic, phone;
        Integer workExperience;
        try {
            amount = (Double) updatedFields.get("contract");
            surname = (String) updatedFields.get("surname");
            firstname = (String) updatedFields.get("firstname");
            patronymic = (String) updatedFields.get("patronymic");
            phone = (String) updatedFields.get("phone");
            workExperience = (Integer) updatedFields.get("workExperience");
        } catch (ClassCastException e) {
            return false;
        }

        if (amount != null) {
            if (!updateContract(contractId, amount)) {
                return false;
            }
        }

        try {
            db.begin();
            db.execute(
                    "UPDATE actor SET surname=?, firstname=?, patronymic=?, phone=?, work_experience=? " +
                            "WHERE actor_id=?",
                    (surname == null) ? actor.getSurname() : surname,
                    (firstname == null) ? actor.getFirstname() : firstname,
                    (patronymic == null) ? actor.getPatronymic() : patronymic,
                    (phone == null) ? actor.getPhone() : phone,
                    (workExperience == null) ? actor.getWorkExperience().getDays() : workExperience,
                    id
            );
            db.commit();
        } catch (Exception e) {
            db.rollback();
            System.out.println(e.getMessage());
            return false;
        }

        try {
            db.begin();
            db.execute("DELETE FROM actor_actor_title WHERE actor_id=?", id);
            db.execute("DELETE FROM actor_actor_award WHERE actor_id=?", id);
            db.commit();
        } catch (SQLException e) {
            db.rollback();
            System.out.println(e.getMessage());
            return false;
        }

        saveTitles(id, actor.getActorTitles());
        saveAwards(id, actor.getActorAwards());

        return true;
    }

    public boolean delete(long id) {
        try {
            db.begin();
            db.execute("DELETE FROM actor WHERE actor_id=?", id);
            db.commit();
        } catch (SQLException e) {
            db.rollback();
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public long getCount(SqlQueryContext ctx) {
        String sql = ctx.apply("SELECT COUNT(*) FROM actor");

        try {
            Long count = db.fetchScalar(sql, ctx.getParams());
            return count != null ? count : 0L;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
    }

    public long getCount() {
        return getCount(new SqlQueryContext());
    }

    private Actor extractActor(Map<String, Object> row) {
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

    private long saveContract(Contract c) {
        if (c == null) return 0;

        long id;
        try {
            db.begin();
            id = db.insertReturningId(
                    "INSERT INTO contract(amount) VALUES(?) RETURNING contract_id",
                    c.getAmount()
            );
            db.commit();
        } catch (SQLException e) {
            db.rollback();
            System.out.println(e.getMessage());
            return 0;
        }

        setPrivateField(c, "contractId", (int) id);

        return id;
    }

    private boolean updateContract(long id, Double amount) {
        try {
            db.begin();
            db.execute("UPDATE contract SET amount=? WHERE contract_id=?", amount, id);
            db.commit();
        } catch (SQLException e) {
            db.rollback();
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private Contract getContract(long id) {
        Map<String, Object> row;
        try {
            row = db.fetchOne(
                    "SELECT amount FROM contract WHERE contract_id=?",
                    id
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        if (row == null) return null;

        return new Contract(id, row.get("amount").toString());
    }

    private void saveTitles(long actorId, List<ActorTitle> list) {
        if (list == null) return;

        for (ActorTitle t : list) {
            long titleId;
            try {
                db.begin();
                titleId = db.insertReturningId(
                        "INSERT INTO actor_title(title_name) VALUES(?) " +
                                "ON CONFLICT(title_name) DO UPDATE SET title_name=EXCLUDED.title_name " +
                                "RETURNING title_id",
                        t.getTitleName()
                );
                db.commit();
            } catch (SQLException e) {
                db.rollback();
                System.out.println(e.getMessage());
                return;
            }

            setPrivateField(t, "titleId", (int) titleId);

            try {
                db.begin();
                db.execute(
                        "INSERT INTO actor_actor_title(actor_id, title_id) VALUES(?, ?)",
                        actorId, titleId
                );
                db.commit();
            } catch (SQLException e) {
                db.rollback();
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private List<ActorTitle> getTitles(long actorId) {
        List<Map<String, Object>> rows;
        try {
            rows = db.fetchAll(
                    "SELECT t.title_id, t.title_name FROM actor_title t " +
                            "JOIN actor_actor_title a ON a.title_id = t.title_id " +
                            "WHERE a.actor_id=?",
                    actorId
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

        List<ActorTitle> list = new ArrayList<>();
        for (var r : rows) {
            list.add(new ActorTitle(
                    (int) r.get("title_id"),
                    r.get("title_name")
            ));
        }
        return list;
    }

    private void saveAwards(long actorId, List<ActorAward> list) {
        if (list == null) return;

        for (ActorAward t : list) {

            long id;
            try {
                db.begin();
                id = db.insertReturningId(
                        "INSERT INTO actor_award(award_name) VALUES(?) " +
                                "ON CONFLICT(award_name) DO UPDATE SET award_name=EXCLUDED.award_name " +
                                "RETURNING award_id",
                        t.getAwardName()
                );
                db.commit();
            } catch (SQLException e) {
                db.rollback();
                System.out.println(e.getMessage());
                return;
            }

            setPrivateField(t, "awardId", (int) id);

            try {
                db.begin();
                db.execute(
                        "INSERT INTO actor_actor_award(actor_id, award_id) VALUES(?, ?)",
                        actorId, id
                );
                db.commit();
            } catch (SQLException e) {
                db.rollback();
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    private List<ActorAward> getAwards(long actorId) {
        List<Map<String, Object>> rows;
        try {
            rows = db.fetchAll(
                    "SELECT a.award_id, a.award_name FROM actor_award a " +
                            "JOIN actor_actor_award aw ON aw.award_id = a.award_id " +
                            "WHERE aw.actor_id=?",
                    actorId
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

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
            System.out.println("Ошибка присвоения приватного поля " + name + " " + e.getMessage());
        }
    }
}
