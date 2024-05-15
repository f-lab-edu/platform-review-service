package com.review.rsproject.repository;

import com.review.rsproject.common.ConstantValues;
import com.review.rsproject.domain.Member;
import com.review.rsproject.domain.Platform;
import com.review.rsproject.type.MemberRole;
import com.review.rsproject.type.PlatformStatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 메모리 DB 사용 X
class PlatformRepositoryTest {

    @Autowired PlatformRepository platformRepository;
    @Autowired MemberRepository memberRepository;


    @Test
    @DisplayName("플랫폼 대기 상태 조회")
    void findWaitStatus() {
        // given
        saveBulk(PlatformStatus.WAIT, 12);
        Pageable pageRequest = PageRequest.of(0, ConstantValues.PAGE_SIZE);

        // when
        Page<Platform> platform = platformRepository.findByStatus(pageRequest, PlatformStatus.WAIT);


        // then
        assertThat(platform.getContent().size()).isEqualTo(10);
        assertThat(platform.getContent()).allSatisfy(p -> assertThat(p.getStatus()).isEqualTo(PlatformStatus.WAIT));
    }

    @Test
    @DisplayName("플랫폼 승인 상태 조회")
    void findAcceptStatus() {
        // given
        saveBulk(PlatformStatus.ACCEPT, 12);
        Pageable pageRequest = PageRequest.of(0, ConstantValues.PAGE_SIZE);

        // when
        Page<Platform> platform = platformRepository.findByStatus(pageRequest, PlatformStatus.ACCEPT);


        // then
        assertThat(platform.getContent().size()).isEqualTo(10);
        assertThat(platform.getContent()).allSatisfy(p -> assertThat(p.getStatus()).isEqualTo(PlatformStatus.ACCEPT));
    }


    @Test
    @DisplayName("플랫폼 거부 상태 조회")
    void findDenyStatus() {
        // given
        saveBulk(PlatformStatus.DENY, 12);
        Pageable pageRequest = PageRequest.of(0, ConstantValues.PAGE_SIZE);

        // when
        Page<Platform> platform = platformRepository.findByStatus(pageRequest, PlatformStatus.DENY);


        // then
        assertThat(platform.getContent().size()).isEqualTo(10);
        assertThat(platform.getContent()).allSatisfy(p -> assertThat(p.getStatus()).isEqualTo(PlatformStatus.DENY));
    }


    @Test
    @DisplayName("플랫폼 일반 조회")
    void findDefault() {
        // given
        saveBulk(PlatformStatus.WAIT, 4);
        saveBulk(PlatformStatus.ACCEPT, 4);
        saveBulk(PlatformStatus.DENY, 4);
        Pageable pageRequest = PageRequest.of(0, ConstantValues.PAGE_SIZE);

        // when
        Page<Platform> platform = platformRepository.findByStatus(pageRequest, null);


        // then
        assertThat(platform.getContent().size()).isEqualTo(10);
    }



    private void saveBulk(PlatformStatus status, Integer count) {
        Member member = new Member(UUID.randomUUID().toString().substring(0, 5), "test", MemberRole.ROLE_USER);
        memberRepository.save(member);
        for (int i = 1; i <= count; ++i) {
            Platform platform = new Platform(status.getStatus() + i , "https://tt.com", "test", member);
            platform.changeInfo(null, status);
            platformRepository.save(platform);
        }
    }
}












