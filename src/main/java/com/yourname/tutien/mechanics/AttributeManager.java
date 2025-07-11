package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import java.util.UUID;

public class AttributeManager {
    private final TuTienPlugin plugin;
    private static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("a8a3a3e6-71d5-45a2-8447-5d5a6a35368a");
    private static final UUID DEFENSE_MODIFIER_UUID = UUID.fromString("b8b3b3e6-71d5-45a2-8447-5d5a6b35368b");

    public AttributeManager(TuTienPlugin plugin) { this.plugin = plugin; }

    public void updatePlayerAttributes(Player player) {
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null) return;

        StatManager statManager = new StatManager(data.getTuLuyenInfo());
        double maxHealth = statManager.getMaxHealth();
        double defense = statManager.getDefense();
        
        AttributeInstance healthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttribute != null) {
            healthAttribute.getModifiers().stream()
                    .filter(m -> m.getUniqueId().equals(HEALTH_MODIFIER_UUID))
                    .forEach(healthAttribute::removeModifier);

            AttributeModifier healthModifier = new AttributeModifier(HEALTH_MODIFIER_UUID, "tutien_health", maxHealth - 20, AttributeModifier.Operation.ADD_NUMBER);
            healthAttribute.addModifier(healthModifier);
        }
        
        AttributeInstance defenseAttribute = player.getAttribute(Attribute.GENERIC_ARMOR);
        if (defenseAttribute != null) {
            defenseAttribute.getModifiers().stream()
                    .filter(m -> m.getUniqueId().equals(DEFENSE_MODIFIER_UUID))
                    .forEach(defenseAttribute::removeModifier);
            
            AttributeModifier defenseModifier = new AttributeModifier(DEFENSE_MODIFIER_UUID, "tutien_defense", defense, AttributeModifier.Operation.ADD_NUMBER);
            defenseAttribute.addModifier(defenseModifier);
        }
    }
}
