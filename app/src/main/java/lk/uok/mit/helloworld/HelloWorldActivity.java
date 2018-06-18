package lk.uok.mit.helloworld;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;

import lk.uok.mit.database.MyDataBaseOperations;
import lk.uok.mit.model.Message;
import lk.uom.mit.adapter.MessageAdapter;

public class HelloWorldActivity extends AppCompatActivity {

    //a variable to hold context
    private Context context = null;

    //a variable to hold the message list retrieved from database
    private List<Message> messages;

    // a variable to hold the list view view
    private ListView listView;

    // a variable to hold the adapter
    private MessageAdapter adapter;

    // a variable to hold the database operations
    private MyDataBaseOperations dataBaseOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
        this.context = getApplicationContext();

        Log.d("HelloWorldActivity ", "cam egere");


        //initialize the List View
        listView = (ListView) findViewById(R.id.listViewMessageList);

        //initialize the database operations
        this.dataBaseOperations = new MyDataBaseOperations(context);

        //retrieve all the saved messages
        this.messages = this.dataBaseOperations.getAllMessages();

        Message message = new Message();
        message.setContactNumber("0112218676");
        message.setSentTime(Calendar.getInstance().getTime());
        message.setRetryCount((short) 1);
        message.setSentStatus(true);
        message.setMessageText("this is message text");
        message.setId(1);
        this.messages.add(message);

        Log.d("this.messages ", this.messages.toString());

        //initialize the adapetr using messages and context
        this.adapter = new MessageAdapter(this.context, this.messages);

        //set the initialized adapter to the list view
        listView.setAdapter(adapter);
    }
}
