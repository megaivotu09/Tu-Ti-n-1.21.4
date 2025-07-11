package com.yourname.tutien.core;

import com.yourname.tutien.TuTienPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final TuTienPlugin plugin;
    private FileConfiguration config;

    public double THIEN_DINH_BONUS;
    public int NGU_KIEM_YEU_CAU_TANG;
    public double LOI_KIEP_NGUONG_NANG;
    public double LOI_KIEP_NGUONG_NHE;
    public double LOI_KIEP_PHAT_NHE;
    public int CHET_PHAT_ID;
    public int CHET_TAN_HON_ID;

    public ConfigManager(TuTienPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
        
        THIEN_DINH_BONUS = config.getDouble("thien-dinh.he-so-bonus", 3.0);
        NGU_KIEM_YEU_CAU_TANG = config.getInt("kha-nang-bay.ngu-kiem-yeu-cau-tang", 5);
        LOI_KIEP_NGUONG_NANG = config.getDouble("loi-kiep.nguong-tau-hoa-nhap-ma", 30.0);
        LOI_KIEP_NGUONG_NHE = config.getDouble("loi-kiep.nguong-kinh-mach-roi-loan", 10.0);
        LOI_KIEP_PHAT_NHE = config.getDouble("loi-kiep.phan-tram-phat-nhe", 0.5);
        CHET_PHAT_ID = config.getInt("hinh-phat-tu-vong.canh-gioi-bat-dau-phat", 3);
        CHET_TAN_HON_ID = config.getInt("hinh-phat-tu-vong.canh-gioi-bat-dau-tan-hon", 4);
    }
    
    public String getMessage(String path) {
        String message = config.getString("tin-nhan." + path, "&cTin nhắn không tồn tại: " + path);
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
