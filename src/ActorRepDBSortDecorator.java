public class ActorRepDBSortDecorator extends ActorRepDBDecorator {

    public ActorRepDBSortDecorator(ActorRepository repo) {
        super(repo);
    }

    public ActorRepDBSortDecorator bySurname(Sort sort) {
        ctx.setOrderBy("surname", sort);
        return this;
    }

    public ActorRepDBSortDecorator byExperience(Sort sort) {
        ctx.setOrderBy("work_experience", sort);
        return this;
    }
}
