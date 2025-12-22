package repository.decorators.sort;

import repository.decorators.ActorRepDBDecorator;
import repository.interfaces.ActorRep;
import utils.Sort;

public class ActorRepDBSortDecorator extends ActorRepDBDecorator {

    public ActorRepDBSortDecorator(ActorRep repo) {
        super(repo);
    }

    public ActorRepDBSortDecorator bySurname(Sort sort) {
        ctx.setOrderBy("surname", sort);
        return this;
    }

    public ActorRepDBSortDecorator byFirstname(Sort sort) {
        ctx.setOrderBy("firstname", sort);
        return this;
    }

    public ActorRepDBSortDecorator byPatronymic(Sort sort) {
        ctx.setOrderBy("patronymic", sort);
        return this;
    }

    public ActorRepDBSortDecorator byExperience(Sort sort) {
        ctx.setOrderBy("work_experience", sort);
        return this;
    }
}
