package com.qnhu.swp391projectmanagementtool.services.interfaces;

public interface JiraService {

    boolean testConnection();

    void createProjectForGroup(int groupId, String projectKey, String projectName);
}