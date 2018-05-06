package com.example.ajeet.fakecontact;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class ContactInfo extends AppCompatActivity {
    private TextView user_name,user_number;
    private Button sendMessage;
    String name,number;
    private EditText messages;
    private Button send;
    private ImageButton cancel_bottomsheet;
    int n;
    private ProgressDialog mProgressDailog;
    private BottomSheetBehavior mBottomSheetBehavior;
    public static final String ACCOUNT_SID = "ACc1e8892151fbeaed7e9a99a51e066946";
    public static final String AUTH_TOKEN = "Your Auth key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        //..............tool bar...............
        getSupportActionBar().setTitle("Contact Info");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getting from MainActivity....................
        name=getIntent().getStringExtra("name");
        number=getIntent().getStringExtra("number");
        // intialize progress dailog................
        mProgressDailog=new ProgressDialog(ContactInfo.this);
        mProgressDailog.setCanceledOnTouchOutside(false);
        mProgressDailog.setMessage("Please wait...");
       // Android field............................
        user_name=(TextView)findViewById(R.id.contact_name1);
        user_number=(TextView)findViewById(R.id.c_number1);
        sendMessage=(Button)findViewById(R.id.send_message);
       cancel_bottomsheet=(ImageButton)findViewById(R.id.cancel_bottomseet1);
       // when we click on cancel button of Bottom sheet..................
       cancel_bottomsheet.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
               mBottomSheetBehavior.setPeekHeight(0);
           }
       });
         // bottomsheet view.....................
        final View bottomSheet = findViewById( R.id.bottom_sheet1);

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);

        user_number.setText(number);
        user_name.setText(name);

 // Action on send Message button..................................
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // Genrate random number ............................
                 Random rnd = new Random();
                 n = 100000 + rnd.nextInt(900000);
                 messages=(EditText) bottomSheet.findViewById(R.id.messages);
                 final String mMessage="Hi, Your OTP is : "+n;
                 messages.setText(mMessage);
                 messages.setEnabled(false);
                 send=(Button)bottomSheet.findViewById(R.id.send);
                 send.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         mProgressDailog.show();
                         // creating url for Msg ............ i used MSG91........ you can replace own Api key ...here

                        String Url=Url="http://api.msg91.com/api/sendhttp.php?sender=KISSAN&route=4&mobiles="+number+"&authkey="+AUTH_TOKEN+"&country=91&message="+mMessage;;
                        new FetchApi().execute(Url);



                          //  Toast.makeText(ContactInfo.this, message.getSid(), Toast.LENGTH_LONG).show();




                     }
                 });


            }
        });


    }
    // action bar button........
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



// Creating backgroung task for Api hit.........
    private class FetchApi extends AsyncTask<String, String, String> {
private HttpURLConnection connection;
private BufferedReader reader;
        File path = new File(android.os.Environment.getExternalStorageDirectory(), "Message.json");
FileOpreation fileOpreation=new FileOpreation(ContactInfo.this,path);

        @Override
        protected String doInBackground(String... params) {
            try {
                URL myUrl = new URL(params[0]);
                connection = (HttpURLConnection) myUrl.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                Log.d("ajeet", buffer.toString());
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                mProgressDailog.dismiss();
                //  dialog.hide();
            } catch (IOException e) {
                e.printStackTrace();
                //dialog.hide();
            } finally {
                if (connection != null) {
                    connection.disconnect();

                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (EOFException e){
                    Toast.makeText(ContactInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDailog.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ContactInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDailog.dismiss();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                super.onPostExecute(s);
                Toast.makeText(ContactInfo.this, "message sent successfully ", Toast.LENGTH_SHORT).show();
                String jsonString=fileOpreation.read();
                boolean writeJson=false;
                // if Message sent successfully data store in Json file..........................

                if(fileOpreation.isFileExist()){
                    writeJson=fileOpreation.write(name, System.currentTimeMillis()+"",n+"",1);


                } else {
                    writeJson=fileOpreation.create(name, System.currentTimeMillis()+"",n+"",1);

                }



                if(writeJson){
                    sendMessage.setEnabled(false);
                    // close dailog sheet.... and progress..............
                    mProgressDailog.dismiss();
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheetBehavior.setPeekHeight(0);

                }
                else{
                    mProgressDailog.dismiss();
                    Toast.makeText(ContactInfo.this, "file not created", Toast.LENGTH_SHORT).show();
                }




            }
        }

    }

}
