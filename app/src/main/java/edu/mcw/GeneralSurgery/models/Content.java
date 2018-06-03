package edu.mcw.GeneralSurgery.models;

/**
 * Created by arham on 2/9/18.
 */

public class Content {

    static final String COLUMN_ID = "ID";
    static final String COLUMN_TOPIC_ID = "TOPICID";
    static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    static final String TABLE_NAME = "CONTENT";
    static final String COLUMNS = "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
                                        COLUMN_TOPIC_ID + " INTEGER," +
                                        COLUMN_DESCRIPTION + " TEXT)";

    private int id;
    private int topicID;
    private String description;

    public Content(int id, int topicID, String description) {
        this.id = id;
        this.topicID = topicID;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
