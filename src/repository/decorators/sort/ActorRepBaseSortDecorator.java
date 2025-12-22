package repository.decorators.sort;

import model.PublicActor;
import repository.decorators.ActorRepBaseDecorator;
import repository.interfaces.ActorRep;
import utils.Sort;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class ActorRepBaseSortDecorator extends ActorRepBaseDecorator {

    private Comparator<PublicActor> comparator;

    public ActorRepBaseSortDecorator(ActorRep repo) {
        super(repo);
    }

    public ActorRepBaseSortDecorator bySurname(Sort sort) {
        comparator = Comparator.comparing(PublicActor::getSurname);
        if (sort == Sort.DESC) comparator = comparator.reversed();
        return this;
    }

    public ActorRepBaseSortDecorator byFirstname(Sort sort) {
        comparator = Comparator.comparing(PublicActor::getFirstname);
        if (sort == Sort.DESC) comparator = comparator.reversed();
        return this;
    }

    public ActorRepBaseSortDecorator byPatronymic(Sort sort) {
        comparator = Comparator.comparing(PublicActor::getPatronymic);
        if (sort == Sort.DESC) comparator = comparator.reversed();
        return this;
    }

    public ActorRepBaseSortDecorator byPhone(Sort sort) {
        comparator = Comparator.comparing(PublicActor::getPhone);
        if (sort == Sort.DESC) comparator = comparator.reversed();
        return this;
    }

    @Override
    public List<PublicActor> getKNShortList(int k, int n) {
        List<PublicActor> base =
                repo.getKNShortList(1, Integer.MAX_VALUE);

        Stream<PublicActor> stream = base.stream();
        if (comparator != null) {
            stream = stream.sorted(comparator);
        }

        return stream
                .skip((long) (k - 1) * n)
                .limit(n)
                .toList();
    }

    @Override
    public long getCount() {
        return repo.getCount();
    }
}
