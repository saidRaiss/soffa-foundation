package io.soffa.foundation.data;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
public class DbMigration {

    private final List<String> sources = new ArrayList<>();

    public DbMigration(String... path) {
        sources.addAll(Arrays.asList(path));
    }

    public void add(String path) {
        sources.add(path);
    }

}
