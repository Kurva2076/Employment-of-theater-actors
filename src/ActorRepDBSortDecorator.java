public class ActorRepDBSortDecorator extends ActorRepDBDecorator {

    public ActorRepDBSortDecorator(ActorRepository repo) {
        super(repo);
    }

    public ActorRepDBSortDecorator bySurname(SortSQL sort) {
        ctx.setOrderBy("surname", sort);
        return this;
    }

    public ActorRepDBSortDecorator byExperience(SortSQL sort) {
        ctx.setOrderBy("work_experience", sort);
        return this;
    }
}
