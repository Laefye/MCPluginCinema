package com.github.laefye.plugincinema.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;

public class GeneralConfig {
    public interface IField<T> {
        IField<T> fill(String key, T def);
        T get(YamlConfiguration configuration);
        void put(YamlConfiguration configuration);
        T getValue();
        String getKey();
    }

    public static class Field<T> implements IField<T> {
        public String key;
        public T def;
        public T value;

        @Override
        public IField<T> fill(String key, T def) {
            this.key = key;
            this.def = def;
            return this;
        }

        @Override
        public T get(YamlConfiguration configuration) {
            value = (T) configuration.get(key, def);
            return value;
        }

        @Override
        public void put(YamlConfiguration configuration) {
            configuration.set(key, getValue());
        }

        @Override
        public T getValue() {
            return value == null ? def : value;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    private final File file;

    protected GeneralConfig(File file) {
        this.file = file;
        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(this.file.getParentFile().toURI()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void load() {
        if (file.exists()) {
            try {
                var yaml = new YamlConfiguration();
                yaml.load(file);
                for (var field : fields) {
                    field.get(yaml);
                }
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected <T> T get(String key) {
        for (var field : fields) {
            if (field.getKey().equals(key)) {
                return (T) field.getValue();
            }
        }
        return null;
    }

    protected void save() {
        try {
            var yaml = new YamlConfiguration();
            for (var field : fields) {
                field.put(yaml);
            }
            yaml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final HashSet<IField<?>> fields = new HashSet<>();

    protected <T> void put(String key, T def) {
        fields.add(new Field<>().fill(key, def));
    }

    protected <T> void putField(String key, IField<T> field, T def) {
        fields.add(field.fill(key, def));
    }
}
