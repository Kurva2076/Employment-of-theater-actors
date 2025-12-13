import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ActorRepJson extends ActorRepBase {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Конструктор, который проверяет, является ли файл директорией или соответствует ли расширение.
     * Вызывается соответствующее исключение.
     */
    public ActorRepJson(String filePath) {
        super(filePath);
    }

    @Override
    protected String getPatternExtension() {
        return "^.*\\.json$";
    }

    @Override
    protected String getExtension() {
        return "json";
    }

    @Override
    protected void writeToFile(Map<String, ?> wrapper) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(gson.toJson(wrapper));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи JSON-файла: " + file.getName(), e);
        }
    }
}
