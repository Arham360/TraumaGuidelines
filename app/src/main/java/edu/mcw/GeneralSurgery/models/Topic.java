package edu.mcw.GeneralSurgery.models;

/**
 * Created by arham on 2/9/18.
 */

public class Topic {

    static final String COLUMN_ID = "ID";
    static final String COLUMN_PARENT_ID = "PARENTID";
    static final String COLUMN_TITLE = "TITLE";
    static final String COLUMN_SUMMARY = "SUMMARY";
    static final String COLUMN_TAGS = "TAGS";
    static final String COLUMN_PRIORITY = "PRIORITY";
    static final String TABLE_NAME = "TOPIC";
    static final String COLUMNS = "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                                        COLUMN_PARENT_ID + " INTEGER NULL," +
                                        COLUMN_TITLE + " TEXT," +
                                        COLUMN_PRIORITY + " INTEGER," +
                                        COLUMN_SUMMARY + " TEXT, " +
                                        COLUMN_TAGS + " TEXT)";

    private int id;
    private int parentID;
    private String title;
    private String tags;
    private String summary;
    private int priority;

    public Topic(int id, int parentID, String title, String tags, String summary, int priority) {
        this.id = id;
        this.parentID = parentID;
        this.title = title;
        this.tags = tags;
        this.summary = summary;
        this.priority = priority;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    public String getSummary() {
        return summary;
    }
}
