package com.example.admin.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }
}