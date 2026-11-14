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
package org.apache.maven.plugins.changelog;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.maven.api.plugin.MojoException;
import org.apache.maven.doxia.module.xhtml5.Xhtml5SinkFactory;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkFactory;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;

/**
 * Common test setup for the report mojos.
 */
abstract class AbstractChangeLogReportTest {

    /**
     * Executes a report mojo and writes the report as plain XHTML5 to
     * <code>&lt;outputDirectory&gt;/&lt;outputName&gt;.html</code>.
     * <p>
     * {@link AbstractMavenReport#execute()} renders a standalone report through the Doxia site renderer, which needs
     * Maven 3 objects that the Maven 4 test harness cannot provide. The report content does not depend on that
     * rendering, so this helper calls {@link AbstractMavenReport#generate(Sink, SinkFactory, Locale)} directly and
     * wraps a {@link MavenReportException} the way <code>execute()</code> does.
     */
    protected void executeReport(AbstractMavenReport mojo) throws Exception {
        try {
            if (!mojo.canGenerateReport()) {
                return;
            }
        } catch (MavenReportException e) {
            throw new MojoException("Failed to determine whether report can be generated", e);
        }

        File outputDirectory = mojo.getReportOutputDirectory();
        SinkFactory sinkFactory = new Xhtml5SinkFactory();
        try (Sink sink = sinkFactory.createSink(outputDirectory, mojo.getOutputName() + ".html")) {
            mojo.generate(sink, sinkFactory, Locale.ENGLISH);
        } catch (MavenReportException e) {
            throw new MojoException(
                    "An error has occurred in " + mojo.getName(Locale.ENGLISH) + " report generation.", e);
        } catch (IOException e) {
            throw new MojoException("Cannot create the report sink in " + outputDirectory, e);
        }
    }
}
