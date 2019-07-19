package tk.nathanf.mttest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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

        ArrayList<Message> demoConvo1 = new ArrayList<Message>() {{
            add(Message.parse(MainActivity.this, other, "Hey, make sure you call Jim. His number is +1 (123) 456 - 7890.")[0]);
            add(Message.parse(MainActivity.this, self, "Thanks!")[0]);
            add(Message.parse(MainActivity.this, other, "Have you seen this? https://www.youtube.com/watch?v=E7uGvsT_nnM")[0]);
            add(Message.parse(MainActivity.this, self, "Yeah, that's crazy!")[0]);
            add(Message.parse(MainActivity.this, self, "Do you want to grab dinner tonight?")[0]);
        }};

        ArrayList<Message> demoConvo2 = new ArrayList<Message>() {{
            add(Message.parse(MainActivity.this, other, "Hey, What's up?")[0]);
            add(Message.parse(MainActivity.this, self, "Just got back from the beach. Took some nice photos.")[0]);
            add(Message.parse(MainActivity.this, self, "Want to see?")[0]);
            add(Message.parse(MainActivity.this, other, "Sure!")[0]);
            add(Message.parse(MainActivity.this, other, "Send them my way! \uD83D\uDE0A")[0]);
            add(Message.parse(MainActivity.this, self, "https://i.imgur.com/ml4VjZw.jpg")[0]);
        }};

        ArrayList<Message> demoConvo3 = new ArrayList<Message>() {{
            add(Message.parse(MainActivity.this, other, "What're you doing tonight?")[0]);
            add(Message.parse(MainActivity.this, self, "Probably going to stay in, make some dinner.")[0]);
            add(Message.parse(MainActivity.this, self, "Any ideas of what I should make?")[0]);
            add(Message.parse(MainActivity.this, other, "Here are some options!")[0]);
            add(Message.parse(MainActivity.this, other, "https://tasty.co/article/melissaharrison/easy-dinner-recipes")[0]);
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