package edu.mcw.GeneralSurgery.models;

/**
 * Created by arham on 2/9/18.
 */

public class Image {

    static final String COLUMN_ID = "ID";
    static final String COLUMN_CONTENT_ID = "CONTENTID";
    static final String COLUMN_TITLE = "TITLE";
    static final String COLUMN_URL = "URL";
    static final String COLUMN_PRIORITY = "PRIORITY";
    static final String TABLE_NAME = "QUESTION";
    static final String COLUMNS = "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                                        COLUMN_CONTENT_ID + " INTEGER," +
                                        COLUMN_PRIORITY + " INTEGER," +
                                        COLUMN_TITLE + " TEXT," +
                                        COLUMN_URL + " TEXT)";

    private int id;
    private int contentID;
    private String title;
    private String url;
    private int priority;

    public Image(int id, int contentID, String title, String url, int priority) {
        this.id = id;
        this.contentID = contentID;
        this.title = title;
        this.url = url;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
