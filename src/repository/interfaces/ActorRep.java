package repository.interfaces;

import model.Actor;
import model.PublicActor;

import java.util.List;
import java.util.Map;

public interface ActorRep {
    Actor getById(long id);

    List<PublicActor> getKNShortList(int k, int n);

    Actor add(Actor actor);

    boolean update(long id, Actor actor);

    boolean update(long id, Map<String, Object> updatedFields);

    boolean delete(long id);

    long getCount();
}
