package com.yourname.tutien.player;

import com.yourname.tutien.TuTienPlugin;
import com.yourname.tutien.enums.CanhGioi;
import com.yourname.tutien.enums.LinhCan;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private TuLuyenInfo tuLuyenInfo;
    private long linhKhi;
    private final LinhCan linhCan;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.tuLuyenInfo = new TuLuyenInfo(CanhGioi.PHAM_NHAN, 1);
        this.linhKhi = 0;
        this.linhCan = LinhCan.phanLoaiNgauNhien();
    }

    public PlayerData(UUID uuid, CanhGioi canhGioi, int tang, long linhKhi, LinhCan linhCan) {
        this.uuid = uuid;
        this.tuLuyenInfo = new TuLuyenInfo(canhGioi, tang);
        this.linhKhi = linhKhi;
        this.linhCan = linhCan;
    }
    
    public void addLinhKhi(long amount) {
        if (amount > 0) this.linhKhi += amount;
    }

    public boolean canBreakthrough() {
        if (this.tuLuyenInfo.getTang() == 9) return true; // Đại cảnh giới luôn có thể thử độ kiếp
        return linhKhi >= tuLuyenInfo.getLinhKhiCanThiet();
    }
    
    public long getExcessLinhKhi() {
        if (!canBreakthrough()) return 0;
        return this.linhKhi - tuLuyenInfo.getLinhKhiCanThiet();
    }
    
    public void performBreakthrough() {
        // Tiểu đột phá không cần lệnh, tự động diễn ra
        while (this.linhKhi >= tuLuyenInfo.getLinhKhiCanThiet() && this.tuLuyenInfo.getTang() < 9) {
            long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
            this.linhKhi -= linhKhiCanThiet;
            tuLuyenInfo.dotPha();
            
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage("§b[Tiểu Đột Phá] §fLinh khí ngưng tụ, đạo hữu đã đột phá lên §e" + tuLuyenInfo.getTenHienThiDayDu());
                TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
                TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
            }
        }
    }
    
    public void performGrandBreakthrough() {
        if (this.tuLuyenInfo.getTang() != 9) return;
        
        long linhKhiCanThiet = tuLuyenInfo.getLinhKhiCanThiet();
        this.linhKhi -= linhKhiCanThiet;
        tuLuyenInfo.dotPha();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            TuTienPlugin.getInstance().getAttributeManager().updatePlayerAttributes(player);
            TuTienPlugin.getInstance().getFlightManager().updatePlayerFlight(player);
        }
    }
    
    public UUID getUuid() { return uuid; }
    public TuLuyenInfo getTuLuyenInfo() { return tuLuyenInfo; }
    public void setTuLuyenInfo(TuLuyenInfo tuLuyenInfo) { this.tuLuyenInfo = tuLuyenInfo; }
    public long getLinhKhi() { return linhKhi; }
    public void setLinhKhi(long linhKhi) { this.linhKhi = linhKhi; }
    public LinhCan getLinhCan() { return linhCan; }
}
