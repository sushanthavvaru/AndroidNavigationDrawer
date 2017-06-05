package com.example.sushanth.identifyuser;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.sushanth.identifyuser.MainActivity.CountryReceived;
import static com.example.sushanth.identifyuser.MainActivity.StateReceived;
import static com.example.sushanth.identifyuser.MainActivity.closeActivity;
import static com.example.sushanth.identifyuser.MainActivity.xlatitude;
import static com.example.sushanth.identifyuser.MainActivity.xlongitude;


/**
 * A simple {@link Fragment} subclass.
 */
public class postUserFragment extends Fragment
    {



    ArrayList<Integer> year = new ArrayList<Integer>();
    Spinner spin;
    Button submit, country, location;
    TextView password, nickname, displayCountry, displayState, city;
    Double platitude, plongitude;
    int startYear = 1970;
    int presentYear = 2017;
    String selectedYear;
    Boolean validData;


    public postUserFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_post_user, container, false);
        spin = (Spinner) v.findViewById(R.id.spinner);
        submit = (Button) v.findViewById(R.id.submit);
        password = (TextView) v.findViewById(R.id.password);
        nickname = (TextView) v.findViewById(R.id.nickname);
        city = (TextView) v.findViewById(R.id.city);
        displayCountry = (TextView) v.findViewById(R.id.displayCountry);
        displayState = (TextView) v.findViewById(R.id.displayState);
        country = (Button) v.findViewById(R.id.country);
        year.clear();

        location = (Button) v.findViewById(R.id.location);
        for (int i = startYear; i <= presentYear; i++) {
            year.add(i);
        }
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this.getActivity(),
                android.R.layout.simple_spinner_item, year);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedYear = parent.getItemAtPosition(0).toString();

            }
        });




        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        closeActivity=true;

        nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                nickname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(nickname.getText().toString().trim().equals("")){
                            nickname.setError("Cannot be Empty");
                        }else{validateNickName();}
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        String result = s.toString().replaceAll(" ", "");
                        if (!s.toString().equals(result)) {
                            nickname.setText(result);
                            Toast.makeText(getActivity(), "Spaces not Allowed", Toast.LENGTH_LONG ).show();
                            return;


                            // alert the user
                        }
                    }
                });

            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("rew", "Submit pressed");
                platitude = xlatitude;
                plongitude = xlongitude;

                if(nickname.getText().toString().trim().equals("")){
                    validData = false;
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_LONG ).show();
                    return;

                }
                else if(true)
                {
                    validateNickName();
                    if(validData == true){}
                    else{
                        Toast.makeText(getActivity(), "Enter valid Name", Toast.LENGTH_LONG ).show();
                        return;
                    }
                }
                else{validData=true;}


                if(password.getText().length()<3){
                    validData = false;
                    Log.i("rew", ""+validData );
                    Toast.makeText(getActivity(), "Password length should be atleast 3 characters", Toast.LENGTH_LONG ).show();
                    return;
                }
                else{
                    validData = true;

                }


                if(CountryReceived == null && StateReceived == null){
                    validData = false;
                    Toast.makeText(getActivity(), "Enter Location please", Toast.LENGTH_LONG ).show();
                    return;
                }
                else{
                    validData = true;
                }



                if(city.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(), "Enter City", Toast.LENGTH_LONG ).show();
                    validData = false;
                    return;
                }
                else{
                    validData = true;
                }

                if(xlongitude ==0.0 && xlatitude == 0.0){
                    // do something or Geocoder
                   // Toast.makeText(getActivity(), ""+xlatitude+" " +xlongitude, Toast.LENGTH_LONG ).show();

                    Geocoder locator = new Geocoder(getActivity());
                    try {
                        List<Address> state = locator.getFromLocationName(CountryReceived+", "+StateReceived, 1);
                        for (Address stateLocation: state) {
                            if (stateLocation.hasLatitude())
                                xlatitude = stateLocation.getLatitude();
                            if (stateLocation.hasLongitude())
                                xlongitude = stateLocation.getLongitude();
                        }
                    } catch (Exception error) {
                        Log.e("rew", "Address lookup Error", error);
                    }
                    validData =true;

                }
                else{
                    Log.i("rew", ""+ xlatitude + xlongitude);
                    //Toast.makeText(getActivity(), ""+xlatitude+  " " +xlongitude, Toast.LENGTH_LONG ).show();
                    validData = true;
                }
                if (validData == true){
                    //call volley and post
                    //Toast.makeText(getActivity(), "Everything went good..." +xlongitude+"  " + xlatitude , Toast.LENGTH_LONG ).show();
                    submitDataToServer();
                }
                else{
                    //Something went wrong
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG ).show();

                }





            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragments = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
                Countryfragment fragment = new Countryfragment();;
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("start1");
                fragmentTransaction.commit();



            }
        });


        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.FragmentManager fragments = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragments.beginTransaction();
                SetLocation fragment = new SetLocation();;
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack("start1");
                fragmentTransaction.commit();

            }
        });

    if(CountryReceived !=null && StateReceived != null) {
        displayCountry.setText("Country:" + CountryReceived);
        displayState.setText("State:" + StateReceived);
    }

    }

    public void validateNickName(){
        Response.Listener<String> success = new Response.Listener<String>() {

            public void onResponse(String response) {
                Log.i("rew", "validateNickName called");

                if(response.equalsIgnoreCase("true")){
                    //show error and do not allow to submit
                    nickname.setError("Nickname already exists");

                    validData=false;
                    submit.setEnabled(false);
                }
                else{

                    submit.setEnabled(true);
                    validData = true;
                }
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                Log.d("rew", error.toString());
            }
        };
        String url ="http://bismarck.sdsu.edu/hometown/nicknameexists?name="+nickname.getText();
        Log.i("rew", ""+url);
        StringRequest getRequest = new StringRequest(Request.Method.GET, url, success, failure);
        VolleyQueue.instance(getActivity()).add(getRequest);

    }
        public void submitDataToServer(){
            JSONObject data = new JSONObject();
            try {
                //data.put("latitude", 10.34);
                data.put("nickname",nickname.getText());
                data.put("password", password.getText());
                data.put("country",CountryReceived);
                data.put("state", StateReceived);
                data.put("city", city.getText());
                data.put("year", Integer.parseInt(selectedYear));
                data.put("latitude", xlatitude);
                data.put("longitude", xlongitude);

            } catch (JSONException error) {
                Log.e("rew", "JSON eorror", error);
                return;
            }
            Response.Listener<JSONObject> success = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    Toast.makeText(getActivity(), "Succesfully Posted", Toast.LENGTH_LONG ).show();

                    //Clear the page
                    nickname.setText("");
                    password.setText("");
                    displayCountry.setText(" Display Country");
                    displayState.setText("Display State");
                    city.setText("");
                    selectedYear="1970";
                    xlatitude=0.0;
                    xlatitude=0.0;

                    Log.i("rew", response.toString());
                }
            };
            Response.ErrorListener failure = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "unable to Post", Toast.LENGTH_LONG ).show();
                    Log.i("rew", "post fail " + new String(error.networkResponse.data));
                }
            };
            String url = "http://bismarck.sdsu.edu/hometown/adduser";
            JsonObjectRequest postRequest = new JsonObjectRequest(url, data, success, failure);
            VolleyQueue.instance(getActivity()).add(postRequest);
        }



}

