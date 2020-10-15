package com.example.faceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OpisSlike extends AppCompatActivity {
    ImageView imgSlika1;
    Button btnAnaliza;
    TextView txtRezultat;

    private final String API_KEY ="25513f8eefcb4906ae383989e666208c";
    private final String API_LINK= "https://labranja.cognitiveservices.azure.com/vision/v1.0";

    public VisionServiceClient visionServiceClient = new VisionServiceRestClient(API_KEY,API_LINK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opis_slike);

        imgSlika1 = (ImageView)findViewById(R.id.imgSlika1);
        btnAnaliza =(Button)findViewById(R.id.btnAnaliza);
        txtRezultat=(TextView)findViewById(R.id.txtRezultat);


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
                    ProgressDialog progressDialog = new ProgressDialog(OpisSlike.this);

                    @Override
                    protected void onPreExecute() {
                        progressDialog.show();
                    }

                    @Override
                    protected String doInBackground(InputStream... inputStreams) {
                        try {
                            publishProgress("Prepoznavanje...");
                            String[] features = {"Opis"};//get opis from API
                            String[] details = {};

                            AnalysisResult result = visionServiceClient.analyzeImage(inputStreams[0], features, details);


                            String jsonResult = new Gson().toJson(result);
                            return jsonResult;

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch(VisionServiceException e){
                            e.printStackTrace();
                        }
                        return "";
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        if(TextUtils.isEmpty(s)){
                            Toast.makeText(OpisSlike.this, "API je vratio prazan rezultat", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();

                            AnalysisResult result = new Gson().fromJson(s, AnalysisResult.class);
                            StringBuilder result_Text = new StringBuilder();

                            for (Caption caption:result.description.captions) {
                                result_Text.append(caption.text);
                            }

                            txtRezultat.setText(result_Text.toString());
                        }
                    }

                    @Override
                    protected void onProgressUpdate(String... values) {
                        progressDialog.setMessage(values[0]);
                    }
                };

                visionTask.execute(inputStream);
            }
        });

    }
}
