package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.config.JiraProperties;
import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.JiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class JiraServiceImpl implements JiraService {

    private final JiraProperties jiraProperties;
    private final GroupRepository groupRepository;

    @Override
    public boolean testConnection() {

        String url = jiraProperties.getUrl() + "/rest/api/3/myself";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getStatusCode() == HttpStatus.OK;

        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to Jira: " + e.getMessage());
        }
    }

    private String buildAuthHeader() {
        String auth = jiraProperties.getEmail() + ":" + jiraProperties.getToken();
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes());
        return "Basic " + encoded;
    }

    @Override
    public void createProjectForGroup(int groupId, String projectKey, String projectName) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        if (group.getProjectKey() != null) {
            throw new RuntimeException("Group already has a Jira project");
        }

        String url = jiraProperties.getUrl() + "/rest/api/3/project";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", buildAuthHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
            {
              "key": "%s",
              "name": "%s",
              "projectTypeKey": "software",
              "projectTemplateKey": "com.pyxis.greenhopper.jira:gh-scrum-template"
            }
            """.formatted(projectKey, projectName);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {

                group.setProjectKey(projectKey);
                groupRepository.save(group);
            } else {
                throw new RuntimeException("Failed to create Jira project");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error creating Jira project: " + e.getMessage());
        }
    }
}