package com.tobipristupin.simplerun.ui.login;


import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;

import com.tobipristupin.simplerun.R;

/**
 * Class that contains base methods used in all login fragments
 */
public class BaseLoginFragment extends Fragment {

    /**
     * Configures all TextInputLayout to remove their errors every time text is inputted
     */
    protected void setLayoutErrorReset(TextInputLayout ... textInputLayouts) {
        for (TextInputLayout layout : textInputLayouts) {
            layout.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    layout.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

}
