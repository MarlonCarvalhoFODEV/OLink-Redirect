package util;

import frames.dashboard.DashboardController;

public class DecodeChannelID {
    public static String decodeIconFromIDChannel(int id){
        switch (id){
            case DashboardController.YOUTUBE: return "youtube_gray.png";
            case DashboardController.FACEBOOK: return "facebook_gray.png";
            case DashboardController.WHATSAPP: return "whatsapp_gray.png";
            case DashboardController.TELEGRAM: return "telegram_gray.png";
            case DashboardController.INSTAGRAM: return "instagram_gray.png";
            case DashboardController.TWITTER: return "twitter_gray.png";
            case DashboardController.PINTEREST: return "pinterest_gray.png";
            case DashboardController.SITE: return "site_gray.png";
            
            default:return "";
        }
    }
}
