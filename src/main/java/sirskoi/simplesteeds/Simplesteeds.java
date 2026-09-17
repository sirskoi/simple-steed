package sirskoi.simplesteeds;

import net.fabricmc.api.ModInitializer;

public class Simplesteeds implements ModInitializer {
    @Override
    public void onInitialize() {
        modconfig.load();
        loottable.register();
    }
}