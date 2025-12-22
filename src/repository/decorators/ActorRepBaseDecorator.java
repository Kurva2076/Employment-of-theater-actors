package repository.decorators;

import model.Actor;
import model.PublicActor;
import repository.interactions.ActorRepBase;
import repository.interfaces.ActorRep;

import java.util.List;
import java.util.Map;

public abstract class ActorRepBaseDecorator implements ActorRep {

    protected final ActorRepBase repo;

    protected ActorRepBaseDecorator(ActorRep repo) {
        this.repo = extractBase(repo);
    }

    protected ActorRepBase extractBase(ActorRep repo) {
        if (repo instanceof ActorRepBase base) {
            return base;
        }
        if (repo instanceof ActorRepBaseDecorator decorator) {
            return decorator.repo;
        }
        throw new IllegalArgumentException("repository.interfaces.ActorRep is not backed by repository.interactions.ActorRepBase");
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
    public boolean update(long id, Map<String, Object> updatedFields) {
        return repo.update(id, updatedFields);
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
