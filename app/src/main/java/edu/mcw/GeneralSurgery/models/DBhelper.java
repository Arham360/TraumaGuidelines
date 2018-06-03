package edu.mcw.GeneralSurgery.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arham on 2/9/18.
 */

public class DBhelper extends SQLiteOpenHelper {

    static final String DB_NAME = "TRAUMA";
    static final int DB_VERSION = 1;

    Context context;
    SQLiteDatabase sqLiteDatabase;

    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + Topic.TABLE_NAME + Topic.COLUMNS + ";");
        sqLiteDatabase.execSQL("CREATE TABLE " + Content.TABLE_NAME + Content.COLUMNS + ";");
        sqLiteDatabase.execSQL("CREATE TABLE " + Image.TABLE_NAME + Image.COLUMNS + ";");
        sqLiteDatabase.execSQL("CREATE TABLE " + Option.TABLE_NAME + Option.COLUMNS + ";");
        sqLiteDatabase.execSQL("CREATE TABLE " + Prompt.TABLE_NAME + Prompt.COLUMNS + ";");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addTopics(ArrayList<Topic> topics) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        for (int i  = 0 ; i < topics.size(); i++){

            Topic topic = topics.get(i);
            ContentValues values = new ContentValues();
            values.put(Topic.COLUMN_ID, topic.getId());
            values.put(Topic.COLUMN_PARENT_ID, topic.getParentID());
            values.put(Topic.COLUMN_TITLE, topic.getTitle());
            values.put(Topic.COLUMN_TAGS, topic.getTags());
            String summary = topic.getSummary();
            if(summary.equalsIgnoreCase("null")){
                summary = "";
            }
            values.put(Topic.COLUMN_PRIORITY,topic.getPriority());
            values.put(Topic.COLUMN_SUMMARY, summary);
            sqLiteDatabase.insertWithOnConflict(Topic.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        
    }

    public void addContent(ArrayList<Content> contents) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        for (int i  = 0 ; i < contents.size(); i++){

            Content content = contents.get(i);
            ContentValues values = new ContentValues();
            values.put(Content.COLUMN_ID, content.getId());
            values.put(Content.COLUMN_TOPIC_ID, content.getTopicID());
            values.put(Content.COLUMN_DESCRIPTION, content.getDescription());
            sqLiteDatabase.insertWithOnConflict(Content.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();


    }

    public void addimages(ArrayList<Image> images) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        for (int i  = 0 ; i < images.size(); i++){

            Image image = images.get(i);
            ContentValues values = new ContentValues();
            values.put(Image.COLUMN_ID, image.getId());
            values.put(Image.COLUMN_CONTENT_ID, image.getContentID());
            values.put(Image.COLUMN_TITLE, image.getTitle());
            values.put(Image.COLUMN_URL, image.getUrl());
            values.put(Image.COLUMN_PRIORITY, image.getPriority());
            sqLiteDatabase.insertWithOnConflict(Image.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();


    }

    public ArrayList<Topic> getInitialTopics(){
        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Topic.TABLE_NAME + " WHERE " + Topic.COLUMN_PARENT_ID + " <= 0"  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        ArrayList<Topic> retlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_ID));
                int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PARENT_ID));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TAGS));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TITLE));
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_SUMMARY));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PRIORITY));
                Topic topic = new Topic(id, parentId, title, tags, summary, priority);
                retlist.add(topic);
            }while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();
        return retlist;
    }


    public ArrayList<Topic> getTopicsBasedOnID(int mID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Topic.TABLE_NAME + " WHERE " +Topic.COLUMN_PARENT_ID + " = " + mID  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        ArrayList<Topic> retlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_ID));
                int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PARENT_ID));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TAGS));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TITLE));
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_SUMMARY));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PRIORITY));
                Topic topic = new Topic(id, parentId, title, tags, summary,priority);
                retlist.add(topic);

            }while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();
        return retlist;

    }

    public Content getContent(int mID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Content.TABLE_NAME + " WHERE " +Content.COLUMN_ID + " = " + mID  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Content.COLUMN_ID));
                int topicId = cursor.getInt(cursor.getColumnIndexOrThrow(Content.COLUMN_TOPIC_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Content.COLUMN_DESCRIPTION));
                Content content = new Content(id, topicId, title);
            cursor.close();
            sqLiteDatabase.close();
                return content;
        }else
        cursor.close();
        sqLiteDatabase.close();
        return null;

    }


    public ArrayList<Content> getContentFromTopicID(int mID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Content.TABLE_NAME + " WHERE " +Content.COLUMN_TOPIC_ID + " = " + mID  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        ArrayList<Content> retlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Content.COLUMN_ID));
                int topicId = cursor.getInt(cursor.getColumnIndexOrThrow(Content.COLUMN_TOPIC_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Content.COLUMN_DESCRIPTION));
                Content content = new Content(id, topicId, title);
                retlist.add(content);

            }while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();
        return retlist;

    }

    public ArrayList<Image> getImagesFromContentID(int ID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Image.TABLE_NAME + " WHERE " +Image.COLUMN_CONTENT_ID + " = " + ID  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        ArrayList<Image> retlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Image.COLUMN_ID));
                int contentID = cursor.getInt(cursor.getColumnIndexOrThrow(Image.COLUMN_CONTENT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Image.COLUMN_TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(Image.COLUMN_URL));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(Image.COLUMN_PRIORITY));
                Image image = new Image(id, contentID, title, url,priority);
                retlist.add(image);

            }while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();
        return retlist;

    }

    public Topic getTopic(int topicID) {
        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Topic.TABLE_NAME + " WHERE " +Topic.COLUMN_ID + " = " + topicID ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_ID));
                int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PARENT_ID));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TAGS));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TITLE));
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_SUMMARY));
            int priority = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PRIORITY));

            Topic topic = new Topic(id, parentId, title, tags,summary,priority);

            cursor.close();
            sqLiteDatabase.close();
            return topic;

        }else{
            return null;
        }

    }


    public List<Topic> getTopicsBasedOnQuerry(String query) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Topic.TABLE_NAME + " WHERE " + Topic.COLUMN_TITLE + " LIKE " + "'%" + query + "%'"
                + " OR " + Topic.COLUMN_SUMMARY + " LIKE " + "'%" + query + "%'"  +
                " OR " + Topic.COLUMN_TAGS + " LIKE " + "'%" + query + "%'" ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        ArrayList<Topic> retlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_ID));
                int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PARENT_ID));
                String tags = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TAGS));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_TITLE));
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(Topic.COLUMN_SUMMARY));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(Topic.COLUMN_PRIORITY));

                Topic topic = new Topic(id, parentId, title, tags, summary,priority);
                retlist.add(topic);

            }while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();
        return retlist;



    }

    public void addPrompt(ArrayList<Prompt> prompts) {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        for (int i  = 0 ; i < prompts.size(); i++){

            Prompt prompt = prompts.get(i);
            ContentValues values = new ContentValues();
            values.put(prompt.COLUMN_ID, prompt.getId());
            values.put(prompt.COLUMN_CONTENT_ID, prompt.getContentID());
            int isRoot;
            if (prompt.getRoot()){
                isRoot = 1;
            }else{
                isRoot = 0;
            }
            values.put(prompt.COLUMN_IS_ROOT, isRoot);
            values.put(prompt.COLUMN_PROMPT, prompt.getPrompt());
            sqLiteDatabase.insertWithOnConflict(Prompt.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        

    }

    public void addOption(ArrayList<Option> options) {
        sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            for (int i  = 0 ; i < options.size(); i++){

                Option option = options.get(i);
                ContentValues values = new ContentValues();
                values.put(option.COLUMN_ID, option.getId());
                values.put(option.COLUMN_PARENT_ID, option.getParentID());
                values.put(option.COLUMN_TARGET_ID, option.getTargetID());
                values.put(option.COLUMN_PROMPT, option.getPrompt());
                sqLiteDatabase.insertWithOnConflict(Option.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
        
        
    }

    public Prompt getPromptFromContentID(int contentID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Prompt.TABLE_NAME + " WHERE " +Prompt.COLUMN_CONTENT_ID + " = " + contentID ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Prompt.COLUMN_ID));
            int contentId = cursor.getInt(cursor.getColumnIndexOrThrow(Prompt.COLUMN_CONTENT_ID));
            int isRoot = cursor.getInt(cursor.getColumnIndexOrThrow(Prompt.COLUMN_IS_ROOT));
            Boolean root;
            if(isRoot == 1){
                root = true;
            }else{
                root = false;
            }
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Prompt.COLUMN_PROMPT));
            Prompt prompt = new Prompt(id, contentId, root, title);

            cursor.close();
            sqLiteDatabase.close();
            return prompt;
        }else{
            return null;
        }

    }

    public ArrayList<Option> getOptionsFromPrompt(int promptID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Option.TABLE_NAME + " WHERE " +Option.COLUMN_PARENT_ID + " = " + promptID  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        ArrayList<Option> retlist = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Option.COLUMN_ID));
                int contentID = cursor.getInt(cursor.getColumnIndexOrThrow(Option.COLUMN_PARENT_ID));
                int title = cursor.getInt(cursor.getColumnIndexOrThrow(Option.COLUMN_TARGET_ID));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(Option.COLUMN_PROMPT));
                Option option = new Option(id,contentID,title,url);
                retlist.add(option);

            }while (cursor.moveToNext());

        }
        cursor.close();
        sqLiteDatabase.close();
        return retlist;

    }

    public Prompt getPromptFromOptionID(int mParam1) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Prompt.TABLE_NAME + " WHERE " +Prompt.COLUMN_ID + " = " + mParam1 ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Prompt.COLUMN_ID));
            int contentId = cursor.getInt(cursor.getColumnIndexOrThrow(Prompt.COLUMN_CONTENT_ID));
            int isRoot = cursor.getInt(cursor.getColumnIndexOrThrow(Prompt.COLUMN_IS_ROOT));
            Boolean root;
            if(isRoot == 1){
                root = true;
            }else{
                root = false;
            }
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Prompt.COLUMN_PROMPT));
            Prompt prompt = new Prompt(id, contentId, root, title);

            cursor.close();
            sqLiteDatabase.close();
            return prompt;
        }else{
            return null;
        }


    }

    public Image getImage(int ID) {

        sqLiteDatabase = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Image.TABLE_NAME + " WHERE " +Image.COLUMN_ID + " = " + ID  ;
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(Image.COLUMN_ID));
                int contentID = cursor.getInt(cursor.getColumnIndexOrThrow(Image.COLUMN_CONTENT_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Image.COLUMN_TITLE));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(Image.COLUMN_URL));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow(Image.COLUMN_PRIORITY));
                Image image = new Image(id, contentID, title, url,priority);
            cursor.close();
            sqLiteDatabase.close();
                return image;
        }else{
            cursor.close();
            sqLiteDatabase.close();
            return null;
        }

    }

    public String[] getSuggestionsBasedOnText(String newText) {
        return null;
    }
}
