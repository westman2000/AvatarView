package com.example.admin.myapplication;

import android.app.Instrumentation;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import corp.wmsoft.android.lib.wmavatarview.AndroidHelper;
import corp.wmsoft.android.lib.wmavatarview.IWMAvatarStatus;
import corp.wmsoft.android.lib.wmavatarview.WMAvatarView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wm::MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final WMAvatarView toxAvatarView = (WMAvatarView) findViewById(R.id.toxAvatar);
//        final WMAvatarView toolbarAvatar = (WMAvatarView) findViewById(R.id.toolbarAvatar);
//        final WMAvatarView toxAvatarMedium = (WMAvatarView) findViewById(R.id.toxAvatarMedium);

        findViewById(R.id.btnOffline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.OFFLINE);
//                toolbarAvatar.setStatus(IWMAvatarStatus.OFFLINE);
//                toxAvatarMedium.setStatus(IWMAvatarStatus.OFFLINE);
            }
        });

        findViewById(R.id.btnOnline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.ONLINE);
//                toolbarAvatar.setStatus(IWMAvatarStatus.ONLINE);
//                toxAvatarMedium.setStatus(IWMAvatarStatus.ONLINE);
            }
        });

        findViewById(R.id.btnAway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.AWAY);
//                toolbarAvatar.setStatus(IWMAvatarStatus.AWAY);
//                toxAvatarMedium.setStatus(IWMAvatarStatus.AWAY);
            }
        });

        findViewById(R.id.btnBusy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.BUSY);
//                toolbarAvatar.setStatus(IWMAvatarStatus.BUSY);
//                toxAvatarMedium.setStatus(IWMAvatarStatus.BUSY);
            }
        });

        /**
         *
         */
        findViewById(R.id.btnSetEmpty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("");
//                toolbarAvatar.setText("");
//                toxAvatarMedium.setText("");
            }
        });
        findViewById(R.id.btnSetNull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText(null);
//                toolbarAvatar.setText(null);
//                toxAvatarMedium.setText(null);
            }
        });
        findViewById(R.id.btnWWW).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("WWW");
//                toolbarAvatar.setText("WWW");
//                toxAvatarMedium.setText("WWW");
            }
        });
        findViewById(R.id.btnA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("a");
//                toolbarAvatar.setText("a");
//                toxAvatarMedium.setText("a");
            }
        });

        /**
         * 5692031122
         */
        findViewById(R.id.btnSetResImg).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setImageDrawable(AndroidHelper.getVectorDrawable(MainActivity.this, R.drawable.ic_android_black_24dp));
//                Picasso.with(view.getContext()).load(R.drawable.default_avatar).into(toxAvatarMedium);
            }
        });

        findViewById(R.id.btnSetExtImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick("+toxAvatarView.getWidth()+", "+toxAvatarView.getHeight()+")");

                toxAvatarView.setImageBitmap(AndroidHelper.decodeSampledBitmapFromFile(Environment.getExternalStorageDirectory() + File.separator + "1.jpg", toxAvatarView.getWidth(), toxAvatarView.getHeight()));
//                Picasso.with(view.getContext()).load(new File(Environment.getExternalStorageDirectory() + File.separator + "1.jpg")).into(toxAvatarView);
//                Picasso.with(view.getContext()).load(new File(Environment.getExternalStorageDirectory() + File.separator + "2.jpg")).resize(512, 512).centerCrop().into(toxAvatarMedium);

            }
        });

        findViewById(R.id.btnClearImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setImageDrawable(null);
//                toxAvatarMedium.setImageDrawable(null);
            }
        });

    }
}