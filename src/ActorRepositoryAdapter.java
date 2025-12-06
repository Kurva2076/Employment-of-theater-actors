import java.util.List;

public class ActorRepositoryAdapter implements ActorRepository {
    private final Object adaptee;

    public ActorRepositoryAdapter(Object adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public Actor getById(long id) {
        if (adaptee instanceof ActorRepDB db) {
            return db.getById(id);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.getById((int) id);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public List<PublicActor> getKNShortList(int k, int n) {
        if (adaptee instanceof ActorRepDB db) {
            return db.getKNShortList(k, n);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.getKNShortList(k, n);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public Actor add(Actor actor) {
        if (adaptee instanceof ActorRepDB db) {
            return db.add(actor);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.add(actor);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public boolean update(long id, Actor actor) {
        if (adaptee instanceof ActorRepDB db) {
            db.update(id, actor);
            return true;
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.replaceById((int) id, actor);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public boolean delete(long id) {
        if (adaptee instanceof ActorRepDB db) {
            db.delete(id);
            return true;
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.deleteById((int) id);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public long getCount() {
        if (adaptee instanceof ActorRepDB db) {
            return db.getCount();
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.getCount();
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }
}
