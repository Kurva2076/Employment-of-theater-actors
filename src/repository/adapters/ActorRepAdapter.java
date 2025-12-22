package repository.adapters;

import model.Actor;
import model.PublicActor;
import repository.decorators.ActorRepBaseDecorator;
import repository.decorators.ActorRepDBDecorator;
import repository.interactions.ActorRepBase;
import repository.interactions.ActorRepDB;
import repository.interfaces.ActorRep;

import java.util.List;
import java.util.Map;

public class ActorRepAdapter implements ActorRep {
    private final Object adaptee;

    public ActorRepAdapter(Object adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public Actor getById(long id) {
        if (adaptee instanceof ActorRepDB db) {
            return db.getById(id);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.getById(id);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public List<PublicActor> getKNShortList(int k, int n) {
        if (adaptee instanceof ActorRepDB db) {
            return db.getKNShortList(k, n);
        }
        if (adaptee instanceof ActorRepDBDecorator dbDecorator) {
            return dbDecorator.getKNShortList(k, n);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.getKNShortList(k, n);
        }
        if (adaptee instanceof ActorRepBaseDecorator decorator) {
            return decorator.getKNShortList(k, n);
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
            return db.update(id, actor);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.update(id, actor);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public boolean update(long id, Map<String, Object> updatedFields) {
        if (adaptee instanceof ActorRepDB db) {
            return db.update(id, updatedFields);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.update(id, updatedFields);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }


    @Override
    public boolean delete(long id) {
        if (adaptee instanceof ActorRepDB db) {
            return db.delete(id);
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.delete(id);
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }

    @Override
    public long getCount() {
        if (adaptee instanceof ActorRepDB db) {
            return db.getCount();
        }
        if (adaptee instanceof ActorRepDBDecorator dbDecorator) {
            return dbDecorator.getCount();
        }
        if (adaptee instanceof ActorRepBase file) {
            return file.getCount();
        }
        if (adaptee instanceof ActorRepBaseDecorator decorator) {
            return decorator.getCount();
        }
        throw new UnsupportedOperationException("Unknown repository type");
    }
}
