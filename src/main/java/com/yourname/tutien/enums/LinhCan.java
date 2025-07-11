package com.yourname.tutien.enums;

import org.bukkit.ChatColor;

public enum LinhCan {
    KHONG_CO("§8Không có Linh Căn", 0.0),
    PHE_PHAM("§7Phế Phẩm Linh Căn", 0.0),
    HA_PHAM("§fHạ Phẩm Linh Căn", 1.0),
    TRUNG_PHAM("§aTrung Phẩm Linh Căn", 3.0),
    THUONG_PHAM("§bThượng Phẩm Linh Căn", 12.0),
    THANH_PHAM("§6§lThánh Phẩm Linh Căn", 72.0);

    private final String tenHienThi;
    private final double heSoLinhKhi;

    LinhCan(String tenHienThi, double heSoLinhKhi) {
        this.tenHienThi = tenHienThi;
        this.heSoLinhKhi = heSoLinhKhi;
    }

    public String getTenHienThi() { return ChatColor.translateAlternateColorCodes('&', tenHienThi); }
    public double getHeSoLinhKhi() { return heSoLinhKhi; }

    public static LinhCan phanLoaiNgauNhien() {
        double random = Math.random() * 100;
        if (random < 4) {
            return THANH_PHAM;
        } else if (random < 14) {
            return THUONG_PHAM;
        } else if (random < 35.5) {
            return TRUNG_PHAM;
        } else if (random < 57) {
            return HA_PHAM;
        } else if (random < 78.5) {
            return PHE_PHAM;
        } else {
            return KHONG_CO;
        }
    }
}
