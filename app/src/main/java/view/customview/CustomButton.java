package view.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomButton extends AppCompatButton {

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomButton(Context context) {
        super(context);
        init();
    }

    public void init() {
        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Bold.ttf");
        setTypeface(tf);
    }
}
