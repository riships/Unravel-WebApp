package com.code.enhancement.apis;

import java.util.*;

public class MemoryManager {
    private static final Map<String, byte[]> largeSessionData = Collections.synchronizedMap(new HashMap<>());

    public static void addSessionData(String sessionId) {
        largeSessionData.put(sessionId, new byte[10 * 1024 * 1024]); // 10MB per session
    }

    public static void removeSessionData(String sessionId) {
        largeSessionData.remove(sessionId);
    }

    public static int getActiveSessionCount() {
        synchronized (largeSessionData) {
            return largeSessionData.size();
        }
    }
}
