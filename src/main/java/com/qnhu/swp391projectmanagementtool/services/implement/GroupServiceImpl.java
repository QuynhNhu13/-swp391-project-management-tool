package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.dtos.GroupResponse;
import com.qnhu.swp391projectmanagementtool.entities.*;
import com.qnhu.swp391projectmanagementtool.enums.Role;
import com.qnhu.swp391projectmanagementtool.mappers.GroupMapper;
import com.qnhu.swp391projectmanagementtool.repositories.GroupRepository;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import com.qnhu.swp391projectmanagementtool.services.interfaces.IGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements IGroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    // ==============================
    // CREATE GROUP
    // ==============================

    @Override
    public GroupResponse createGroup(String groupName) {

        if (groupRepository.existsByGroupName(groupName)) {
            throw new RuntimeException("Group name already exists");
        }

        Group group = new Group();
        group.setGroupName(groupName);

        Group saved = groupRepository.save(group);

        return GroupMapper.toResponse(saved);
    }

    // ==============================
    // ASSIGN LECTURER
    // ==============================

    @Override
    public void assignLecturer(int groupId, int lecturerId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ROLE_LECTURER) {
            throw new RuntimeException("User is not Lecturer");
        }

        group.setLecturer((Lecturer) user);
        groupRepository.save(group);
    }

    // ==============================
    // ADD MEMBER
    // ==============================

    @Override
    public void addMember(int groupId, int userId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new RuntimeException("Admin cannot join group");
        }

        if (group.getMembers().contains(user)) {
            throw new RuntimeException("User already in group");
        }

        group.addMember(user);
        groupRepository.save(group);
    }

    // ==============================
    // ASSIGN LEADER
    // ==============================

    @Override
    public void assignLeader(int groupId, int leaderId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        User leader = userRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (leader.getRole() != Role.ROLE_LEADER) {
            throw new RuntimeException("User is not Leader");
        }

        if (!group.getMembers().contains(leader)) {
            throw new RuntimeException("Leader must be member of group");
        }

        group.setTeamLeader(leader);
        groupRepository.save(group);
    }

    // ==============================
    // GET ALL GROUPS
    // ==============================

    @Override
    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(GroupMapper::toResponse)
                .toList();
    }

    // ==============================
    // GET GROUP BY ID
    // ==============================

    @Override
    public GroupResponse getGroupById(int groupId) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return GroupMapper.toResponse(group);
    }

    // ==============================
    // DELETE GROUP
    // ==============================

    @Override
    public void deleteGroup(int groupId) {
        groupRepository.deleteById(groupId);
    }

    // ==============================
    // GET GROUPS BY CURRENT USER ROLE
    // ==============================

    @Override
    public List<GroupResponse> getGroupsByCurrentUser(User currentUser) {

        List<Group> groups;

        switch (currentUser.getRole()) {

            case ROLE_ADMIN:
                groups = groupRepository.findAll();
                break;

            case ROLE_LECTURER:
                groups = groupRepository.findByLecturer((Lecturer) currentUser);
                break;

            case ROLE_LEADER:
                groups = groupRepository.findByTeamLeader(currentUser);
                break;

            case ROLE_MEMBER:
                groups = groupRepository.findByMembersContaining(currentUser);
                break;

            default:
                throw new RuntimeException("Invalid role");
        }

        return groups.stream()
                .map(GroupMapper::toResponse)
                .toList();
    }
}