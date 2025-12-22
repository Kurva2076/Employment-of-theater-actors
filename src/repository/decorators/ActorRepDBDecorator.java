package repository.decorators;

import model.Actor;
import model.PublicActor;
import repository.interactions.ActorRepDB;
import repository.interfaces.ActorRep;
import utils.SqlQueryContext;

import java.util.List;
import java.util.Map;

public abstract class ActorRepDBDecorator implements ActorRep {

    protected final ActorRepDB repo;
    protected final SqlQueryContext ctx;

    protected ActorRepDBDecorator(ActorRep source) {
        ExtractResult r = extractDbAndCtx(source);
        this.repo = r.repo;
        this.ctx = r.ctx;
    }

    private static ExtractResult extractDbAndCtx(ActorRep src) {
        if (src instanceof ActorRepDB db) {
            return new ExtractResult(db, new SqlQueryContext());
        }
        if (src instanceof ActorRepDBDecorator dec) {
            return new ExtractResult(dec.repo, dec.ctx);
        }

        throw new IllegalArgumentException(
                "repository.interfaces.ActorRep должен быть repository.interactions.ActorRepDB или repository.decorators.ActorRepDBDecorator"
        );
    }

    @Override
    public Actor getById(long id) {
        return repo.getById(id);
    }

    @Override
    public List<PublicActor> getKNShortList(int k, int n) {
        return repo.getKNShortList(k, n, ctx);
    }

    @Override
    public long getCount() {
        return repo.getCount(ctx);
    }

    @Override
    public boolean update(long id, Actor actor) {
        return repo.update(id, actor);
    }

    @Override
    public boolean update(long id, Map<String, Object> updatedFields) {
        return repo.update(id, updatedFields);
    }

    @Override
    public Actor add(Actor actor) {
        return repo.add(actor);
    }

    @Override
    public boolean delete(long id) {
        return repo.delete(id);
    }

    protected record ExtractResult(ActorRepDB repo, SqlQueryContext ctx) {
    }
}
