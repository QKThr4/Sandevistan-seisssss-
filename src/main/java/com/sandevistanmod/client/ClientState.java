package com.sandevistanmod.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Estado visivel de cada jogador (implante nas costas, Sandevistan ligado), recebido do servidor. */
public final class ClientState {
    private ClientState() {}

    private static final Map<UUID, Integer> STATES = new HashMap<>();

    public static void set(UUID id, int code) {
        if (code <= 0) STATES.remove(id);
        else STATES.put(id, code);
    }

    public static int get(UUID id) {
        return STATES.getOrDefault(id, 0);
    }

    public static boolean implanted(UUID id) {
        return (get(id) & 1) != 0;
    }

    public static boolean active(UUID id) {
        return (get(id) & 2) != 0;
    }

    public static Set<UUID> activeIds() {
        Set<UUID> out = new HashSet<>();
        for (Map.Entry<UUID, Integer> en : STATES.entrySet()) {
            if ((en.getValue() & 2) != 0) out.add(en.getKey());
        }
        return out;
    }

    public static void clear() {
        STATES.clear();
    }
}
