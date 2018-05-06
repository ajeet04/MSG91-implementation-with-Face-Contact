package com.example.ajeet.fakecontact;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Model> listData;
    private Context context;
    private View mView;
    public  TextView name1,number1,time;
    public  int from;
    public MyAdapter(Context context, List<Model> listData, int from) {
        this.listData=listData;
        this.context=context;
        this.from=from;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder( MyViewHolder viewHolder, int i) {
        final String mName;
        String mNumber=null;
        final String OTP;
       Model datalist= listData.get(i);
       // adapter using for contact list....
       if(from==0){
        mName=datalist.getFirst_name()+" "+datalist.getLast_name();
        mNumber=datalist.getNumber();
           name1.setText(mName);
           number1.setText(mNumber);
       }
       // using for Message list......
       else{
           mName=datalist.getName();
          long cTime=datalist.getTime();
           OTP=datalist.getOTP();
           name1.setText(mName);
           number1.setText(getDate(cTime, "dd/MM/yyyy hh:mm:ss"));
           time.setText("OTP is : "+OTP);


       }

//viewHolder.itemView.setOnClickListener(new );
        // If Contact list calling this Adapter............. switch Activity Main to Contact info...............
        if(from==0) {
            final String finalMNumber = mNumber;
            viewHolder.View1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent startInfoActivity = new Intent(context, ContactInfo.class);
                    startInfoActivity.putExtra("name", mName);
                    startInfoActivity.putExtra("number", finalMNumber);
                    context.startActivity(startInfoActivity);
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View View1=null;
        public MyViewHolder(View itemView) {
            super(itemView);
            View1=itemView;
            name1 = (TextView) itemView.findViewById(R.id.item_name);
            time = (TextView) itemView.findViewById(R.id.time);
            number1 = (TextView) itemView.findViewById(R.id.item_number);
            if(from==1){
                time.setVisibility(View.VISIBLE);
            }
            else{
                time.setVisibility(View.GONE);
            }


        }
    }
    // millisecond to date format....................

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
