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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * @author Tobias Sarnowski
 */
final class ActiveCollabService implements ActiveCollab {
    private static final Logger LOG = LoggerFactory.getLogger(ActiveCollabService.class);

    private final ActiveCollab ac;

    @Inject
    ActiveCollabService(
            @Named(ActiveCollabConfig.URI) URI uri,
            @Named(ActiveCollabConfig.USER) String user,
            @Named(ActiveCollabConfig.APIKEY) String apikey,
            @Named(ActiveCollabConfig.PROJECTID) int projectId) {

        ac = ActiveCollabConnector.connectActiveCollab(uri, user, apikey, projectId);
        LOG.info("Configured {}", ac);
    }

    @Override
    public URI getUri() {
        return ac.getUri();
    }

    @Override
    public String getUser() {
        return ac.getUser();
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
    public Issue createIssue(String title, String description) throws IssueTrackerException {
        return ac.createIssue(title, description);
    }

    @Override
    public Issue createIssue(String title, String description, Predicate<Issue> duplicationCheck) throws IssueTrackerException {
        return ac.createIssue(title, description, duplicationCheck);
    }

    @Override
    public List<Issue> listIssues() throws IssueTrackerException {
        return ac.listIssues();
    }

    @Override
    public void updateIssue(Issue issue) throws IssueTrackerException {
        ac.updateIssue(issue);
    }
}