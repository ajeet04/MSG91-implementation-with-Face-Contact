
package com.example.ajeet.fakecontact;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    File path = new File(android.os.Environment.getExternalStorageDirectory(), "contacts.json");
    FileOpreation fileOpreation =new FileOpreation(getContext(),path);

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int from = 0;
    private View mView;
    private RecyclerView contactList;
    private LinearLayoutManager mLinearLayaoutManager;
    private MyAdapter adapter;
    private List<Model> listData=new ArrayList<Model>();
    FloatingActionButton add_contact;
    private BottomSheetBehavior mBottomSheetBehavior;

    private OnFragmentInteractionListener mListener;

    public ContactList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactList.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactList newInstance(String param1, String param2) {
        ContactList fragment = new ContactList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       mView= inflater.inflate(R.layout.fragment_contact_list, container, false);
       contactList=(RecyclerView)mView.findViewById(R.id.contact_list);
       mLinearLayaoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
       loadData();
       adapter=new MyAdapter(getContext(),listData,from);
       contactList.setLayoutManager(mLinearLayaoutManager);
       contactList.setAdapter(adapter);



        final View bottomSheet = mView.findViewById( R.id.bottom_sheet );

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior.setPeekHeight(0);
        add_contact = (FloatingActionButton) mView.findViewById(R.id.add_contact_button);
        add_contact = (FloatingActionButton) mView.findViewById(R.id.add_contact_button);
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_contact.setVisibility(View.INVISIBLE);
                // expand bottom sheet
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                // bottom sheet field......
                final TextInputLayout firstName=(TextInputLayout)bottomSheet.findViewById(R.id.first_name);
                final TextInputLayout lastName=(TextInputLayout)bottomSheet.findViewById(R.id.last_name);
                final TextInputLayout number=(TextInputLayout)bottomSheet.findViewById(R.id.contact_number);
                final ImageButton cancel_button=(ImageButton)bottomSheet.findViewById(R.id.cancel_bottomseet);
                Button saveContact=(Button)bottomSheet.findViewById(R.id.save_contact);
                cancel_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        mBottomSheetBehavior.setPeekHeight(0);
                        add_contact.setVisibility(View.VISIBLE);


                    }
                });


                saveContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Permissions()) {    //..check permission for external storage......
                            String first_name = firstName.getEditText().getText().toString().trim();
                            String last_name = lastName.getEditText().getText().toString().trim();
                            String contact_number = number.getEditText().getText().toString().trim();
                            // .............if field arnt empty................ write json file.............
                            if (!first_name.isEmpty() && !last_name.isEmpty() && !contact_number.isEmpty()) {
                                boolean writeJson = false;
                                writeJson = createAndSaveJSONFile(first_name, last_name, contact_number);

                                if (writeJson) {
                                    Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    mBottomSheetBehavior.setPeekHeight(0);
                                    add_contact.setVisibility(View.VISIBLE);
                                    loadData(); // load data in Arraylist from json file...................
                                    adapter = new MyAdapter(getContext(), listData, from);
                                    contactList.setLayoutManager(mLinearLayaoutManager);
                                    contactList.setAdapter(adapter);
                                    firstName.getEditText().setText("");
                                    lastName.getEditText().setText("");
                                    number.getEditText().setText("");

                                } else {
                                    // collapse dailog..........................
                                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                    mBottomSheetBehavior.setPeekHeight(0);

                                    add_contact.setVisibility(View.VISIBLE);

                                }


                            }
                            // set error in field.............
                            else if (first_name.isEmpty()) {
                                firstName.setError("Field can not be empty!");
                            } else if (last_name.isEmpty()) {
                                lastName.setError("Field can not be empty!");

                            } else if (contact_number.isEmpty()) {
                                number.setError("Number must be 10 char");

                            }

                        }
                    }
                });





            }
        });
       return mView;
    }

    private void loadData() {
        listData.clear();
        String jsonString=fileOpreation.read();

        Model data;
        if (jsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                //Toast.makeText(getContext(), jsonArray.length() + "", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    data = new Model(object.getString("first_name"), object.getString("last_name"), object.getString("number"),"ab");
                    listData.add(data);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    private boolean createAndSaveJSONFile(String first_name, String last_name, String number1){
            if (fileOpreation.isFileExist()) {  // check json file exist.......
                return fileOpreation.write(first_name, last_name, number1, 0);


            } else {// if json file not exist ....
                return fileOpreation.create(first_name, last_name, number1, 0);

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


    // storage permision..............


    private boolean Permissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setCancelable(true);
                //alertBuilder.setTitle("Permission necessary");
                alertBuilder.setMessage("phone stat is necessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            return false;
        } else {
            return true;
        }


    }

}


