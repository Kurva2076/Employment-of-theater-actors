import java.util.Comparator;

public class ActorComparators {
    public static final Comparator<Actor> BY_ID =
            Comparator.comparing(Actor::getActorId);
    public static final Comparator<Actor> BY_SURNAME =
            Comparator.comparing(a -> a.getSurname().toLowerCase());
    public static final Comparator<Actor> BY_FIRSTNAME =
            Comparator.comparing(a -> a.getFirstname().toLowerCase());
    public static final Comparator<Actor> BY_PATRONYMIC =
            Comparator.comparing(
                    a -> a.getPatronymic() == null ? "" : a.getPatronymic().toLowerCase()
            );
    public static final Comparator<Actor> BY_INITIALS =
            Comparator.comparing(a -> a.getInitials().toLowerCase());
    public static final Comparator<Actor> BY_WORK_EXPERIENCE =
            Comparator.comparing(a -> a.getWorkExperience().getDays());
    public static final Comparator<Actor> BY_CONTRACT_AMOUNT =
            Comparator.comparing(a -> a.getContract().getAmount());
    public static final Comparator<Actor> BY_TITLES_COUNT =
            Comparator.comparing(a -> a.getActorTitles().size());
    public static final Comparator<Actor> BY_AWARDS_COUNT =
            Comparator.comparing(a -> a.getActorAwards().size());
    public static final Comparator<Actor> BY_FULL_NAME =
            BY_SURNAME.thenComparing(BY_FIRSTNAME).thenComparing(BY_PATRONYMIC);
}
