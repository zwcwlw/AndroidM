package com.zwcwlw.androidm;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import java.security.PrivateKey;

public class MainActivity extends Activity {
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //AndroidM获取联系人的用法
    public void getFirstContact(View view) {
        //1.如果是target23就需要检查权限
        int permission = ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_CONTACTS);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            showFirstContact(view.getContext());

        } else {
            //2.告诉用户开启权限
            Toast.makeText(MainActivity.this, "请开启用户权限", Toast.LENGTH_SHORT).show();
            //开启权限 第二个参数String[]=new String{}可以是多个参数
            String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);

        }
    }

    //3.sdk23的权限对话框消失之后会调用此方法--可知道获取权限的结果
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //判断用户是否真的允许
        if (requestCode != REQUEST_CODE) {
            //用户不允许
            return;
        }
        //多个权限获取显示Toast
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < permissions.length; i++) {
            sb.append(grantResults[i]).append("").append(permissions[i]).append("\n");
        }
        Toast.makeText(MainActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
        //用户允许
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showFirstContact(MainActivity.this);
        }
    }

    //4.获取联系人的方法
    private void showFirstContact(Context context) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            Toast.makeText(MainActivity.this, "名字:" + name, Toast.LENGTH_SHORT).show();
        }
    }
}
