package com.bookwise.service;

import com.bookwise.model.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member addMember(Member member);
    List<Member> getAllMembers();
    Optional<Member> getMemberById(Long id);
    Member updateMember(Long id, Member member);
    void deleteMember(Long id);
}