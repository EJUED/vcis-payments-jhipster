package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeeListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeeList.class);
        FeeList feeList1 = new FeeList();
        feeList1.setId(1L);
        FeeList feeList2 = new FeeList();
        feeList2.setId(feeList1.getId());
        assertThat(feeList1).isEqualTo(feeList2);
        feeList2.setId(2L);
        assertThat(feeList1).isNotEqualTo(feeList2);
        feeList1.setId(null);
        assertThat(feeList1).isNotEqualTo(feeList2);
    }
}
