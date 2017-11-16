package space.dotcat.assistant.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.*;
import android.text.TextUtils;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;


public class InputLayoutErrorMatcher extends TypeSafeMatcher<View> {

    @StringRes
    private int mErrorId;

    private String mExpectedErrorText;

    private InputLayoutErrorMatcher(@StringRes int errorId) {
        mErrorId = errorId;
    }

    public static InputLayoutErrorMatcher matchError(@StringRes int errorId) {
        return new InputLayoutErrorMatcher(errorId);
    }

    @Override
    protected boolean matchesSafely(View item) {
        if(! (item instanceof TextInputEditText)){
            return false;
        }

        mExpectedErrorText = item.getResources().getString(mErrorId);

        TextInputEditText textInputEditText = (TextInputEditText) item;

        return TextUtils.equals(mExpectedErrorText, textInputEditText.getResources().getString(mErrorId));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with error: " + mExpectedErrorText);
    }
}
