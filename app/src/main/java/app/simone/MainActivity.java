package app.simone;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author Michele Sapignoli
 */
public class MainActivity extends FullscreenActivity {

    private Button VSCpuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VSCpuButton = (Button)findViewById(R.id.button_vs_cpu);


        //Listener on vs CPUActor button
        VSCpuButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openActivity(VSCpuActivity.class);
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_main);
        mContentView = findViewById(R.id.main_fullscreen_content);
        mVisible = true;
    }



}
