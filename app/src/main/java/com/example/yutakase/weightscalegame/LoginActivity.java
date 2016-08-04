package com.example.yutakase.weightscalegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nifty.cloud.mb.core.LoginCallback;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBUser;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A login screen that offers login via user name/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginCallback {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    @Bind(R.id.user_name_text_view)
    AutoCompleteTextView userNameView;
    @Bind(R.id.password)
    EditText passwordView;
    @Bind(R.id.login_progress)
    View progressView;
    @Bind(R.id.login_form)
    View loginFormView;
    @Bind(R.id.email_sign_in_button)
    Button SignInButton;

    @BindString(R.string.error_field_required)
    String errorFieldRequired;
    @BindString(R.string.user_name_key)
    String userNameKey;
    @BindString(R.string.password_key)
    String passwordKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // Set up the login form.
        this.passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin(SignInButton);
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    @OnClick({R.id.email_sign_in_button, R.id.email_sign_up_button})
    void attemptLogin(View view) {
        if (this.mAuthTask != null) {
            return;
        }

        // Reset errors.
        this.userNameView.setError(null);
        this.passwordView.setError(null);

        // Store values at the time of the login attempt.
        String userName = this.userNameView.getText().toString();
        String password = this.passwordView.getText().toString();

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

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (view.getId() == R.id.email_sign_up_button) {
            Intent intent = new Intent(this, InputUserDataActivity.class);
            intent.putExtra(this.userNameKey, userName);
            intent.putExtra(this.passwordKey, password);
            startActivity(intent);
        } else {

            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            this.showProgress(true);
            this.mAuthTask = new UserLoginTask(userName, password);
            this.mAuthTask.execute(this);
        }
    }

    private void showProgress(boolean show) {
        ViewUtil.showProgress(this, this.loginFormView, this.progressView, show);
    }


    @Override
    public void done(NCMBUser ncmbUser, NCMBException e) {
        if (e != null) {
            //error
            this.mAuthTask = null;
            this.showProgress(false);
            this.userNameView.setError(e.getMessage());
            this.passwordView.setError(e.getMessage());
            this.userNameView.requestFocus();
        } else {
            // success
            Intent intent = new Intent(this, HomeActivity.class);
            finish();
            startActivity(intent);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask {

        private final String userName;
        private final String password;

        UserLoginTask(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public void execute(LoginCallback loginCallback) {
            try {
                NCMBUser.loginInBackground(this.userName, this.password, loginCallback);
            } catch (NCMBException e) {
                e.printStackTrace();
            }
        }
    }
}

