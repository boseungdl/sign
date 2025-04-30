package com.signproject.signmanager.common.trace;

import java.util.UUID;

/**
 * 각 요청마다 고유한 트랜잭션 ID와 깊이를 추적하는 객체
 */
public class TraceId {
    private final String id;
    private final int level;

    public TraceId() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
