package com.springboot.rest.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.springboot.rest.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ATest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AOld.class);
        AOld a1 = new AOld();
        a1.setId(1L);
        AOld a2 = new AOld();
        a2.setId(a1.getId());
        assertThat(a1).isEqualTo(a2);
        a2.setId(2L);
        assertThat(a1).isNotEqualTo(a2);
        a1.setId(null);
        assertThat(a1).isNotEqualTo(a2);
    }
}
