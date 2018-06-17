package lk.uok.mit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import lk.uok.mit.model.Message;

public class MyDataBaseOperations {

    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;

    public MyDataBaseOperations(Context context) {
        this.dbHelper = new MyDatabaseHelper(context);
    }

    private void openDatabase(boolean readOnly) {
        if (readOnly) {
            this.database = this.dbHelper.getReadableDatabase();
        } else {
            this.database = this.dbHelper.getWritableDatabase();
        }
    }

    private void closeDatabase() {
        this.dbHelper.close();
    }

    //to format the "sent_time" to a string
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS.SSS");

    //the method to insert a record to "message" table
    public Message createMessage(Message message) {
        //Initialize a variable named “contentValues” of type “android.content.ContentValues”
        ContentValues contentValues = new ContentValues();
        //Put the column names, and values in to contentValues
        contentValues.put(MyDatabaseHelper.COLUMN_CONTACT_NUMBER, message.getContactNumber());
        contentValues.put(MyDatabaseHelper.COLUMN_MESSAGE_TEXT, message.getMessageText());
        contentValues.put(MyDatabaseHelper.COLUMN_SENT_TIME, simpleDateFormat.format(message.getSentTime()));
        contentValues.put(MyDatabaseHelper.COLUMN_SENT_STATUS, message.isSentStatus());
        contentValues.put(MyDatabaseHelper.COLUMN_RETRY_COUNT, message.getRetryCount());
        //create/open a writable database
        this.openDatabase(false);
        //Call insert method of “database” and assigned the returned generated primary key to message
        long generatedId = this.database.insert(MyDatabaseHelper.TABLE_MESSAGE, null, contentValues);
        message.setId(Integer.parseInt(Long.toString(generatedId)));
        //close the database
        this.closeDatabase();
        //return the message with assigned generated key
        return message;
    }

    private static final String[] allColumns = {
            MyDatabaseHelper.COLUMN_ID,
            MyDatabaseHelper.COLUMN_CONTACT_NUMBER,
            MyDatabaseHelper.COLUMN_MESSAGE_TEXT,
            MyDatabaseHelper.COLUMN_SENT_TIME,
            MyDatabaseHelper.COLUMN_SENT_STATUS,
            MyDatabaseHelper.COLUMN_RETRY_COUNT

    };

    public List<Message> getAllMessages() {

        Cursor cursor = database.query(MyDatabaseHelper.TABLE_MESSAGE, allColumns, null, null, null, null, null);

        List<Message> messages = new ArrayList<Message>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.setId(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ID)));
                message.setContactNumber(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_CONTACT_NUMBER)));
                message.setMessageText(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_MESSAGE_TEXT)));
                try {
                    message.setSentTime(simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_SENT_TIME))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                message.setSentStatus(cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_SENT_STATUS)) > 0);
                message.setRetryCount(cursor.getShort(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_RETRY_COUNT)));
                messages.add(message);
            }
        }
        // return All Messages
        return messages;
    }
}
