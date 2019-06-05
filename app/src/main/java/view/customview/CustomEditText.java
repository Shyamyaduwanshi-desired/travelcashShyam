package view.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public void init() {
        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Light.ttf");
        setTypeface(tf);
    }
}
