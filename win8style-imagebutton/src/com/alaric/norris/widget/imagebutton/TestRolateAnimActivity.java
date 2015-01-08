package com.alaric.norris.widget.imagebutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.alaric.norris.widget.imagebutton.R ;

public class TestRolateAnimActivity extends Activity
{
	Win8ImageButton joke;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		joke = (Win8ImageButton) findViewById(R.id.c_joke);
		joke.setOnClickIntent(new Win8ImageButton.OnViewClickListener()
		{

			@Override
			public void onViewClick(Win8ImageButton view)
			{
				Toast.makeText(TestRolateAnimActivity.this, "Joke", 1000).show();
			}
		});
	}
	
	
}