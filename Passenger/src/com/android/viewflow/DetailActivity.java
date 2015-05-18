package com.android.viewflow;

import com.ky.passenger.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class DetailActivity extends Activity {

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.news_image_detail);
		imageView = (ImageView) findViewById(R.id.detail_image);

		Bundle extras = getIntent().getExtras();
		imageView.setImageResource(extras.getInt("image_id"));
	}

}
