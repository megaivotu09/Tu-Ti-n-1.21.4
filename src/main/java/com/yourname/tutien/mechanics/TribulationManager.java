// Vị trí: src/main/java/com/yourname/tutien/mechanics/TribulationManager.java
package com.yourname.tutien.mechanics;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TribulationManager {
    // ... các biến và constructor giữ nguyên ...
    private final TuTienPlugin plugin;
    private final Map<UUID, Double> playersInTribulation = new HashMap<>();
    public TribulationManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isInTribulation(Player player) { return playersInTribulation.containsKey(player.getUniqueId()); }
    public void addTamMa(Player player, double damage) { /* ... giữ nguyên ... */ }
    public void startTribulation(Player player) { /* ... giữ nguyên ... */ }
    private void handleTribulationResult(Player player) { /* ... giữ nguyên ... */ }
    private void tauHoaNhapMa(Player player) { /* ... giữ nguyên ... */ }
    private void kinhMachRoiLoan(Player player) { /* ... giữ nguyên ... */ }
    public void failTribulation(Player player, String message) { /* ... giữ nguyên ... */ }


    // HÀM QUAN TRỌNG CẦN SỬA
    private void succeedTribulation(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            String canhGioiCu = data.getTuLuyenInfo().getTenHienThiDayDu();
            
            // Thực hiện đột phá
            data.performBreakthrough();
            
            String canhGioiMoi = data.getTuLuyenInfo().getTenHienThiDayDu();

            // Gửi log để kiểm tra
            plugin.getLogger().info(String.format("Player %s breakthrough SUCCESS: %s -> %s", player.getName(), canhGioiCu, canhGioiMoi));

            // Thông báo
            player.sendTitle("§a§lTHÀNH CÔNG", "§fChúc mừng đột phá lên §e" + canhGioiMoi, 10, 80, 20);
            Bukkit.broadcastMessage(String.format("§e[Chúc Mừng] §fĐạo hữu §b%s§f đã vượt qua thiên kiếp, từ %s đột phá lên %s!",
                    player.getName(), canhGioiCu, canhGioiMoi));
            
            // Quan trọng: Gọi lại các hàm update ngay lập tức để người chơi thấy thay đổi
            plugin.getAttributeManager().updatePlayerAttributes(player);
            plugin.getFlightManager().updatePlayerFlight(player);
            plugin.getScoreboardManager().updateScoreboard(player);
            plugin.getBossBarManager().updateBossBar(player);
        }
    }
}
