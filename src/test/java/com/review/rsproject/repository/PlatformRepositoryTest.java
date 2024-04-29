package com.review.rsproject.repository;

import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 메모리 DB 사용 X
class PlatformRepositoryTest {

    @Autowired PlatformRepository platformRepository;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("플랫폼 상태 값에 따른 조회 테스트")
    void findStatus() {

        // given
        Member member = new Member("test", "test", MemberRole.ROLE_USER);
        memberRepository.save(member);

        // 각 상태별 데이터를 15개씩 저장
        for (PlatformStatus status : PlatformStatus.values()) {
            for (int i = 1; i <= 15; ++i) {

                Platform platform = new Platform(status.getStatus() + i , "https://tt.com", "test", member);
                platform.changeInfo(null, status);
                platformRepository.save(platform);
            }
        }

        // when
        Pageable pageRequest = PageRequest.of(0, 10);

        Page<Platform> platform_wait = platformRepository.findByStatus(pageRequest, PlatformStatus.WAIT);

        Page<Platform> platform_accept = platformRepository.findByStatus(pageRequest, PlatformStatus.ACCEPT);
        Page<Platform> platform_deny = platformRepository.findByStatus(pageRequest, PlatformStatus.DENY);

        Page<Platform> platform_all = platformRepository.findByStatus(pageRequest, null);


        // then
        assertThat(platform_wait.getContent().size()).isEqualTo(10);
        assertThat(platform_wait.getContent()).allSatisfy(platform -> assertThat(platform.getStatus()).isEqualTo(PlatformStatus.WAIT));

        assertThat(platform_accept.getContent().size()).isEqualTo(10);
        assertThat(platform_accept.getContent()).allSatisfy(platform -> assertThat(platform.getStatus()).isEqualTo(PlatformStatus.ACCEPT));

        assertThat(platform_deny.getContent().size()).isEqualTo(10);
        assertThat(platform_deny.getContent()).allSatisfy(platform -> assertThat(platform.getStatus()).isEqualTo(PlatformStatus.DENY));

        assertThat(platform_all.getContent().size()).isEqualTo(10);

    }

}












