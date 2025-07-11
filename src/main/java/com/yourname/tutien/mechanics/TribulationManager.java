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
    private final TuTienPlugin plugin;
    private final Map<UUID, Double> playersInTribulation = new HashMap<>();

    public TribulationManager(TuTienPlugin plugin) { this.plugin = plugin; }
    public boolean isInTribulation(Player player) { return playersInTribulation.containsKey(player.getUniqueId()); }

    public void addTamMa(Player player, double damage) {
        if (!isInTribulation(player)) return;
        double currentTamMa = playersInTribulation.getOrDefault(player.getUniqueId(), 0.0);
        playersInTribulation.put(player.getUniqueId(), currentTamMa + damage);
        player.sendMessage("§4[Cảnh Báo] §cTâm ma đang trỗi dậy do ngoại giới can thiệp!");
    }

    public void startTribulation(Player player) {
        if (isInTribulation(player)) {
            player.sendMessage("§cBạn đang trong quá trình độ kiếp!");
            return;
        }
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data == null || !data.canBreakthrough()) {
            player.sendMessage("§cBạn chưa đủ linh khí để đột phá!");
            return;
        }

        playersInTribulation.put(player.getUniqueId(), 0.0);
        player.sendTitle("§4§lLÔI KIẾP", "§cThiên kiếp sắp giáng lâm, hãy chuẩn bị!", 10, 70, 20);

        long excessLinhKhi = data.getExcessLinhKhi();
        int durationInSeconds = 15;
        double lightningMultiplier = 1.0 + (excessLinhKhi / 10000.0 / 100.0);
        int totalLightningStrikes = (int) (10 * lightningMultiplier);

        new BukkitRunnable() {
            int ticksLeft = durationInSeconds * 20;
            int strikesLeft = totalLightningStrikes;

            @Override
            public void run() {
                if (!player.isOnline() || !playersInTribulation.containsKey(player.getUniqueId())) {
                    failTribulation(player, "§cĐộ kiếp bị gián đoạn!");
                    this.cancel();
                    return;
                }
                if (ticksLeft <= 0 || player.isDead()) {
                    handleTribulationResult(player);
                    this.cancel();
                    return;
                }
                if (strikesLeft > 0 && ticksLeft > 0 && ticksLeft % (durationInSeconds * 20 / totalLightningStrikes) == 0) {
                    player.getWorld().strikeLightning(player.getLocation());
                    strikesLeft--;
                }
                ticksLeft--;
            }
        }.runTaskTimer(plugin, 40L, 1L);
    }

    private void handleTribulationResult(Player player) {
        double tamMaValue = playersInTribulation.getOrDefault(player.getUniqueId(), 0.0);
        if (player.isDead()) {
            failTribulation(player, "§cBạn đã thất bại dưới lôi kiếp!");
            return;
        }
        
        if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NANG) tauHoaNhapMa(player);
        else if (tamMaValue >= plugin.getConfigManager().LOI_KIEP_NGUONG_NHE) kinhMachRoiLoan(player);
        else succeedTribulation(player);
    }
    
    private void tauHoaNhapMa(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        plugin.getPlayerDataManager().resetPlayerData(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, Integer.MAX_VALUE, 1));
        Bukkit.broadcastMessage("§4§l[BI KỊCH] §cDo bị ngoại giới can nhiễu, đạo hữu §b" + player.getName() + " §cđã tẩu hỏa nhập ma, tu vi tiêu tán, trở thành phế nhân!");
    }
    
    private void kinhMachRoiLoan(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        player.sendMessage("§cDo bị can thiệp, kinh mạch của bạn bị rối loạn, đột phá thất bại!");
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            long penalty = (long) (data.getLinhKhi() * plugin.getConfigManager().LOI_KIEP_PHAT_NHE);
            data.setLinhKhi(data.getLinhKhi() - penalty);
            player.sendMessage(String.format("§cBạn đã mất §e%,d§c linh khí!", penalty));
        }
    }

    private void succeedTribulation(Player player) {
        playersInTribulation.remove(player.getUniqueId());
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            String canhGioiCu = data.getTuLuyenInfo().getTenHienThiDayDu();
            data.performBreakthrough();
            String canhGioiMoi = data.getTuLuyenInfo().getTenHienThiDayDu();
            player.sendTitle("§a§lTHÀNH CÔNG", "§fChúc mừng đột phá lên §e" + canhGioiMoi, 10, 80, 20);
            Bukkit.broadcastMessage(String.format("§e[Chúc Mừng] §fĐạo hữu §b%s§f đã vượt qua thiên kiếp, từ %s đột phá lên %s!", player.getName(), canhGioiCu, canhGioiMoi));
        }
    }

    public void failTribulation(Player player, String message) {
        playersInTribulation.remove(player.getUniqueId());
        player.sendMessage(message);
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        if (data != null) {
            long penalty = (long) (data.getLinhKhi() * 0.3);
            data.setLinhKhi(data.getLinhKhi() - penalty);
            player.sendMessage(String.format("§cBạn đã mất §e%,d§c linh khí!", penalty));
        }
    }
}
