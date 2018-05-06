package com.example.ajeet.fakecontact;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Messages.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Messages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Messages extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final int from = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mView;
    private RecyclerView MessagesList;
    private LinearLayoutManager mLinearLayaoutManager;
    private MyAdapter adapter;
    private List<Model> listMessage=new ArrayList<Model>();
    File path = new File(android.os.Environment.getExternalStorageDirectory(), "Message.json");
    private FileOpreation fileOpreation=new FileOpreation(getContext(),path);

    private OnFragmentInteractionListener mListener;

    public Messages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Messages.
     */
    // TODO: Rename and change types and number of parameters
    public static Messages newInstance(String param1, String param2) {
        Messages fragment = new Messages();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
          //  mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // creating Rercylerview and layoutManager....................
        mView= inflater.inflate(R.layout.fragment_messages, container, false);
        MessagesList=(RecyclerView)mView.findViewById(R.id.messages_list);
        mLinearLayaoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);



        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadMessage();
        adapter=new MyAdapter(getContext(),listMessage,from);
        MessagesList.setLayoutManager(mLinearLayaoutManager);
        MessagesList.setAdapter(adapter);
    }
// adding OTP in ArrayList from JSON file.......................
    private void loadMessage() {
        listMessage.clear();
        String jsonString=fileOpreation.read();

        Model data;
        if (jsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("messages");
                //Toast.makeText(getContext(), jsonArray.length() + "", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    data = new Model(object.getString("name"), Long.parseLong(object.getString("time")), object.getString("OTP"));
                    listMessage.add(data);
                    Collections.sort(listMessage, Model.stTime);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
