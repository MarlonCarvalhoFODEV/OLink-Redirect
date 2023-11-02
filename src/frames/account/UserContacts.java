package frames.account;

import java.io.Serializable;

public class UserContacts implements Serializable {

    private String email ;
    private String cel ;
    private String tel ;
    private SocialContacts socialContacts ;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCel() {
        return cel;
    }

    public void setCel(String cel) {
        this.cel = cel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public SocialContacts getSocialContacts() {
        return socialContacts;
    }

    public void setSocialContacts(SocialContacts socialContacts) {
        this.socialContacts = socialContacts;
    }

    public class SocialContacts{

        private String facebook ;
        private String instagram ;
        private String telegram ;
        private String pinterest ;
        private String whatsApp;

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

        public String getTelegram() {
            return telegram;
        }

        public void setTelegram(String telegram) {
            this.telegram = telegram;
        }

        public String getPinterest() {
            return pinterest;
        }

        public void setPinterest(String pinterest) {
            this.pinterest = pinterest;
        }

        public String getWhatsApp() {
            return whatsApp;
        }

        public void setWhatsApp(String whatsApp) {
            this.whatsApp = whatsApp;
        }
    }
}
