package util;

import java.io.Serializable;

public class UpdateApp implements Serializable {

    private int id ;
    private String title, newModifications, date;
    private float version ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewModifications() {
        return newModifications;
    }

    public void setNewModifications(String newModifications) {
        this.newModifications = newModifications;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }
}
