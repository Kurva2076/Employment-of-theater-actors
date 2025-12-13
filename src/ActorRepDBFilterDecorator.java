public class ActorRepDBFilterDecorator extends ActorRepDBDecorator {

    public ActorRepDBFilterDecorator(ActorRepository repo) {
        super(repo);
    }

    public ActorRepDBFilterDecorator bySurname(String surname) {
        ctx.addWhere("surname", OperatorSQL.EQ, surname);
        return this;
    }

    public ActorRepDBFilterDecorator phoneStartsWith(String prefix) {
        ctx.addWhere("phone", OperatorSQL.LIKE, prefix + "%");
        return this;
    }
}
