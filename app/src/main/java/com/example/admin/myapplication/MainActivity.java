package com.example.admin.myapplication;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

import corp.wmsoft.android.lib.wmavatarview.AndroidHelper;
import corp.wmsoft.android.lib.wmavatarview.IWMAvatarStatus;
import corp.wmsoft.android.lib.wmavatarview.WMAvatarView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final WMAvatarView toxAvatarView = (WMAvatarView) findViewById(R.id.toxAvatar);
        final WMAvatarView toolbarAvatar = (WMAvatarView) findViewById(R.id.toolbarAvatar);
        final WMAvatarView toxAvatarMedium = (WMAvatarView) findViewById(R.id.toxAvatarMedium);

        findViewById(R.id.btnOffline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.OFFLINE);
                toolbarAvatar.setStatus(IWMAvatarStatus.OFFLINE);
                toxAvatarMedium.setStatus(IWMAvatarStatus.OFFLINE);
            }
        });

        findViewById(R.id.btnOnline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.ONLINE);
                toolbarAvatar.setStatus(IWMAvatarStatus.ONLINE);
                toxAvatarMedium.setStatus(IWMAvatarStatus.ONLINE);
            }
        });

        findViewById(R.id.btnAway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.AWAY);
                toolbarAvatar.setStatus(IWMAvatarStatus.AWAY);
                toxAvatarMedium.setStatus(IWMAvatarStatus.AWAY);
            }
        });

        findViewById(R.id.btnBusy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.BUSY);
                toolbarAvatar.setStatus(IWMAvatarStatus.BUSY);
                toxAvatarMedium.setStatus(IWMAvatarStatus.BUSY);
            }
        });

        /**
         *
         */
        findViewById(R.id.btnSetEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("");
                toolbarAvatar.setText("");
                toxAvatarMedium.setText("");
            }
        });
        findViewById(R.id.btnSetNull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText(null);
                toolbarAvatar.setText(null);
                toxAvatarMedium.setText(null);
            }
        });
        findViewById(R.id.btnWWW).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("WWW");
                toolbarAvatar.setText("WWW");
                toxAvatarMedium.setText("WWW");
            }
        });
        findViewById(R.id.btnA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("a");
                toolbarAvatar.setText("a");
                toxAvatarMedium.setText("a");
            }
        });

        /**
         *
         */
        findViewById(R.id.btnSetResImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setImageDrawable(AndroidHelper.getVectorDrawable(MainActivity.this, R.drawable.ic_android_black_24dp));
                toxAvatarMedium.setImageResource(R.drawable.default_avatar);
            }
        });

        findViewById(R.id.btnSetExtImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "1.png"));
                toxAvatarMedium.setImageBitmap(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "2.png"));
            }
        });

        findViewById(R.id.btnClearImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setImageDrawable(null);
                toxAvatarMedium.setImageDrawable(null);
            }
        });

    }
}