import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ActorRepYaml extends ActorRepBase {
    private final Yaml yaml = new Yaml();

    /**
     * Конструктор, который проверяет, является ли файл директорией или соответствует ли расширение.
     * Вызывается соответствующее исключение.
     */
    public ActorRepYaml(String filePath) {
        super(filePath);
    }

    @Override
    protected String getPatternExtension() {
        return "^.*\\.ya?ml$";
    }

    @Override
    protected String getExtension() {
        return "yaml";
    }

    @Override
    protected void writeToFile(Map<String, ?> wrapper) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(yaml.dump(wrapper));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка записи YAML-файла: " + file.getName(), e);
        }
    }
}
