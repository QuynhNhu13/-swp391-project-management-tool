package com.qnhu.swp391projectmanagementtool.controllers;

import com.qnhu.swp391projectmanagementtool.dtos.CreateGroupRequest;
import com.qnhu.swp391projectmanagementtool.dtos.GroupResponse;
import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.services.interfaces.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final IGroupService groupService;

    // ==============================
    // CREATE GROUP
    // ==============================

    @PostMapping
    public GroupResponse createGroup(@RequestBody CreateGroupRequest request) {
        return groupService.createGroup(request.getGroupName());
    }

    // ==============================
    // ASSIGN LECTURER
    // ==============================

    @PutMapping("/{groupId}/lecturer/{lecturerId}")
    public void assignLecturer(@PathVariable int groupId,
                               @PathVariable int lecturerId) {
        groupService.assignLecturer(groupId, lecturerId);
    }

    // ==============================
    // ADD MEMBER
    // ==============================

    @PutMapping("/{groupId}/member/{userId}")
    public void addMember(@PathVariable int groupId,
                          @PathVariable int userId) {
        groupService.addMember(groupId, userId);
    }

    // ==============================
    // ASSIGN LEADER
    // ==============================

    @PutMapping("/{groupId}/leader/{leaderId}")
    public void assignLeader(@PathVariable int groupId,
                             @PathVariable int leaderId) {
        groupService.assignLeader(groupId, leaderId);
    }

    // ==============================
    // GET ALL GROUPS
    // ==============================

    @GetMapping
    public List<GroupResponse> getAllGroups() {
        return groupService.getAllGroups();
    }

    // ==============================
    // GET GROUP BY ID
    // ==============================

    @GetMapping("/{groupId}")
    public GroupResponse getGroup(@PathVariable int groupId) {
        return groupService.getGroupById(groupId);
    }

    // ==============================
    // DELETE GROUP
    // ==============================

    @DeleteMapping("/{groupId}")
    public void deleteGroup(@PathVariable int groupId) {
        groupService.deleteGroup(groupId);
    }

    // ==============================
    // GET GROUPS BY CURRENT USER
    // ==============================

    @GetMapping("/my-groups")
    public List<GroupResponse> getMyGroups(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return groupService.getGroupsByCurrentUser(currentUser);
    }
}