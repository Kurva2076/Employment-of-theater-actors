import java.util.List;

public interface ActorRepository {
    Actor getById(long id);
    List<PublicActor> getKNShortList(int k, int n);
    Actor add(Actor actor);
    boolean update(long id, Actor actor);
    boolean delete(long id);
    long getCount();
}
