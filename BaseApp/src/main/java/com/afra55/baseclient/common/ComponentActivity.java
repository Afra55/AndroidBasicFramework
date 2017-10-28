package com.afra55.baseclient.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.afra55.baseclient.R;
import com.afra55.baseclient.ui.fragment.ShopFragment;
import com.afra55.commontutils.activity.ActivityUtils;

/**
 * Created by Afra55 on 2017/10/28.
 * Smile is the best name card.
 */

public class ComponentActivity extends AppActivity {

    public static final String FRAGMENT_ID = "FRAGMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base);
        AppFragment appFragment = getFragment();
        if (appFragment != null) {
            ActivityUtils.replaceFragmentContent(getSupportFragmentManager(), appFragment);
        }
    }

    @Override
    protected String getScreenTitle() {
        return null;
    }

    private AppFragment getFragment() {
        Intent intent = getIntent();
        if (intent == null) {
            return null;
        }
        String stringExtra = intent.getStringExtra(FRAGMENT_ID);
        if (TextUtils.isEmpty(stringExtra)) {
            return null;
        }
        AppFragment appFragment = null;

        // 在这里添加不同 fragment
        switch (stringExtra) {
            case "xx":
                appFragment = ShopFragment.newInstance(null, null);
                break;
        }
        if (appFragment != null) {
            appFragment.setContainerId(R.id.base_container);
            if (getIntent().getExtras() != null) {
                appFragment.setArguments(getIntent().getExtras());
            }
        }
        return appFragment;

    }
}
