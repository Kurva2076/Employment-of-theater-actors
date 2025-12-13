import java.util.List;
import java.util.stream.Stream;

public class ActorRepBaseFilterDecorator extends ActorRepBaseDecorator {

    private String surname;
    private String phonePrefix;

    public ActorRepBaseFilterDecorator(ActorRepository repo) {
        super(repo);
    }

    public ActorRepBaseFilterDecorator bySurname(String surname) {
        this.surname = surname;
        return this;
    }

    public ActorRepBaseFilterDecorator phoneStartsWith(String prefix) {
        this.phonePrefix = prefix;
        return this;
    }

    private Stream<PublicActor> applyFilter(Stream<PublicActor> stream) {
        if (surname != null) {
            stream = stream.filter(a -> surname.equals(a.getSurname()));
        }
        if (phonePrefix != null) {
            stream = stream.filter(a -> a.getPhone().startsWith(phonePrefix));
        }
        return stream;
    }

    @Override
    public List<PublicActor> getKNShortList(int k, int n) {
        List<PublicActor> base =
                repo.getKNShortList(1, Integer.MAX_VALUE);

        return applyFilter(base.stream())
                .skip((long) (k - 1) * n)
                .limit(n)
                .toList();
    }

    @Override
    public long getCount() {
        List<PublicActor> base =
                repo.getKNShortList(1, Integer.MAX_VALUE);

        return applyFilter(base.stream()).count();
    }
}
