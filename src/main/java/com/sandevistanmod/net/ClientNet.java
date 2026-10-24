package com.sandevistanmod.net;

import com.sandevistanmod.client.ClientData;
import net.minecraft.core.RegistryAccess;

public final class ClientNet {
    private ClientNet() {}

    public static void handleSync(SyncPayload payload) {
        ClientData.DATA.deserializeNBT(RegistryAccess.EMPTY, payload.tag());
    }
}
