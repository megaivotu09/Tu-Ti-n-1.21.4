package com.yourname.tutien.tasks;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.player.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LinhKhiTask extends BukkitRunnable {
    private final TuTienPlugin plugin;
    public LinhKhiTask(TuTienPlugin plugin) { this.plugin = plugin; }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (plugin.getRemnantSoulManager().isSoul(player.getUniqueId())) continue;

            PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
            if (data != null) {
                double baseAmount = 1 + data.getTuLuyenInfo().getCanhGioi().getId();
                double finalAmount = baseAmount * data.getLinhCan().getHeSoLinhKhi();
                if (plugin.getMeditationManager().isMeditating(player)) {
                    finalAmount *= plugin.getConfigManager().THIEN_DINH_BONUS;
                }
                if (finalAmount > 0) {
                    data.addLinhKhi((long) finalAmount);
                    // Tự động kiểm tra và thực hiện tiểu đột phá
                    data.performBreakthrough();
                }
            }
        }
    }
}
