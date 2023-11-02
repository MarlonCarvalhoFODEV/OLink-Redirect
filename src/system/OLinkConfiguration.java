package system;

import Itens.ITSocialChannel;
import apis.internet.InternetRequisition;
import apis.internet.InternetRequisitionAPI;
import frames.account.UserAccount;
import urls.UrlsConnetionsWEB;
import webconnection.WebJson;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class OLinkConfiguration {

    private static UserAccount account = null;
    private static ITSocialChannel selectedChannel = null;
    private static final ThreadGroup threadGroup = new ThreadGroup("All-Threads");

    private static final int SERVICE_TRIMEST = 9476455;
    private static final int SERVICE_ANUAL = 94741289;

    private static final Map<Integer, Boolean> plans = new HashMap<>();
    private static final InternetRequisitionAPI apiRequisition = new InternetRequisitionAPI();

    private Object controller;

    public static void setAccount(UserAccount ac) {
        account = ac;
        if(ac != null && ac.getSec().getIpPublicFirst() == null)
            ac.getSec().setIpPublicFirst(ac.getSec().getIpPublic());
    }

    public static UserAccount getAccount() {
        return account;
    }

    public static ITSocialChannel getSelectedChannel() {
        return selectedChannel;
    }

    public static void setSelectedChannel(ITSocialChannel selectedChannel) {
        if(selectedChannel != null){
            selectedChannel.setSelected(true);
            OLinkConfiguration.selectedChannel = selectedChannel;
        }
    }

    public static ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public static void setPlans(Map<Integer, Boolean> allPlans) {
        if (plans.isEmpty()) {
            plans.putAll(allPlans);
        }
        for (Integer i : allPlans.keySet());
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public static boolean haveAccess() {
        return !plans.isEmpty();
    }

    public static boolean haveAccess(int plan) {
        return plans != null && !plans.isEmpty() && plans.get(plan) != null;
    }

    public static int getSERVICE_TRIMEST() {
        return SERVICE_TRIMEST;
    }

    public static int getSERVICE_ANUAL() {
        return SERVICE_ANUAL;
    }

    public static final String encrypt(String message){
        String publicKeyBASE64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBEetroW5SaLGQBXh10qFbevk1TNq5L7zQamhFUr4RQ8z9CJOLBukhLm46awkfumGJls2U5+VnPye+NsD5nPwmV79TX1I9OFHU8Loxhae92EsE8G5FV1oNLGNB+VsTD8x6JgX+md+zDMA5grNQNnjQ5tSBuubmbNywQdGI4ElKkQIDAQAB";
        InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(publicKeyBASE64);
        return rsaCryptogrsph.encryptToJson(message);
    }

    public static final String encryptExtended(Object object){
        String otherPublicKeyBASE64 = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjEYuyH1PM2fHvAZPBUq5D0BpRSRP5q34kTImMAPA7w7Y5nHMpDSZBNm8UqlE6/z+mjfTq8io80yawn9H7vg36AStmLmFPjucOcqYAvxP4DRwjbzjyaCfARUv1L9cOUSKqKWWE2If+bTZPOEEqlHedKCe+Tednpfb9FegXYW+5Zg9ylGB/+dN2sM1DMk6U/DXoug0WLLmvvDvgPgzTomS96NBqkIUH/bgs2tnXkMMt05bWqES9GsidHk3QYEfNIgeSbdu0/0M8DhdBy0Ua2mDhRt1J2F9aImczJGH0d75aysqzrtFtYWLB2+5WBf9bRXe3BuN5YvI8ZxRmkdLlo37Lq+I7ys4LJVXK3su8SWExrNGuOk2fju1TL8g54GZtdZgKvPYg47gikusYvLgbSZ843ou2gn30c15cmlgskYRl/BaWXX1ug66PyKBrnE8OGHnA/+DdY0rbvQaO1tDZlWOR7K24YAqrSvXnVRhR9LqRt/xpExfjz+u7uMyirtyy4scZWSYOGXHH//xrnoK1aazpMG4f0me6AbYMbLEBzhbjz9dVAKeE0z6ekv2XcSKA/cs6YTq0OW6SogxEVfpxCyVk+LuCJrcS9m0qwhRxXiS6FoX5FK1KMPU3Ut3qlb+iUstN5zAyYuOdsA6iFv9Es23vc/D6xIN0BAMMR5ldEVhAX8CAwEAAQ==";
        InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(otherPublicKeyBASE64);
        return rsaCryptogrsph.encryptToJson(object);
    }

    public static InternetRequisitionAPI getApiRequisition() {

        if (apiRequisition.getCryptographRSA() == null) {
            String publicKeyBASE64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBEetroW5SaLGQBXh10qFbevk1TNq5L7zQamhFUr4RQ8z9CJOLBukhLm46awkfumGJls2U5+VnPye+NsD5nPwmV79TX1I9OFHU8Loxhae92EsE8G5FV1oNLGNB+VsTD8x6JgX+md+zDMA5grNQNnjQ5tSBuubmbNywQdGI4ElKkQIDAQAB";
            InternetRequisition.CryptographRSA rsaCryptogrsph = new InternetRequisition.CryptographRSA(publicKeyBASE64);

            apiRequisition.setCryptographRSA(rsaCryptogrsph);
        }


        if (apiRequisition.getOtherCryptographRSA() == null) {
            String otherPublicKeyBASE64 = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAjEYuyH1PM2fHvAZPBUq5D0BpRSRP5q34kTImMAPA7w7Y5nHMpDSZBNm8UqlE6/z+mjfTq8io80yawn9H7vg36AStmLmFPjucOcqYAvxP4DRwjbzjyaCfARUv1L9cOUSKqKWWE2If+bTZPOEEqlHedKCe+Tednpfb9FegXYW+5Zg9ylGB/+dN2sM1DMk6U/DXoug0WLLmvvDvgPgzTomS96NBqkIUH/bgs2tnXkMMt05bWqES9GsidHk3QYEfNIgeSbdu0/0M8DhdBy0Ua2mDhRt1J2F9aImczJGH0d75aysqzrtFtYWLB2+5WBf9bRXe3BuN5YvI8ZxRmkdLlo37Lq+I7ys4LJVXK3su8SWExrNGuOk2fju1TL8g54GZtdZgKvPYg47gikusYvLgbSZ843ou2gn30c15cmlgskYRl/BaWXX1ug66PyKBrnE8OGHnA/+DdY0rbvQaO1tDZlWOR7K24YAqrSvXnVRhR9LqRt/xpExfjz+u7uMyirtyy4scZWSYOGXHH//xrnoK1aazpMG4f0me6AbYMbLEBzhbjz9dVAKeE0z6ekv2XcSKA/cs6YTq0OW6SogxEVfpxCyVk+LuCJrcS9m0qwhRxXiS6FoX5FK1KMPU3Ut3qlb+iUstN5zAyYuOdsA6iFv9Es23vc/D6xIN0BAMMR5ldEVhAX8CAwEAAQ==";
            InternetRequisition.CryptographRSA otherRsaCryptogrsph = new InternetRequisition.CryptographRSA(otherPublicKeyBASE64);
            apiRequisition.setOtherCryptographRSA(otherRsaCryptogrsph);
        }

        if (apiRequisition.getCryptographAES() == null) {
            String publicKeyBASE64AES = null;
            InternetRequisition.CryptographAES rsaCryptogrsphAES = new InternetRequisition.CryptographAES(publicKeyBASE64AES);

            apiRequisition.setCryptographAES(rsaCryptogrsphAES);
        }

        apiRequisition.setAuthorization(generateAuthorization());

        return apiRequisition;
    }

    private static final String generateAuthorization() {
        String creds = String.format("%s:%s", "client", "M@7832@78d4de");
        return "Basic " + Base64.getEncoder().encodeToString(creds.getBytes());
    }

    public static class SystemConfigure {
        public static final int SYSTEM_ID = 573192864;
        public static final float VERSION_APP = 2.7f;

        public float getVERSION_APP() {
            return VERSION_APP;
        }
    }
}
