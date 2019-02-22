package org.fsin.matomat.database.model;

import java.math.BigDecimal;
import java.sql.Date;

public class TransferEntry extends TransactionEntry {

    private BigDecimal charged_amount;

    public BigDecimal getCharged_amount() {
        return charged_amount;
    }

    public void setCharged_amount(BigDecimal charged_amount) {
        this.charged_amount = charged_amount;
    }
}
