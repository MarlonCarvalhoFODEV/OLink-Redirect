package urls;

import system.OLinkConfiguration;
import webconnection.WebJson;

public class UrlsConnetionsWEB {

    public static final String DOMAIN_CONNECTION = "https://olink.me/";
    //public static final String DOMAIN_CONNECTION = "http://192.168.0.3:8080/";

    public class ContentManager{

        public static final String CONTENTS = "contents/";

        public class Method{
            public static final String GET = "get/";
            public static final String NEW_CONTENT = "new-content/";
            public static final String DELETE_CONTENT = "del-content/";
            public static final String UPDATE_CONTENT = "update-content/";
            public static final String GET_SAVEDS_CATEGORIES = "get-categorys-saveds/";
            public static final String GET_PERFORMANCE_CHANNEL = "get-performance-channel/";
            public static final String GET_PERFORMANCE_CONTENT = "get-performance-content/";
        }

        public class Link{
            public static final String LINKS = "";

            public class Method{
                public static final String DELETE_LINK = "linkmanager/del-link/";
                public static final String UPDATE_LINK = "linkmanager/update-link/";
                public static final String AS_AVAILABLE_LINK = "linkmanager/check-available";
                public static final String GET_HISTORIC = "linkmanager/get-month-historic-link/";
                public static final String GET_MONTH_HISTORIC = "linkmanager/get-month-historic-link/";
                public static final String GET_YEAR_HISTORIC = "linkmanager/get-year-historic-link/";
                public static final String GET_DAY_HISTORIC = "linkmanager/get-day-historic-link/";
                public static final String GET_HOUR_HISTORIC = "linkmanager/get-hour-historic-link/";
            }
        }

    }

     public class UpdateControll{
        public static final String UPDATE_CONTROLL = "updatectrl/";

        public class Method{
            public static final String VERIFY_AS_UPDATE = "as-update/";
        }
    }

    public class FacebookControll{
        public static final String FACEBOOK = "facebook/";

        public class Method{
            public static final String SAVE_ACCOUNT_BACKUP = "save-account-backup/";
            public static final String SAVE_PIXEL_EXISTING = "save-pixel-existing/";
            public static final String HATING_PROJECT = "hating/";
        }
    }

    public class VideoTutorial{
        public static final String TUTORIALS = "tutorials/";

        public class Method{
            public static final String GET_LIST_TUTORIALS = "get-list-tutorials/";
        }
    }
    
    public class UserManager{

        public static final String ACCOUNTS = "accounts/";

        public class Methods{
            public static final String REGISTER = "register/";
            public static final String LOGIN = "login/";
            public static final String TYPE_PLAN = "type-plan-access/";
            public static final String REQUEST_PASSWORD = "request-pass/";
            public static final String UPDATE_PASSWORD = "update-pass/";
        }


    }
    
    public class ChannelManager{
        public static final String CHANNEL = "channel/";
        public class Methods{
            public static final String GET = "get-channels/";
            public static final String GET_EVENTS_PIXELS = "get-events-pixels/";
            public static final String CREATE = "insert-channel/";
            public static final String UPDATE = "update-channel/";
        }
    }

    public class PartnerManager{

        public static final String PARTNER = "partners/";

        public class Methods{
            public static final String GET_PANEL_INF = "get-inf-panel/";
        }
    }
    
    public class ServiceManager{
        
        public static final String SERVICES = "services/";
        
        public class Methods{
            public static final String SERVICE = "get-service/";
        }
    }
    
    public class PayManger{

        public static final String PAYMENT = "pay/";

        public class Methods{
            public static final String PAY = "new-pay/";
        }
    }
}
