package com.rileyborgard.game;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.rileyborgard.game.Main;

public class AndroidLauncher extends AndroidApplication {

	private static final String TAG = "AndroidLauncher";
	protected AdView adView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		adView = new AdView(this);
		adView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				Log.i(TAG, "Ad Loaded...");
			}
		});
		adView.setAdSize(AdSize.SMART_BANNER);
        adView.setId(R.id.adViewId);
		adView.setAdUnitId("ca-app-pub-5309122724767561/6881657029");

		AdRequest.Builder builder = new AdRequest.Builder();
		builder.addTestDevice("981771D30A338CE7A4097BC8F19050AE");
        builder.addTestDevice("547E3B851146E29CDCCBC19CFDFDEFB3");
		RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT
		);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        RelativeLayout layout = new RelativeLayout(this);
		layout.addView(adView, adParams);
		adView.loadAd(builder.build());

        RelativeLayout.LayoutParams gameViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        gameViewParams.addRule(RelativeLayout.ABOVE, adView.getId());
//        gameViewParams.bottomMargin = 150;

        View gameView = initializeForView(new Main(), config);
        layout.addView(gameView, gameViewParams);

		setContentView(layout);
	}

}
