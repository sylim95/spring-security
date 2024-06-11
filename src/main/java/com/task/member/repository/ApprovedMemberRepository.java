package com.task.member.repository;

import com.task.member.domain.ApproveMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovedMemberRepository extends JpaRepository<ApproveMember, Integer> {
    List<ApproveMember> findApproveMemberByName(String name);

}
