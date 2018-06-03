package edu.mcw.GeneralSurgery.models;

/**
 * Created by arham on 2/27/18.
 */

public class Option {

    static final String COLUMN_ID = "ID";
    static final String COLUMN_PARENT_ID = "PARENTID";
    static final String COLUMN_TARGET_ID = "TARGETID";
    static final String COLUMN_PROMPT = "PROMPT";
    static final String TABLE_NAME = "OPTION";
    static final String COLUMNS = "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_PARENT_ID + " INTEGER," +
            COLUMN_TARGET_ID + " INTEGER," +
            COLUMN_PROMPT + " TEXT)";

    private int id;
    private int parentID;
    private int targetID;
    private String prompt;

    public Option(int id, int parentID, int targetID, String prompt) {
        this.id = id;
        this.parentID = parentID;
        this.targetID = targetID;
        this.prompt = prompt;
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

    public void setParentID(int contentID) {
        this.parentID = contentID;
    }

    public int getTargetID() {
        return targetID;
    }

    public void setTargetID(int targetID) {
        this.targetID = targetID;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
