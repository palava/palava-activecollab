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

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import de.cosmocode.issuetracker.Issue;
import de.cosmocode.issuetracker.IssueTrackerException;
import de.cosmocode.issuetracker.activecollab.ActiveCollab;
import de.cosmocode.issuetracker.activecollab.ActiveCollabConnector;
import de.cosmocode.issuetracker.activecollab.ActiveCollabIssue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author Tobias Sarnowski
 */
final class ActiveCollabService implements ActiveCollab {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveCollabService.class);

    private final ActiveCollab ac;

    @Inject
    ActiveCollabService(
            @Named(ActiveCollabConfig.URI) URI uri,
            @Named(ActiveCollabConfig.TOKEN) String token,
            @Named(ActiveCollabConfig.PROJECTID) int projectId) {

        ac = ActiveCollabConnector.connectActiveCollab(uri, token, projectId);
        LOG.info("Configured {}", ac);
    }

    @Override
    public URI getUri() {
        return ac.getUri();
    }

    @Override
    public int getProjectId() {
        return ac.getProjectId();
    }

    @Override
    public int getMilestoneId() {
        return ac.getMilestoneId();
    }

    @Inject(optional = true)
    public void configureMilestoneId(@Named(ActiveCollabConfig.MILESTONEID) int milestoneId) {
        setMilestoneId(milestoneId);
        LOG.info("{} milestoneId set to {}", ac, milestoneId);
    }

    @Override
    public void setMilestoneId(int milestoneId) {
        ac.setMilestoneId(milestoneId);
    }

    @Override
    public int getParentId() {
        return ac.getParentId();
    }

    @Inject(optional = true)
    public void configureParentId(@Named(ActiveCollabConfig.PARENTID) int parentId) {
        setParentId(parentId);
        LOG.info("{} parentId set to {}", ac, parentId);
    }

    @Override
    public void setParentId(int parentId) {
        ac.setParentId(parentId);
    }

    @Override
    public ActiveCollabIssue createIssue(String title, String description) throws IssueTrackerException {
        return ac.createIssue(title, description);
    }

    @Override
    public ActiveCollabIssue createIssue(String title, String description, Predicate<? super Issue> duplicationCheck)
            throws IssueTrackerException {
        return ac.createIssue(title, description, duplicationCheck);
    }

    @Override
    public Iterable<ActiveCollabIssue> listIssues() throws IssueTrackerException {
        return ac.listIssues();
    }

    @Override
    public ActiveCollabIssue updateIssue(Issue issue) throws IssueTrackerException {
        return ac.updateIssue(issue);
    }

    @Override
    public String toString() {
        return "ActiveCollabService{" +
                "ac=" + ac +
                '}';
    }
}