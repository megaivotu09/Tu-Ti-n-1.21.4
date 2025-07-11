// Vị trí: src/main/java/com/yourname/tutien/mechanics/MeditationManager.java
package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MeditationManager {
    // ... các biến và constructor giữ nguyên ...
    private final TuTienPlugin plugin;
    private final Set<UUID> meditatingPlayers = new HashSet<>();
    private final Map<UUID, ArmorStand> meditationSeats = new HashMap<>();
    public MeditationManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isMeditating(Player player) { return meditatingPlayers.contains(player.getUniqueId()); }
    public Set<UUID> getMeditatingPlayers() { return meditatingPlayers; }
    public void stopMeditation(Player player) { /* ... giữ nguyên ... */ }


    // HÀM QUAN TRỌNG CẦN SỬA
    public void startMeditation(Player player) {
        if (isMeditating(player)) {
            stopMeditation(player);
            return;
        }
        if (!player.isOnGround()) {
            player.sendMessage(plugin.getConfigManager().getMessage("khong-the-thien"));
            return;
        }

        // --- SỬA LỖI TẠI ĐÂY ---
        // Lấy vị trí của người chơi
        Location playerLocation = player.getLocation();
        // Tạo một vị trí mới cho ArmorStand.
        // Trừ đi khoảng 0.5 block theo trục Y để người chơi "ngồi" xuống thấp hơn một chút, trông sẽ tự nhiên.
        // Không trừ quá nhiều để tránh bị tụt xuống đất.
        Location seatLocation = playerLocation.clone().subtract(0, 0.5, 0);

        ArmorStand seat = (ArmorStand) player.getWorld().spawnEntity(seatLocation, EntityType.ARMOR_STAND);

        seat.setGravity(false);
        seat.setVisible(false);
        seat.setInvulnerable(true);
        seat.setMarker(true);

        // Đặt người chơi lên ArmorStand
        seat.addPassenger(player);

        meditatingPlayers.add(player.getUniqueId());
        meditationSeats.put(player.getUniqueId(), seat);

        player.sendMessage(plugin.getConfigManager().getMessage("thien-bat-dau"));
        player.sendMessage("§7Gõ §e/tuluyen §7lần nữa hoặc di chuyển để thoát.");
    }
}
