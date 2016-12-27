package com.example.admin.myapplication;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.io.File;

import corp.wmsoft.android.lib.wmavatarview.IWMAvatarStatus;
import corp.wmsoft.android.lib.wmavatarview.IWMAvatarStatusChangedListener;
import corp.wmsoft.android.lib.wmavatarview.WMAvatarView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "wm::MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final WMAvatarView toxAvatarView = (WMAvatarView) findViewById(R.id.toxAvatar);
        final WMAvatarView toolbarAvatar = (WMAvatarView) findViewById(R.id.toolbarAvatar);

        toxAvatarView.setStatusChangedListener(new IWMAvatarStatusChangedListener() {
            @Override
            public void onWMAvatarStatusChanged(@IWMAvatarStatus int newStatus) {
                Log.d(TAG, "onWMAvatarStatusChanged  <-- (" + newStatus + ") -->");
                toxAvatarView.setStatus(newStatus);
            }
        });

        toxAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "toxAvatarView ==== onClick ====");
            }
        });

        findViewById(R.id.btnOffline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.OFFLINE);
                toolbarAvatar.setStatus(IWMAvatarStatus.OFFLINE);
            }
        });

        findViewById(R.id.btnOnline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.ONLINE);
                toolbarAvatar.setStatus(IWMAvatarStatus.ONLINE);
            }
        });

        findViewById(R.id.btnAway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.AWAY);
                toolbarAvatar.setStatus(IWMAvatarStatus.AWAY);
            }
        });

        findViewById(R.id.btnBusy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setStatus(IWMAvatarStatus.BUSY);
                toolbarAvatar.setStatus(IWMAvatarStatus.BUSY);
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
            }
        });
        findViewById(R.id.btnSetNull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText(null);
                toolbarAvatar.setText(null);
            }
        });
        findViewById(R.id.btnWWW).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("WWW");
                toolbarAvatar.setText("WWW");
            }
        });
        findViewById(R.id.btnA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setText("a");
                toolbarAvatar.setText("a");
            }
        });

        /**
         * 5692031122
         */
        findViewById(R.id.btnSetResImg).setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso picasso = Picasso.with(view.getContext());
                picasso.setLoggingEnabled(true);

                picasso.load(R.drawable.default_avatar).fit().into(toxAvatarView);
                picasso.load(R.drawable.default_avatar).fit().into(toolbarAvatar);
            }
        });

        findViewById(R.id.btnSetExtImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso picasso = Picasso.with(view.getContext());
                picasso.setLoggingEnabled(true);

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "1.jpg");

                picasso.invalidate(file);

                picasso
                        .load(file)
                        .resize(toxAvatarView.getWidth(), toxAvatarView.getHeight())
                        .into(toxAvatarView);
                picasso.load(file).resize(toolbarAvatar.getWidth(), toolbarAvatar.getHeight()).into(toolbarAvatar);
            }
        });

        findViewById(R.id.btnClearImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toxAvatarView.setImageDrawable(null);
                toolbarAvatar.setImageDrawable(null);
            }
        });

    }
}