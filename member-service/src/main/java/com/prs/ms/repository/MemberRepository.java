package com.prs.ms.repository;


import com.prs.ms.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    List<Member> findByIdIn(List<Long> memberIdList);
}
