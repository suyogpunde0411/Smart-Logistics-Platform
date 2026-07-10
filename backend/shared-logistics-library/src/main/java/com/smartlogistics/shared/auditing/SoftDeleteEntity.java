package com.smartlogistics.shared.auditing;

public interface SoftDeleteEntity {
    boolean isDeleted();
    void setDeleted(boolean deleted);
}
