package com.qnhu.swp391projectmanagementtool.repositories;

import com.qnhu.swp391projectmanagementtool.entities.Group;
import com.qnhu.swp391projectmanagementtool.entities.Lecturer;
import com.qnhu.swp391projectmanagementtool.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {

    boolean existsByGroupName(String groupName);

    // ==============================
    // THÊM 3 METHOD NÀY
    // ==============================

    List<Group> findByLecturer(Lecturer lecturer);

    List<Group> findByTeamLeader(User teamLeader);

    List<Group> findByMembersContaining(User member);
}