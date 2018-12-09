package com.valjapan.todoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ToDoActivity extends AppCompatActivity implements ListView.OnItemLongClickListener {
    public FirebaseUser user;
    public String uid;

    public FirebaseAuth mAuth;

    public FirebaseDatabase database;
    public DatabaseReference reference;

    public CustomAdapter mCustomAdapter;
    public ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        //ログイン情報を取得
        user = FirebaseAuth.getInstance().getCurrentUser();

        //user id = Uid を取得する
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users").child(uid);

        mListView = (ListView) findViewById(R.id.list_view);

        //CustomAdapterをセット
        mCustomAdapter = new CustomAdapter(getApplicationContext(), R.layout.card_view, new ArrayList<ToDoData>());
        mListView.setAdapter(mCustomAdapter);

        //LongListenerを設定
        mListView.setOnItemLongClickListener(this);

        //firebaseと同期するリスナー
        reference.addChildEventListener(new ChildEventListener() {
//            データを読み込むときはイベントリスナーを登録して行う。
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                アイテムのリストを取得するか、アイテムのリストへの追加がないかリッスンします。
                ToDoData toDoData = dataSnapshot.getValue(ToDoData.class);
                mCustomAdapter.add(toDoData);
                mCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                リスト内のアイテムに対する変更がないかリッスンします。

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                リストから削除されるアイテムがないかリッスンします。
                Log.d("ToDoActivity", "onChildRemoved:" + dataSnapshot.getKey());
                ToDoData result = dataSnapshot.getValue(ToDoData.class);
                if (result == null) return;

                ToDoData item = mCustomAdapter.getToDoDataKey(result.getFirebaseKey());

                mCustomAdapter.remove(item);
                mCustomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                並べ替えリストの項目順に変更がないかリッスンします。
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                ログを記録するなどError時の処理を記載する。
            }
        });

    }

    public void addButton(View v) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final ToDoData toDoData = mCustomAdapter.getItem(position);
        uid = user.getUid();

        new AlertDialog.Builder(this)
                .setTitle("Done?")
                .setMessage("この項目を完了しましたか？")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // OK button pressed
                        reference.child(toDoData.getFirebaseKey()).removeValue();
//                        mCustomAdapter.remove(toDoData);
                    }
                })
                .setNegativeButton("No", null)
                .show();

        return false;
    }

    public void logout(View v) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        Intent intent = new Intent(ToDoActivity.this, LoginActivity.class);
        intent.putExtra("check", true);
        startActivity(intent);
        finish();
    }
}