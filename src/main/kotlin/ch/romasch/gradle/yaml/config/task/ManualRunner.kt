package ch.romasch.gradle.yaml.config.task

val YAML_SIMPLE_MAP = "a: b\nc: d\n"
val YAML_TWO_LEVEL_MAP = "a:\n    b: c\n    d: f\n"

val YAML_NESTED = """
TestWebService:
  Connection:
    Host: mockserver
    Port: 7201
    Protocol: https
  Authentication:
    Username: newom
    Password: '1234'
"""

val YAML_WITH_ANCHOR = """
Template: &template
  Connection:
    Host: mockserver
    Port: 7201
    Protocol: https
  Authentication:
    Username: newom
    Password: '1234'
TestWebService:
  Connection:
    Host: localhost
  <<: *template
"""


fun main(args: Array<String>) {
    ManualRunner(YAML_NESTED).run()
}

private class ManualRunner(val yaml: String) {

    fun run() {
        generateJavaCode(arrayListOf(yaml), "ch.romasch.test").values.forEach(::print)
    }

//    public class ConfigManager {
//
//        private AtomicReference<ConfigurationType> config;
//
//        ConfigurationType get() {
//            return config.get();
//        }
//
//        synchronized void override(String yamlFile) {
//            ConfigurationType newConfig = config.get();
//
//            Yaml yaml = new Yaml();
//            Object parsed= yaml.load(yamlFile);
//            ConfigurationType override = config.get().override(parsed);
//            config.set(override);
//        }
//
//    }
}


