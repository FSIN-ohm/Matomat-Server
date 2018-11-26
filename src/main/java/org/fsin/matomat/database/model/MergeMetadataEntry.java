package org.fsin.matomat.database.model;

import java.sql.Date;

public class MergeMetadataEntry {
    private Date lastMerge;
    private Integer mergeThreshold;

    public Date getLastMerge() {
        return lastMerge;
    }

    public void setLastMerge(Date lastMerge) {
        this.lastMerge = lastMerge;
    }

    public Integer getMergeThreshold() {
        return mergeThreshold;
    }

    public void setMergeThreshold(Integer mergeThreshold) {
        this.mergeThreshold = mergeThreshold;
    }
}
