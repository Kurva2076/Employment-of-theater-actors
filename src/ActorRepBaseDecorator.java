import java.util.List;

public abstract class ActorRepBaseDecorator implements ActorRepository {

    protected final ActorRepBase repo;

    protected ActorRepBaseDecorator(ActorRepository repo) {
        this.repo = extractBase(repo);
    }

    protected ActorRepBase extractBase(ActorRepository repo) {
        if (repo instanceof ActorRepBase base) {
            return base;
        }
        if (repo instanceof ActorRepBaseDecorator decorator) {
            return decorator.repo;
        }
        throw new IllegalArgumentException(
                "ActorRepository is not backed by ActorRepBase"
        );
    }

    @Override
    public Actor getById(long id) {
        return repo.getById(id);
    }

    @Override
    public Actor add(Actor actor) {
        return repo.add(actor);
    }

    @Override
    public boolean update(long id, Actor actor) {
        return repo.update(id, actor);
    }

    @Override
    public boolean delete(long id) {
        return repo.delete(id);
    }

    @Override
    public List<PublicActor> getKNShortList(int k, int n) {
        return repo.getKNShortList(k, n);
    }

    @Override
    public long getCount() {
        return repo.getCount();
    }
}
