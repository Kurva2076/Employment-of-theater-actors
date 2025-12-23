package repository.interactions;

import model.Actor;
import model.PublicActor;
import repository.interfaces.ActorRep;
import utils.Parser;
import utils.comparators.ActorComparators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public abstract class ActorRepBase implements ActorRep {
    protected final File file;

    /**
     * Конструктор, который проверяет, является ли файл директорией или соответствует ли расширение.
     * Вызывается соответствующее исключение.
     */
    protected ActorRepBase(String filePath) {
        this.file = new File(filePath);

        if (file.isDirectory()) {
            try {
                throw new FileNotFoundException("Объект не является файлом");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (Pattern.compile(getPatternExtension()).matcher(file.getName()).results().findAny().isEmpty()) {
            try {
                throw new FileNotFoundException("Расширение файла не соответствует");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Ошибка создания файла " + file.getName());
            }

            writeAll(new ArrayList<>());
        }
    }

    /**
     * Получение строки для паттерна расширения файла
     */
    protected abstract String getPatternExtension();

    /**
     * Получение расширения файла
     */
    protected abstract String getExtension();

    /**
     * Запись в файл
     */
    protected abstract void writeToFile(Map<String, ?> wrapper);

    /**
     * Чтение всех значений из файла
     */
    @SuppressWarnings("unchecked")
    public List<Actor> readAll() {
        List<Actor> actors = new ArrayList<>();
        Map<String, ?> map = Parser.parse(file, getExtension(), Actor.class);

        if (map == null || map.get("actors") == null) {
            writeAll(actors);
            return actors;
        }

        Object raw = map.get("actors");

        if (raw instanceof List<?> rawList) {
            for (Object o : rawList) {
                if (o instanceof Map<?, ?> actorMap) {
                    actors.add(new Actor((Map<String, ?>) actorMap));
                }
            }
        }

        return actors;
    }

    /**
     * Запись всех значений в файл
     */
    public void writeAll(List<Actor> actors) {
        Map<String, Object> wrapper = new HashMap<>();

        List<Actor> allActors = readAll();
        System.out.println(allActors.size());
        allActors.addAll(actors);
        System.out.println(allActors.size());

        // Преобразуем каждый Actor в простой Map
        List<Map<String, Object>> simpleList = actors.stream()
                .map(Actor::toSimpleMap)
                .toList();

        wrapper.put("actors", simpleList);

        writeToFile(wrapper);
    }

    /**
     * Получить объект по ID
     */
    public Actor getById(Integer id) {
        for (Actor a : readAll()) {
            if (Objects.equals(a.getActorId(), id)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public Actor getById(long id) {
        return getById(Integer.valueOf((int) id));
    }

    /**
     * Получить список k по счёту n объектов (пагинация)
     */
    public List<PublicActor> getKNShortList(int k, int n) {
        List<Actor> actors = readAll();
        List<PublicActor> publicActors = new ArrayList<>();

        for (Actor actor : actors) {
            publicActors.add(new PublicActor(
                    actor.getActorId(), actor.getSurname(), actor.getFirstname(), actor.getFirstname(), actor.getPhone()
            ));
        }

        int start = k * n - n;
        int end = Math.min(k * n, publicActors.size());

        if (start >= publicActors.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(publicActors.subList(start, end));
    }

    /**
     * Сортировать элементы по выбранному полю
     */
    public List<Actor> sortBy(String field) {
        List<Actor> actors = readAll();

        actors.sort(
                switch (field) {
                    case "id" -> ActorComparators.BY_ID;
                    case "surname" -> ActorComparators.BY_SURNAME;
                    case "firstname" -> ActorComparators.BY_FIRSTNAME;
                    case "patronymic" -> ActorComparators.BY_PATRONYMIC;
                    case "fullName" -> ActorComparators.BY_FULL_NAME;
                    case "initials" -> ActorComparators.BY_INITIALS;
                    case "workExperience" -> ActorComparators.BY_WORK_EXPERIENCE;
                    case "contract" -> ActorComparators.BY_CONTRACT_AMOUNT;
                    case "titles" -> ActorComparators.BY_TITLES_COUNT;
                    case "awards" -> ActorComparators.BY_AWARDS_COUNT;
                    default -> throw new IllegalArgumentException("Unknown sort field: " + field);
                }
        );
        writeAll(actors);

        return actors;
    }

    /**
     * Добавить объект в список (формирования нового ID)
     */
    public Actor add(Actor actor) {
        List<Actor> actors = readAll();
        int newId = getNextId();

        try {
            var field = Actor.class.getDeclaredField("actorId");
            field.setAccessible(true);
            field.set(actor, newId);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось установить новый actorId", e);
        }

        actors.add(actor);
        writeAll(actors);

        return actor;
    }

    /**
     * Получение следующего по счёту id
     */
    public int getNextId() {
        List<Actor> actors = readAll();

        if (actors.isEmpty()) {
            return 1;
        }

        int maxId = actors.stream()
                .mapToInt(Actor::getActorId)
                .max()
                .orElse(0);

        return maxId + 1;
    }

    /**
     * Заменить элемент списка по ID
     */
    public boolean replaceById(Integer id, Actor newActor) {
        List<Actor> actors = readAll();

        for (int i = 0; i < actors.size(); i++) {
            if (Objects.equals(actors.get(i).getActorId(), id)) {
                try {
                    var field = Actor.class.getDeclaredField("actorId");
                    field.setAccessible(true);
                    field.set(newActor, id);
                } catch (Exception e) {
                    throw new RuntimeException("Не удалось установить actorId", e);
                }

                actors.set(i, newActor);
                writeAll(actors);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean update(long id, Actor actor) {
        return replaceById((int) id, actor);
    }

    @Override
    public boolean update(long id, Map<String, Object> updatedFields) {
        Actor actor = getById(id);
        if (actor == null) {
            return false;
        }

        for (String fieldName : updatedFields.keySet()) {
            try {
                actor.set(fieldName, updatedFields.get(fieldName));
            } catch (ClassCastException e) {}
        }
        return update(id, actor);
    }


    /**
     * Удалить элемент списка по ID
     */
    public boolean deleteById(Integer id) {
        List<Actor> actors = readAll();
        Iterator<Actor> it = actors.iterator();

        while (it.hasNext()) {
            Actor a = it.next();
            if (Objects.equals(a.getActorId(), id)) {
                it.remove();
                writeAll(actors);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(long id) {
        return deleteById((int) id);
    }

    /**
     * Получить количество элементов
     */
    public long getCount() {
        return readAll().size();
    }
}
