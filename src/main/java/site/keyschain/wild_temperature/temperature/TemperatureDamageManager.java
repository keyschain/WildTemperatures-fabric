package site.keyschain.wild_temperature.temperature;

import static site.keyschain.wild_temperature.WildTemperature.CONFIG;
import static site.keyschain.wild_temperature.temperature.EnvironmentChecks.isInWater;
import static site.keyschain.wild_temperature.temperature.EquipmentChecks.*;

import net.minecraft.server.network.ServerPlayerEntity;

public class TemperatureDamageManager {
    public static boolean isTakingTemperatureDamage = false;
    public static float EXTREME_HEAT_THRESHOLD = CONFIG.EXTREME_HEAT_THRESHOLD();
    public static final float EXTREME_COLD_THRESHOLD = CONFIG.EXTREME_COLD_THRESHOLD();
    public static final float EXTREME_HEAT_DAMAGE = CONFIG.EXTREME_HEAT_DAMAGE();
    public static final float EXTREME_COLD_DAMAGE = CONFIG.EXTREME_COLD_DAMAGE();

    public static void applyTemperatureDamage(ServerPlayerEntity player) {
        float temperature = TemperatureManager.playerTemperature;

        boolean extremeHeat = temperature >= EXTREME_HEAT_THRESHOLD;
        boolean extremeCold = temperature <= EXTREME_COLD_THRESHOLD;

        if (extremeHeat) {
            doExtremeHeatDamage(player);
            return;
        }

        if (extremeCold) {
            doExtremeColdDamage(player);
            return;
        }

        isTakingTemperatureDamage = false;
    }

    private static void doExtremeHeatDamage(ServerPlayerEntity player) {

        // is in water
        if (isInWater(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // enchanted armor with fire protection
        if (hasFireProtection(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // armor like chainmail
        if (hasHeatProtectionArmor(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // potion of fire resistance
        if (hasFireResistance(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        player.damage(player.getWorld().getDamageSources().onFire(), EXTREME_HEAT_DAMAGE);
        isTakingTemperatureDamage = true;
    }

    private static void doExtremeColdDamage(ServerPlayerEntity player) {

        // enchanted armor with frost protection
        if (hasFrostProtection(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // armor like leather
        if (hasColdProtectionArmor(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        player.damage(player.getWorld().getDamageSources().freeze(), EXTREME_COLD_DAMAGE);
        isTakingTemperatureDamage = true;
    }
}