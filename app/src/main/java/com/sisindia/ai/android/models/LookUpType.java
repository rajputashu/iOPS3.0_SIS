package com.sisindia.ai.android.models;

public enum LookUpType {

    UNKNOWN(0),
    REASON(19),
    SUB_TASK_TYPE(51),
    CHECKLIST(20),
    GUARD_NOT_AVAILABLE(21),
    GUARD_TURNOUT(22),
    REWARD_VALUES(29),
    FINE_VALUES(30),
    SECURITY_RISK_CATEGORY(32),
    IMPROVEMENT_PLAN_TYPE(25),
    IMPROVEMENT_PLAN_CATEGORY(25),
    GRIEVANCE_CATEGORY(12),
    COMPLAINT_MODE(5),
    COMPLAINT_TYPE(6),
    COMPLAINT_NATURE(7);

    private final int typeId;

    LookUpType(int typeId) {
        this.typeId = typeId;
    }

    public static LookUpType of(int typeId) {
        for (LookUpType type : LookUpType.values()) {
            if (type.typeId == typeId) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getTypeId() {
        return typeId;
    }
}
