package tk.nathanf.mttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;

import tk.nathanf.chatthread.components.MessageParameters;
import tk.nathanf.chatthread.components.MessageThreadListAdapter;
import tk.nathanf.chatthread.components.Author;
import tk.nathanf.chatthread.components.MessageThread;
import tk.nathanf.chatthread.components.Message;
import tk.nathanf.chatthread.util.Measure;

public class MainActivity extends AppCompatActivity {

    private static final int DEMO1 = 0;
    private static final int DEMO2 = 1;
    private static final int DEMO3 = 2;

    /**
     * Valid DEMO_NUM values include DEMO1, DEMO2, and DEMO3
     */
    private int DEMO_NUM = DEMO3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Author self = new Author(Message.Source.Self, "Jenny", "https://i.imgur.com/yuty2wv.png");
        final Author other = new Author(Message.Source.Other, "Nathan", "https://dmg-inc.com/wp-content/uploads/2019/03/fisc.png");

        final long nowMs = System.currentTimeMillis();
        final Date now = new Date();
        final Date min2ago = new Date(nowMs - ((2 * 60) * 1000));
        final Date min5ago = new Date(nowMs - ((5 * 60) * 1000));
        final Date min10ago = new Date(nowMs - ((10 * 60) * 1000));
        final Date min15ago = new Date(nowMs - ((15 * 60) * 1000));
        final Date min25ago = new Date(nowMs - ((25 * 60) * 1000));
        final Date min30ago = new Date(nowMs - ((30 * 60) * 1000));
        final Date days1ago = new Date(nowMs - ((60 * 60) * 24) * 1000);

        ArrayList<Message> demoConvo1 = new ArrayList<Message>() {{
            //add(Message.parse(MainActivity.this, other, days1ago, "Hey, make sure you call Jim. His number is +1 (123) 456 - 7890.")[0]);
            add(Message.parse(MainActivity.this, other, days1ago, "Check this out!")[0]);
            add(Message.parse(MainActivity.this, other, days1ago, "View on youtube: https://www.youtube.com/watch?v=E7uGvsT_nnM")[0]);
            add(Message.parse(MainActivity.this, self, min30ago, "Wow, that's crazy!")[0]);
            add(Message.parse(MainActivity.this, self, min2ago, "Do you want to grab dinner tonight?")[0]);
        }};

        ArrayList<Message> demoConvo2 = new ArrayList<Message>() {{
            add(Message.parse(MainActivity.this, other, min30ago, "Hey, What's up?")[0]);
            add(Message.parse(MainActivity.this, self, min30ago, "Just got back from the beach. Took some nice photos.")[0]);
            add(Message.parse(MainActivity.this, self, min30ago, "Want to see?")[0]);
            add(Message.parse(MainActivity.this, other, min25ago, "Send them my way! \uD83D\uDE0A")[0]);
            add(Message.parse(MainActivity.this, self, min30ago, "https://i.imgur.com/ml4VjZw.jpg")[0]);
        }};

        ArrayList<Message> demoConvo3 = new ArrayList<Message>() {{
            add(Message.parse(MainActivity.this, other, min25ago, "What're you doing tonight?")[0]);
            add(Message.parse(MainActivity.this, self, min25ago, "Probably going to stay in, make some dinner. Any ideas of what I should make?")[0]);
            add(Message.parse(MainActivity.this, other, min10ago, "Here are some options!")[0]);
            add(Message.parse(MainActivity.this, other, min10ago, "https://tasty.co/article/melissaharrison/easy-dinner-recipes")[0]);
        }};

        final MessageThread thread = findViewById(R.id.thread);

        if (DEMO_NUM == DEMO1) {
            thread.setDisplayOutgoingAvatars(false);
            thread.setAdapter(new MessageThreadListAdapter(demoConvo1));
        } else if (DEMO_NUM == DEMO2) {
            thread.setDisplayOutgoingAvatars(true);
            thread.setAvatarShape(MessageParameters.AvatarShape.RoundSquare);
            thread.setMessageRadiusPx(Measure.dp8(this), Measure.dp8(this), Measure.dp8(this), Measure.dp8(this));
            thread.setTextMessagePadding((int)Measure.dpToPx(10, this));
            thread.setAdapter(new MessageThreadListAdapter(demoConvo2));
        } else if (DEMO_NUM == DEMO3) {
            thread.setDisplayOutgoingAvatars(false);
            thread.setMessageRadiusPx(Measure.dpToPx(25, this), Measure.dpToPx(25, this), Measure.dpToPx(25, this), Measure.dpToPx(25, this));
            thread.setSentColor(getResources().getColor(R.color.alternateSent));
            thread.setAdapter(new MessageThreadListAdapter(demoConvo3));
        } else {
            throw new RuntimeException("Valid DEMO_NUM values include DEMO1, DEMO2 and DEMO3");
        }

        final TextView textView = findViewById(R.id.inputText);
        Button button = findViewById(R.id.send_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread.getAdapter().addToBottom(
                    Message.parse(MainActivity.this, self, textView.getText().toString()),
                    true
                );
                textView.setText("");
            }
        });
    }
}