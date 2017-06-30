package app.simone.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import app.simone.R;


/**
 * Created by sapi9 on 29/06/2017.
 */
public class SimoneTextView extends AppCompatTextView {

    public SimoneTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public SimoneTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public SimoneTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
                Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/pricedown.ttf");
                setTypeface(myTypeface);
        }
    }

}