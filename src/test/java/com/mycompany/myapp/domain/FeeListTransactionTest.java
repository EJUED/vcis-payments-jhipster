package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeeListTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeeListTransaction.class);
        FeeListTransaction feeListTransaction1 = new FeeListTransaction();
        feeListTransaction1.setId(1L);
        FeeListTransaction feeListTransaction2 = new FeeListTransaction();
        feeListTransaction2.setId(feeListTransaction1.getId());
        assertThat(feeListTransaction1).isEqualTo(feeListTransaction2);
        feeListTransaction2.setId(2L);
        assertThat(feeListTransaction1).isNotEqualTo(feeListTransaction2);
        feeListTransaction1.setId(null);
        assertThat(feeListTransaction1).isNotEqualTo(feeListTransaction2);
    }
}
