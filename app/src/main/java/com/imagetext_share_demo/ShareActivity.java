package com.imagetext_share_demo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import com.imagetext_share_demo.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ShareActivity extends AppCompatActivity implements OnClickListener {
	private static Button selectImage, shareImage, shareText;
	private static ImageView imageView;
	private static EditText textToShare;

	// Uri for image path
	private static Uri imageUri = null;

	private final int select_photo = 1; // request code fot gallery intent

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_activity);


		init();
		setListeners();

	}

	// Initialize views
	private void init() {
		selectImage = (Button) findViewById(R.id.select_image);
		shareImage = (Button) findViewById(R.id.share_image);
		shareText = (Button) findViewById(R.id.btnTextShare);

		imageView = (ImageView) findViewById(R.id.share_imageview);

		textToShare = (EditText) findViewById(R.id.text_share);
	}

	// Implement click listeners
	private void setListeners() {
		selectImage.setOnClickListener(this);
		shareImage.setOnClickListener(this);
		shareText.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.select_image:
			// Intent to gallery
			Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			//in.setType("image/*");
			if(in.resolveActivity(getPackageManager())!=null) {
				startActivityForResult(in, select_photo);// start
				// activity
				// for
				// result
			}

			break;
		case R.id.btnTextShare:

			// share image
			String text = textToShare.getText().toString();
			if (!text.equals("") && text.length() != 0)
				shareImage(imageUri,text);
			else
				Toast.makeText(ShareActivity.this,"Mention some text", Toast.LENGTH_SHORT).show();

			break;
		}
	}

	protected void onActivityResult(int requestcode, int resultcode,
			Intent imagereturnintent) {
		super.onActivityResult(requestcode, resultcode, imagereturnintent);
		switch (requestcode) {
		case select_photo:
			if (resultcode == RESULT_OK) {

				//imageUri = imagereturnintent.getData();// Get intent
														// data

				Bitmap bitmap = (Bitmap) imagereturnintent.getExtras().get("data");
										// deocde
										// uri
										// method

				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);// Set image over
					// bitmap
				} else {
					Toast.makeText(ShareActivity.this,
							"Error while decoding image.",
							Toast.LENGTH_SHORT).show();
				}

				ByteArrayOutputStream bytes = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
				String path = MediaStore.Images.Media.insertImage(ShareActivity.this.getContentResolver(), bitmap, "Title", null);
				imageUri = Uri.parse(path);
				// Check if bitmap is not null then set image else show
				// toast

			}
		}
	}

	// Share image
	private void shareImage(Uri imagePath , String text) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		sharingIntent.setType("image/*");
		sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
		sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
	}

	/*
	// Share text
	private void shareText(String text) {

		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");// Plain format text

		// You can add subject also
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		startActivity(Intent.createChooser(sharingIntent, "Share Text Using"));
	}*/
}
