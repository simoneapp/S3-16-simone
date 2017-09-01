package app.simone.shared.styleable;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Custom textview.
 * @author Michele Sapignoli
 */
public class MainTextView extends AppCompatTextView {

    public MainTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public MainTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    public MainTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        if (attrs!=null) {
            Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/BEBAS.ttf");
            setTypeface(myTypeface);
        }
    }

}