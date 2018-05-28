package com.tobipristupin.simplerun.ui.login;


import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.tobipristupin.simplerun.app.BaseFragment;
import com.tobipristupin.simplerun.ui.main.MainActivityView;

/**
 * Class that contains base methods used in all login fragments
 */
public abstract class BaseLoginFragment extends BaseFragment {

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

    protected void sendMainActivityIntent(){
        Intent intent = new Intent(getContext(), MainActivityView.class);
        //Flags prevent user from returning to LoginActivity when pressing back button
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
