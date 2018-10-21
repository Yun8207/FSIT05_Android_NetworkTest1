package tw.alex.networktest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private UIHandler uiHandler;
    private File sdroot;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,},
            123);
        }else{
                init();
            }


        sdroot = Environment.getExternalStorageDirectory();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        uiHandler = new UIHandler();
        img = findViewById(R.id.img);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED) {
            init();
        }else{
            finish();
        }
    }

    private void init(){
        sdroot = Environment.getExternalStorageDirectory();
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("Downloading....");
        uiHandler = new UIHandler();
        img = findViewById(R.id.img);
    }

    public void test1(View view) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.201.160:8080/JavaEE/Alex40.jsp");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) !=null){
                        Log.v("alex", line);
                    }

                    reader.close();
                    Log.v("alex","OK");

                } catch (Exception e) {
                    Log.v("alex", e.toString());
                }
            }
        }.start();

        Log.v("alex", "click");

    }

    public void test2(View view) {new Thread(){
        @Override
        public void run() {
            try {
                URL url = new URL("https://s.yimg.com/ny/api/res/1.2/ZOI8pFl14Zb9is7MbVGtXA--~A/YXBwaWQ9aGlnaGxhbmRlcjtzbT0xO3c9ODAw/http://media.zenfs.com/en/homerun/feed_manager_auto_publish_494/eafe942aecc61b66fd43f87cde530bd6");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());

                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable("bmp",bitmap);
                message.setData(bundle);

                uiHandler.sendMessage(message);



            } catch (Exception e) {
                Log.v("alex", e.toString());
            }
        }
    }.start();

        Log.v("alex", "click");

    }

    public void test3(View view) {new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line; StringBuffer sb = new StringBuffer();
                    while ((line = reader.readLine()) !=null){
                        sb.append(line);
                    }

                    reader.close();
                    parseJSON(sb.toString());

                   // Log.v("alex", sb.toString());



                } catch (Exception e) {
                    //Log.v("alex", e.toString());
                }
            }
        }.start();

        Log.v("alex", "click");

    }

    private void parseJSON(String json) {

        try{
            JSONArray root = new JSONArray(json);
            for(int i=0; i<root.length(); i++){
                JSONObject row = root.getJSONObject(i);
                String name = row.getString("Name");
                String tel = row.getString("ContactTel");
                Log.v("alex", name + ":" + tel);
            }

        }catch(Exception e){
            Log.v("alex", e.toString());
        }
    }

    public void test4(View view) {new Thread(){

        @Override
        public void run() {
            try {
                URL url = new URL("https://pdfur");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();

                BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
                FileOutputStream fout = new FileOutputStream(sdroot.getAbsolutePath() +
                        "/alex.pdf");

                byte[] buf = new byte[1024*1024]; int len = 0;
                while ((len = bin.read(buf)) != -1){
                    fout.write(buf, 0, len);

                }

                fout.flush();
                fout.close();
                bin.close();

            } catch (Exception e) {
                //Log.v("alex", e.toString());
            }
        }
    }.start();

        Log.v("alex", "click");
    }


    private class UIHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            if (msg.what == -7){
                progressDialog.dismiss();
            }else {
                Bitmap bitmap = (Bitmap) (msg.getData().getParcelable("bmp"));
                img.setImageBitmap(bitmap);
            }
        }
    }

}// use all these in service
