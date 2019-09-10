package com.lly835.bestpay.rest.param;

import javax.ws.rs.client.WebTarget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Path {

    private final List<String> paths;

    private Path(String... paths) {
        this.paths = new ArrayList<>();
        Collections.addAll(this.paths, paths);
    }

    public static Path build(String... paths) {
        Objects.requireNonNull(paths, "Paths are null.");
        return new Path(paths);
    }

    public WebTarget appendToTarget(WebTarget target) {
        WebTarget newTarget = target;
        for (String path : this.paths) {
            newTarget = newTarget.path(path);
        }

        return newTarget;
    }

    @Override
    public String toString() {
        return this.paths.stream().filter(p -> p != null).map(p -> {
            if (!p.startsWith("/")) {
                return "/" + p;
            } else {
                return p;
            }
        }).collect(Collectors.joining("", "{Path: ", "}"));
    }

}
