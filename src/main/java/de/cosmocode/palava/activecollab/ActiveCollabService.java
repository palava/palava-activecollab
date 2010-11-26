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

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.issuetracker.Issue;
import de.cosmocode.issuetracker.IssueTrackerException;
import de.cosmocode.issuetracker.activecollab.ActiveCollab;
import de.cosmocode.issuetracker.activecollab.ActiveCollabConnector;
import de.cosmocode.issuetracker.activecollab.ActiveCollabIssue;

/**
 * {@link Inject Injectable} {@link ActiveCollab}.
 * 
 * @author Tobias Sarnowski
 */
final class ActiveCollabService implements ActiveCollab {
    
    private static final Logger LOG = LoggerFactory.getLogger(ActiveCollabService.class);

    private final ActiveCollab activeCollab;

    @Inject
    ActiveCollabService(
            @Named(ActiveCollabConfig.URI) URI uri,
            @Named(ActiveCollabConfig.TOKEN) String token,
            @Named(ActiveCollabConfig.PROJECTID) int projectId) {

        this.activeCollab = ActiveCollabConnector.connectActiveCollab(uri, token, projectId);
        LOG.info("Configured {}", activeCollab);
    }

    @Override
    public URI getUri() {
        return activeCollab.getUri();
    }

    @Override
    public int getProjectId() {
        return activeCollab.getProjectId();
    }

    @Override
    public int getVisibility() {
        return activeCollab.getVisibility();
    }

    @Override
    @Inject(optional = true)
    public void setVisibility(@Named(ActiveCollabConfig.VISIBILITY) int visibility) {
        activeCollab.setVisibility(visibility);
        LOG.info("{} visibility set to {}", activeCollab, visibility);
    }

    @Override
    public int getMilestoneId() {
        return activeCollab.getMilestoneId();
    }

    @Override
    @Inject(optional = true)
    public void setMilestoneId(@Named(ActiveCollabConfig.MILESTONEID)  int milestoneId) {
        activeCollab.setMilestoneId(milestoneId);
        LOG.info("{} milestoneId set to {}", activeCollab, milestoneId);
    }

    @Override
    public int getParentId() {
        return activeCollab.getParentId();
    }

    @Override
    @Inject(optional = true)
    public void setParentId(@Named(ActiveCollabConfig.PARENTID) int parentId) {
        activeCollab.setParentId(parentId);
        LOG.info("{} parentId set to {}", activeCollab, parentId);
    }

    @Override
    public ActiveCollabIssue createIssue(String title, String description) throws IssueTrackerException {
        return activeCollab.createIssue(title, description);
    }

    @Override
    public ActiveCollabIssue createIssue(String title, String description, Predicate<? super Issue> duplicationCheck)
        throws IssueTrackerException {
        return activeCollab.createIssue(title, description, duplicationCheck);
    }

    @Override
    public Iterable<ActiveCollabIssue> listIssues() throws IssueTrackerException {
        return activeCollab.listIssues();
    }

    @Override
    public Issue getIssue(String issueId) throws IssueTrackerException {
        return activeCollab.getIssue(issueId);
    }

    @Override
    public ActiveCollabIssue updateIssue(ActiveCollabIssue issue) throws IssueTrackerException {
        return activeCollab.updateIssue(issue);
    }

    @Override
    public String toString() {
        return "ActiveCollabService [ac=" + activeCollab + "]";
    }

}
