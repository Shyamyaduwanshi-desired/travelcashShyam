package view.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class CustomTextInput extends TextInputEditText {

    public CustomTextInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextInput(Context context) {
        super(context);
        init();
    }

    public void init() {
        //Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Regular.ttf");
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto_Light.ttf");
        setTypeface(tf);
    }
}
