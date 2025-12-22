package repository.decorators.filter;

import repository.decorators.ActorRepDBDecorator;
import repository.interfaces.ActorRep;
import utils.OperatorSQL;

public class ActorRepDBFilterDecorator extends ActorRepDBDecorator {

    public ActorRepDBFilterDecorator(ActorRep repo) {
        super(repo);
    }

    public ActorRepDBFilterDecorator bySurname(String surname) {
        ctx.addWhere("surname", OperatorSQL.EQ, surname);
        return this;
    }

    public ActorRepDBFilterDecorator byFirstname(String firstname) {
        ctx.addWhere("firstname", OperatorSQL.EQ, firstname);
        return this;
    }

    public ActorRepDBFilterDecorator byPatronymic(String patronymic) {
        ctx.addWhere("patronymic", OperatorSQL.EQ, patronymic);
        return this;
    }

    public ActorRepDBFilterDecorator phoneStartsWith(String prefix) {
        ctx.addWhere("phone", OperatorSQL.LIKE, prefix + "%");
        return this;
    }
}
