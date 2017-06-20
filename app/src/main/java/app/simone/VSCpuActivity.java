package app.simone;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import Model.Color;
import Model.interfaces.IActivity;

/**
 * Created by sapi9 on 20/06/2017.
 */

public class VSCpuActivity extends FullscreenActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setSubclassContentView() {
        setContentView(R.layout.activity_vs_cpu);
        mContentView = findViewById(R.id.vs_cpu_fullscreen_content);
    }

}
