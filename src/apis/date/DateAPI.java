package apis.date;

public class DateAPI {

    private static final DateManager dateManager = new DateManager();
    private static final HoursManager hoursManager = new HoursManager() ;

    public static DateManager getDateManager() {
        return dateManager;
    }

    public static HoursManager getHoursManager() {
        return hoursManager;
    }

}
