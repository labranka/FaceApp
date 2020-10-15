package com.example.faceapp;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import edmt.dev.edmtdevcognitivevision.Contract.AnalysisResult;
import edmt.dev.edmtdevcognitivevision.Contract.Caption;
import edmt.dev.edmtdevcognitivevision.Rest.VisionServiceException;
import edmt.dev.edmtdevcognitivevision.VisionServiceClient;
import edmt.dev.edmtdevcognitivevision.VisionServiceRestClient;

public class AnalizaSlike extends AppCompatActivity {

    ImageView imgSlika1;
    Button btnAnaliza;
    TextView txtRezultat;

    private final String API_KEY ="7817d58161f14c0e9bef8126e3dd54ff";
    private final String API_LINK= "https://branka.cognitiveservices.azure.com/vision/v1.0";

    public VisionServiceClient visionServiceClient = new VisionServiceRestClient(API_KEY,API_LINK);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analizaslike);

        imgSlika1 = (ImageView)findViewById(R.id.imgSlika1);
        btnAnaliza =(Button)findViewById(R.id.btnAnaliza);


        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.smile);
        imgSlika1.setImageBitmap(bitmap);



        btnAnaliza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Konvertovanje Bitmap u BitArray
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                final ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

                AsyncTask<InputStream, String, String> visionTask = new AsyncTask<InputStream, String, String>() {
                   ProgressDialog mDialog = new ProgressDialog(AnalizaSlike.this);

                    @Override
                    protected void onPreExecute() {
                        mDialog.show();
                    }

                    @Override
                    protected String doInBackground(InputStream... inputStreams) {
                        try {
                            publishProgress("Prepoznavanje...");
                            String[] features = {"Opis"};
                            String[] details = {};

                            AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);


                            String jsonResult = new Gson().toJson(result);
                            return jsonResult;

                        } catch (Exception e) {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(String s) {
                            mDialog.dismiss();

                            AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                            txtRezultat=(TextView)findViewById(R.id.txtRezultat);


                            StringBuilder result_Text = new StringBuilder();
                            for (Caption caption:result.description.captions){
                                result_Text.append(caption.text);
                            }

                            txtRezultat.setText(result_Text);

                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        mDialog.setMessage(values[0]);
                    }
                };

                visionTask.execute(inputStream);
            }
        });

    }
}
