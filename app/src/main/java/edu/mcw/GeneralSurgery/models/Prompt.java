package edu.mcw.GeneralSurgery.models;

/**
 * Created by arham on 2/27/18.
 */

public class Prompt {

    static final String COLUMN_ID = "ID";
    static final String COLUMN_CONTENT_ID = "CONTENTID";
    static final String COLUMN_IS_ROOT = "ISROOT";
    static final String COLUMN_PROMPT = "PROMPT";
    static final String TABLE_NAME = "PROMPT";
    static final String COLUMNS = "(" + COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_CONTENT_ID + " INTEGER," +
            COLUMN_IS_ROOT + " INTEGER," +
            COLUMN_PROMPT + " TEXT)";

    private int id;
    private int contentID;
    private Boolean isRoot;
    private String prompt;

    public Prompt(int id, int contentID, Boolean isRoot, String prompt) {
        this.id = id;
        this.contentID = contentID;
        this.isRoot = isRoot;
        this.prompt = prompt;
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

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
