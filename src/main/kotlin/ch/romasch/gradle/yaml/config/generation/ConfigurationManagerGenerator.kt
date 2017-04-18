package ch.romasch.gradle.yaml.config.generation

class ConfigurationManagerGenerator(val packageName: String, val configurationName: String) {

    val managerName: String = "ConfigurationManager"

    fun generate(): String = """
package $packageName;

import java.util.concurrent.atomic.AtomicReference;
import org.yaml.snakeyaml.Yaml;

public class $managerName {

    private AtomicReference<$configurationName> config;

    public $configurationName get() {
        return config.get();
    }

    public synchronized void override(String yamlFile) {
        Yaml yaml = new Yaml();
        Object parsed= yaml.load(yamlFile);
        $configurationName override = config.get().override((java.util.Map) parsed);
        config.set(override);
    }

}
"""

}

