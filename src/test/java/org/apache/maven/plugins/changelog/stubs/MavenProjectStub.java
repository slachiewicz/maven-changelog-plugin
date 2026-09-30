/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugins.changelog.stubs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.api.model.Build;
import org.apache.maven.api.model.Developer;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Scm;
import org.apache.maven.api.plugin.testing.MojoExtension;
import org.apache.maven.api.plugin.testing.stubs.ProjectStub;

/**
 * @author Edwin Punzalan
 */
public class MavenProjectStub extends ProjectStub {
    private static int testCounter = 0;

    private final List<Developer> developers = new ArrayList<>();

    public MavenProjectStub() {
        super();

        testCounter++;
    }

    public MavenProjectStub addDeveloper(String id, String name) {
        developers.add(Developer.newBuilder().id(id).name(name).build());
        return this;
    }

    @Override
    public Model getModel() {
        return Model.newBuilder(super.getModel())
                .scm(Scm.newBuilder().connection("scm://").url("scm://").build())
                .developers(developers)
                .build(Build.newBuilder()
                        .directory(getBasedir().resolve("target").toString())
                        .build())
                .build();
    }

    @Override
    public Path getBasedir() {
        return Paths.get(MojoExtension.getBasedir(), "target/test-harness/" + testCounter);
    }
}
