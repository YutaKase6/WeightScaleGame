package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBObject;
import com.nifty.cloud.mb.core.NCMBUser;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A login screen that offers login via user name/password.
 */
public class InputUserDataActivity extends AppCompatActivity implements DoneCallback {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserSignUpTask mAuthTask = null;

    // UI references.
    @Bind(R.id.user_name_text_view)
    AutoCompleteTextView userNameView;
    @Bind(R.id.password)
    EditText passwordView;
    @Bind(R.id.current_weight_edit_text)
    EditText currentWeightView;
    @Bind(R.id.goal_weight_edit_text)
    EditText goalWeightView;
    @Bind(R.id.login_progress)
    View progressView;
    @Bind(R.id.sign_up_form)
    View signUpFormView;

    @BindString(R.string.error_field_required)
    String errorFieldRequired;
    @BindString(R.string.error_field_not_number)
    String errorFieldNotNumber;
    @BindString(R.string.user_name_key)
    String userNameKey;
    @BindString(R.string.password_key)
    String passwordKey;
    @BindString(R.string.start_weight_key)
    String startWeightKey;
    @BindString(R.string.goal_weight_key)
    String goalWeightKey;
    @BindString(R.string.open_user_data_class)
    String openUserData;
    @BindString(R.string.offset_weight_key)
    String offsetWeightKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_user_data);
        ButterKnife.bind(this);
        // Set up the login form.
        this.passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptSignUp();
                    return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        String userName = intent.getStringExtra(this.userNameKey);
        String password = intent.getStringExtra(this.passwordKey);

        this.userNameView.setText(userName);
        this.passwordView.setText(password);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @OnClick(R.id.email_sign_up_button)
    void attemptSignUp() {
        if (this.mAuthTask != null) {
            return;
        }

        // Reset errors.
        this.userNameView.setError(null);
        this.passwordView.setError(null);
        this.currentWeightView.setError(null);
        this.goalWeightView.setError(null);

        // Store values at the time of the login attempt.
        String userName = this.userNameView.getText().toString();
        String password = this.passwordView.getText().toString();
        String currentWeight = this.currentWeightView.getText().toString();
        String goalWeight = this.goalWeightView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            this.passwordView.setError(this.errorFieldRequired);
            focusView = this.passwordView;
            cancel = true;
        }

        // Check for a valid user name.
        if (TextUtils.isEmpty(userName)) {
            this.userNameView.setError(this.errorFieldRequired);
            focusView = this.userNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(currentWeight)) {
            this.currentWeightView.setError(this.errorFieldRequired);
            focusView = this.currentWeightView;
            cancel = true;
        }

        if (TextUtils.isEmpty(goalWeight)) {
            this.goalWeightView.setError(this.errorFieldRequired);
            focusView = this.currentWeightView;
            cancel = true;
        }

        if (!this.isCorrectWeightValue(currentWeight)) {
            this.currentWeightView.setError(this.errorFieldNotNumber);
            focusView = this.currentWeightView;
            cancel = true;
        }

        if (!this.isCorrectWeightValue(goalWeight)) {
            this.goalWeightView.setError(this.errorFieldNotNumber);
            focusView = this.goalWeightView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            this.showProgress(true);
            this.mAuthTask = new UserSignUpTask(userName, password, currentWeight, goalWeight);
            this.mAuthTask.signUp(this);
        }
    }

    // 入力された体重が正しい値か判定
    private boolean isCorrectWeightValue(String input) {
        double value = -1;
        try {
            value = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            // 数字じゃなかったらだめ
            return false;
        }
        if (value < 0) {
            // マイナスもだめ
            return false;
        }
        return true;

    }

    private void showProgress(boolean show) {
        ViewUtil.showProgress(this, this.signUpFormView, this.progressView, show);
    }


    @Override
    public void done(NCMBException e) {
        if (e != null) {
            //error
            this.mAuthTask = null;
            this.showProgress(false);
            this.userNameView.setError(e.getMessage());
            this.passwordView.setError(e.getMessage());
            this.userNameView.requestFocus();
        } else {
            // success
            this.mAuthTask.saveData(new DoneCallback() {
                @Override
                public void done(NCMBException e) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    finish();
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserSignUpTask {

        private final String userName;
        private final String password;
        private final String currentWeight;
        private final String goalWeight;

        UserSignUpTask(String userName, String password, String currentWeight, String goalWeight) {
            this.userName = userName;
            this.password = password;
            this.currentWeight = currentWeight;
            this.goalWeight = goalWeight;
        }

        public void signUp(DoneCallback doneCallback) {
            NCMBUser user = new NCMBUser();
            user.setUserName(this.userName);
            user.setPassword(this.password);

            user.signUpInBackground(doneCallback);
        }

        public void saveData(DoneCallback doneCallback){
            NCMBUser user = NCMBUser.getCurrentUser();
            user.put(startWeightKey,this.currentWeight);
            user.put(goalWeightKey,this.goalWeight);
            user.saveInBackground(doneCallback);

//            NCMBObject openUserDataRecord = new NCMBObject(openUserData);
//            openUserDataRecord.put(userNameKey,this.userName);
//            openUserDataRecord.put(offsetWeightKey,);
        }

    }
}

