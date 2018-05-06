package com.example.ajeet.fakecontact;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class FileOpreation {
    JSONObject data = new JSONObject();
    JSONObject jsonObject = new JSONObject();
    File path;
    Context ctx;

    public FileOpreation(Context ctx, File path) {
        this.path = path;
        this.ctx = ctx;
    }

    public FileOpreation() {
    }

 // Creating json File and write........................................
    public boolean create(String first_name, String last_name, String number,int from) {
        try {
            if(from==0) { // method call from ContactlistFragment.................
                data.put("first_name", first_name);
                data.put("last_name", last_name);
                data.put("number", number);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(data);
                jsonObject.put("list", jsonArray);
            }
            if(from==1) {// call from ContactInfo.................
                data.put("name", first_name);
                data.put("time", last_name);
                data.put("OTP", number);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(data);
                jsonObject.put("messages", jsonArray);
            }
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonObject.toString());
            fileWriter.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }

    }
// Json file reading..................
    public String read() {
        String jsonStr = null;
        if (isFileExist()) {


            try {
                FileInputStream stream = new FileInputStream(path);
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                jsonStr = Charset.defaultCharset().decode(bb).toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }
        return jsonStr;
    }
    // checking file exist or not................................
    boolean isFileExist(){
        return path.exists();
    }


    // write json data in existing file..................................
    boolean write(String first_name, String last_name, String number,int from){
        String jsonStr=read();
        JSONObject jsonObject1=null;
        JSONArray jsonArray=null;
        try {
            if(from==0){// from contact list................
            data.put("first_name", first_name);
            data.put("last_name", last_name);
            data.put("number", number);
             jsonObject1=new JSONObject(jsonStr);
             jsonArray = jsonObject1.getJSONArray("list");}
            if(from==1){// from contact info...........................
                data.put("name", first_name);
                data.put("time", last_name);
                data.put("OTP", number);
                jsonObject1=new JSONObject(jsonStr);
              jsonArray = jsonObject1.getJSONArray("messages");



            }


            jsonArray.put(data);
            FileWriter fileWriter = new FileWriter(path);

            fileWriter.write(jsonObject1.toString());
            fileWriter.flush();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }



}






