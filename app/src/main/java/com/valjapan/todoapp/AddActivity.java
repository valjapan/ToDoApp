package com.valjapan.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();
    EditText titleEditText, contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        titleEditText = (EditText) findViewById(R.id.title);
        contentEditText = (EditText) findViewById(R.id.content);
    }

    public void save(View v) {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String key = reference.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

//    引数のToDoDataの内容をデータベースに送る。
        ToDoData toDoData = new ToDoData(key, title, content);

        reference.child("users").child(uid).child(key).setValue(toDoData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                finish();
            }
        });
    }
}
