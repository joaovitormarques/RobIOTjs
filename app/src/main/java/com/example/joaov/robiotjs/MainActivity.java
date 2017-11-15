package com.example.joaov.robiotjs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import io.github.kbiakov.codeview.CodeView;
import io.github.kbiakov.codeview.classifier.CodeProcessor;
import io.github.kbiakov.codeview.highlight.ColorTheme;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Pubnub pubnub;
    private MainActivity parent;
    private Button runButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = this;
        setContentView(R.layout.activity_main);
        CodeProcessor.init(this);
        CodeView codeView = (CodeView) findViewById(R.id.code_view);
        codeView.setCode(getString(R.string.listing_js), "js");
        codeView.getOptions().withTheme(ColorTheme.MONOKAI);
        runButton = (Button) findViewById(R.id.run_button);

        pubnub = new Pubnub(getString(R.string.publish_key), getString(R.string.subscribe_key));
        try {
            pubnub.subscribe(getString(R.string.pubnub_channel), new Callback() {
                @Override
                public void connectCallback(String channel, Object message) {
                    pubnub.publish(getString(R.string.pubnub_channel), getString(R.string.hello_pubNub), new Callback() {});
                    parent.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(parent.getBaseContext(), R.string.sucess_IOT, Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void disconnectCallback(String channel, Object message) {
                    System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                public void reconnectCallback(String channel, Object message) {
                    System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                            + " : " + message.getClass() + " : "
                            + message.toString());
                }

                @Override
                public void successCallback(String channel, Object message) {
                    System.out.println("SUBSCRIBE : " + channel + " : "
                            + message.getClass() + " : " + message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    System.out.println("SUBSCRIBE : " + channel + " : "
                            + error.getClass() + " : " + error.toString());
                }
        });
        }catch (PubnubException e) {
            Log.e(TAG, e.toString());
            parent.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(parent.getBaseContext(), R.string.erro_IOT, Toast.LENGTH_LONG).show();
                }
            });
        }

        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(parent.getBaseContext(), R.string.submit_code, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        pubnub.unsubscribe("my_channel");
        super.onDestroy();
    }
}
