/**
 * Copyright 2010 CosmoCode GmbH
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

package de.cosmocode.palava.activecollab;

import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import de.cosmocode.issuetracker.IssueTracker;
import de.cosmocode.issuetracker.activecollab.ActiveCollab;
import de.cosmocode.palava.core.inject.AbstractRebindModule;
import de.cosmocode.palava.core.inject.Config;
import de.cosmocode.palava.core.inject.RebindModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.net.URI;

/**
 * Installs an {@link de.cosmocode.issuetracker.IssueTracker} service to
 * the given annotation.
 *
 * @author Tobias Sarnowski
 */
public final class ActiveCollabModule implements Module {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveCollabModule.class);

    private ActiveCollabModule() {
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(IssueTracker.class).to(ActiveCollabService.class).in(Singleton.class);
        binder.bind(ActiveCollab.class).to(ActiveCollabService.class).in(Singleton.class);
    }

    /**
     * Rebinds all configuration entries using the specified name as prefix for configuration
     * keys and the supplied annoation for key rebindings.
     *
     * @param annotationType the new binding annotation
     * @param prefix the prefix
     * @return a module which rebinds all required settings
     */
    public static RebindModule annotatedWith(Class<? extends Annotation> annotationType, String prefix) {
        Preconditions.checkNotNull(annotationType, "AnnotationType");
        Preconditions.checkNotNull(prefix, "Prefix");
        return new AnnotatedModule(annotationType, prefix);
    }

    /**
     * A {@link RebindModule} which uses {@link Key#get(java.lang.reflect.Type, Annotation)} to rebind.
     */
    private static final class AnnotatedModule extends AbstractRebindModule {

        private final Class<? extends Annotation> key;
        private final Config config;

        private AnnotatedModule(Class<? extends Annotation> key, String prefix) {
            this.key = key;
            this.config = new Config(prefix);
        }

        @Override
        protected void configuration() {
            bind(URI.class).annotatedWith(Names.named(ActiveCollabConfig.URI)).to(
                    Key.get(URI.class, Names.named(config.prefixed(ActiveCollabConfig.URI))));
            bind(String.class).annotatedWith(Names.named(ActiveCollabConfig.TOKEN)).to(
                    Key.get(String.class, Names.named(config.prefixed(ActiveCollabConfig.TOKEN))));
            bind(int.class).annotatedWith(Names.named(ActiveCollabConfig.PROJECTID)).to(
                    Key.get(int.class, Names.named(config.prefixed(ActiveCollabConfig.PROJECTID))));
        }

        @Override
        protected void optionals() {
            bind(int.class).annotatedWith(Names.named(ActiveCollabConfig.VISIBILITY)).to(
                Key.get(int.class, Names.named(config.prefixed(ActiveCollabConfig.VISIBILITY))));
            bind(int.class).annotatedWith(Names.named(ActiveCollabConfig.MILESTONEID)).to(
                Key.get(int.class, Names.named(config.prefixed(ActiveCollabConfig.MILESTONEID))));
            bind(int.class).annotatedWith(Names.named(ActiveCollabConfig.PARENTID)).to(
                Key.get(int.class, Names.named(config.prefixed(ActiveCollabConfig.PARENTID))));
        }

        @Override
        protected void bindings() {
            bind(ActiveCollabService.class).in(Singleton.class);
            bind(IssueTracker.class).annotatedWith(key).to(ActiveCollabService.class).in(Singleton.class);
            bind(ActiveCollab.class).annotatedWith(key).to(ActiveCollabService.class).in(Singleton.class);
        }

        @Override
        protected void expose() {
            expose(IssueTracker.class).annotatedWith(key);
            expose(ActiveCollab.class).annotatedWith(key);
        }

    }

}
