package com.bookwise.service;

import com.bookwise.model.Member;
import com.bookwise.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member updateMember(Long id, Member updated) {
        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found: " + id));
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        return memberRepository.save(existing);
    }

    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}