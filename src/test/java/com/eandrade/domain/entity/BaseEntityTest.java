package com.eandrade.domain.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

class BaseEntityTest {

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void preservesAnExplicitInactiveStatusOnPersist() {
        TestEntity entity = new TestEntity();
        entity.setStatus(false);

        entity.persist();

        assertThat(entity.getStatus()).isFalse();
        assertThat(entity.getCreatedBy()).isEqualTo("system");
        assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    void initializesStatusWhenItWasNotProvided() {
        TestEntity entity = new TestEntity();
        entity.setStatus(null);

        entity.persist();

        assertThat(entity.getStatus()).isTrue();
    }

    private static class TestEntity extends BaseEntity {
        void persist() {
            onPrePersist();
        }
    }
}
