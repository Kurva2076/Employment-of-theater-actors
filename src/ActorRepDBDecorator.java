import java.util.List;
import java.util.Map;

public abstract class ActorRepDBDecorator implements ActorRepository {

    protected final ActorRepDB repo;
    protected final SqlQueryContext ctx;

    protected ActorRepDBDecorator(ActorRepository source) {
        ExtractResult r = extractDbAndCtx(source);
        this.repo = r.repo;
        this.ctx = r.ctx;
    }

    private static ExtractResult extractDbAndCtx(ActorRepository src) {
        if (src instanceof ActorRepDB db) {
            return new ExtractResult(db, new SqlQueryContext());
        }
        if (src instanceof ActorRepDBDecorator dec) {
            return new ExtractResult(dec.repo, dec.ctx);
        }

        throw new IllegalArgumentException(
                "ActorRepository должен быть ActorRepDB или ActorRepDBDecorator"
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
