package com.valjapan.todoapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText emailFormEditText, passwordFormEditText;
    static final String KEY_EMAIL = "key_email";
    static final String KEY_PASSWORD = "key_password";
    static final String KEY_HAS_ACCOUNT = "key_has_account";
    public Intent data;
    public FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailFormEditText = (EditText) findViewById(R.id.email_log_in_edit_text);
        passwordFormEditText = (EditText) findViewById(R.id.password_log_in_edit_text);

        mAuth = FirebaseAuth.getInstance();
    }

    public void checkEmpty() {
        if (!TextUtils.isEmpty(emailFormEditText.getText()) &&
                !TextUtils.isEmpty(passwordFormEditText.getText())) {
            data = new Intent();
            data.putExtra(KEY_EMAIL, emailFormEditText.getText().toString());
            data.putExtra(KEY_PASSWORD, passwordFormEditText.getText().toString());
        }
    }

    public void loginMailButton(View v) {
        signIn(emailFormEditText.getText().toString(), passwordFormEditText.getText().toString());
        data.putExtra(KEY_HAS_ACCOUNT, true);
        setResult(RESULT_OK, data);
    }

    public void addMailButton(View v) {
        createAccount(emailFormEditText.getText().toString(), passwordFormEditText.getText().toString());
        data.putExtra(KEY_HAS_ACCOUNT, false);
        setResult(RESULT_OK, data);
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        checkEmpty();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            changeActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        checkEmpty();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            changeActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(task.getException().getMessage())
                                    .setTitle("Error!")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }
                });
    }

    private void changeActivity() {
        Intent intent = new Intent(this, ToDoActivity.class);
        startActivity(intent);
        finish();
    }

}
