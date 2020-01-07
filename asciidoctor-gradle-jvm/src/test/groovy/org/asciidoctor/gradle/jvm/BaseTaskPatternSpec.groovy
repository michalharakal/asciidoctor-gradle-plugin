/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.asciidoctor.gradle.jvm

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.util.PatternSet
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.workers.WorkerExecutor
import spock.lang.Specification

import javax.inject.Inject

/**
 * @author Lari Hotari
 */
class BaseTaskPatternSpec extends Specification {
    Project project = ProjectBuilder.builder().build()

    static class PatternSpecAsciidoctorTask extends AbstractAsciidoctorTask {
        @Inject
        PatternSpecAsciidoctorTask(WorkerExecutor we) {
            super(we)
        }

        // method for accessing internal field "sourceDocumentPattern"
        PatternSet getInternalSourceDocumentPattern() {
            AbstractAsciidoctorTask.metaClass.getProperty(this, 'sourceDocumentPattern')
        }
    }

    void "Should include patterns passed to sources method"() {
        when:
        def task1 = createTask('task') {
            sources('myfile.adoc', 'otherfile.adoc')
        }
        then:
        task1.internalSourceDocumentPattern.includes == ['myfile.adoc', 'otherfile.adoc'] as Set
    }

    private Task createTask(String name, Closure cfg) {
        project.tasks.create(name: name, type: PatternSpecAsciidoctorTask).configure cfg
    }
}
