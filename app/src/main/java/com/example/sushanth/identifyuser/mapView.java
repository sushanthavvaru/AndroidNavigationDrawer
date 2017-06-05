package com.example.sushanth.identifyuser;


import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sushanth.identifyuser.MainActivity.closeActivity;
import static com.example.sushanth.identifyuser.MainActivity.drawer;


/**
 * A simple {@link Fragment} subclass.
 */
public class mapView extends Fragment implements View.OnClickListener, OnMapReadyCallback {


    ArrayList<String> usernames = new ArrayList<String>();
    int taskstart =0;
    int taskdone = 0;

    GoogleMap mMap;
    MapView mpView;
    String urlsent;
    Button search, listback, listnext;
    ArrayAdapter<String> adapter1;
    JSONArray data = new JSONArray();
    JSONArray userdata = new JSONArray();
    ArrayList<String> year = new ArrayList<String>();
    ArrayList<JSONObject> jsonUser = new ArrayList<JSONObject>();
    int startYear = 1970;
    int presentYear = 2017;
    Spinner countryspinner, statespinner, yearspinner;
    ArrayList<String> countries = new ArrayList<String>();
    ArrayList<String> states = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String selectedYear = "None";
    String  CountrySelectedInList= "None";
    String StateSelectedInlist = "None";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map_view, container, false);
        closeActivity = false;

        countryspinner = (Spinner) v.findViewById(R.id.countryspinner1);
        statespinner = (Spinner) v.findViewById(R.id.statespinner1);
        yearspinner = (Spinner) v.findViewById(R.id.yearspinner1);
        search = (Button) v.findViewById(R.id.search1);
        mpView = (MapView)v.findViewById(R.id.map_view1);
        mpView.onCreate(savedInstanceState);
        mpView.onResume();
        mpView.getMapAsync(this);
        search.setOnClickListener(this);
        year.clear();
        year.add("None");
        for (int i = startYear; i <= presentYear; i++) {
            year.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, year);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        yearspinner.setAdapter(adapter);


        yearspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedYear = parent.getItemAtPosition(position).toString();
                usernames.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedYear = parent.getItemAtPosition(0).toString();
            }
        });

        start();
        return v;

    }
    public mapView() {
        // Required empty public constructor
    }

    class SampleTask extends AsyncTask<String, String, LatLng> {
        String Userquery;


        public LatLng callGeocoderToLocation(String CountrySelected, String StateSelected  ){
            double latitude = 0.0;
            double longitude = 0.0;
            Geocoder locator = new Geocoder(getActivity());
            try {
                List<Address> state = locator.getFromLocationName(CountrySelected+", "+StateSelected, 1);
                for (Address stateLocation: state) {
                    if (stateLocation.hasLatitude())
                        latitude = stateLocation.getLatitude();
                    if (stateLocation.hasLongitude())
                        longitude = stateLocation.getLongitude();
                }
            } catch (Exception error) {
                Log.e("rew", "Address lookup Error", error);
            }
            LatLng stateLatLng = new LatLng(latitude, longitude);
            return stateLatLng;
        }

        protected void onProgressUpdate(String words1, String words2) {
//            if(words1 !=CountrySelectedInList && words2 != StateSelectedInlist ){
//                this.cancel(true);
//                cancel(true);
//            }

        }



        protected void onPostExecute(LatLng loc) {
            taskdone++;
            Log.i("rew", ""+taskdone+ "    "+taskstart);
            mMap.addMarker(new MarkerOptions().position(loc).title("Name:").snippet(Userquery));
            if(taskdone == taskstart){
                search.setEnabled(true);
                taskdone=0;
                taskstart=0;
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            }


        }

        protected LatLng doInBackground(String... words) {
            LatLng loc = callGeocoderToLocation(words[0], words[1]);
            Userquery = words[2];
//

            publishProgress(words[0], words[1]);
            return (loc);
        }



        protected void onPreExecute() {

        }


    }


    public void doAsync(String Country, String State, String name){
        String[] text = {Country, State, name};

//        String name = "dog";
//        SampleTask name = new SampleTask();


        new SampleTask().execute(text);



        //mMap.addMarker(new MarkerOptions().position(select).title("Name:").snippet(eachuser.getString("nickname")));

    }


        public void start() {
            Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
                public void onResponse(JSONArray response) {
                    data = response;
                    int lengthOfCountries = data.length();
                    //Log.d("rew", response.toString() + lengthOfCountries);
                    countries.clear();
                    countries.add("None");
                    for (int i = 0; i < lengthOfCountries; i++) {
                        try {
                            countries.add(data.getString(i));
                            //Log.d("rew", data.getString(i));
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, countries);
                            adapter.setDropDownViewResource(
                                    android.R.layout.simple_spinner_dropdown_item);
                            countryspinner.setAdapter(adapter);

                            countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    CountrySelectedInList = parent.getItemAtPosition(position).toString();
                                    usernames.clear();
                                    getStateInformation();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    CountrySelectedInList = parent.getItemAtPosition(0).toString();

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }
            };
            Response.ErrorListener failure = new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.d("rew", error.toString());
                }
            };
            String url = "http://bismarck.sdsu.edu/hometown/countries";
            JsonArrayRequest getRequest = new JsonArrayRequest(url, success, failure);
            VolleyQueue.instance(getActivity()).add(getRequest);
        }

        public void getStateInformation() {
            if (CountrySelectedInList == "None") {
                states.clear();
                states.add("None");
                StateSelectedInlist ="None";
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, states);
                adapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                statespinner.setAdapter(adapter);
                //Toast.makeText(getActivity(), "Choose Country first", Toast.LENGTH_LONG ).show();

            } else {

                Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
                    public void onResponse(JSONArray response) {
                        data = response;
                        int lengthOfStates = data.length();
                        //Log.d("rew", response.toString() + lengthOfStates);
                        states.clear();
                        states.add("None");
                        for (int i = 0; i < lengthOfStates; i++) {
                            try {
                                states.add(data.getString(i));
                                //Log.d("rew", data.getString(i));
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_item, states);
                                adapter.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item);
                                statespinner.setAdapter(adapter);

                                statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent, View view,
                                                               int position, long id) {
                                        StateSelectedInlist = parent.getItemAtPosition(position).toString().replace(" ","%20");

                                        usernames.clear();

                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        StateSelectedInlist = parent.getItemAtPosition(0).toString();

                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                };
                Response.ErrorListener failure = new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        Log.d("rew", error.toString());
                    }
                };
                String url = "http://bismarck.sdsu.edu/hometown/states?country=" + CountrySelectedInList;
                JsonArrayRequest getRequest = new JsonArrayRequest(url, success, failure);
                VolleyQueue.instance(getActivity()).add(getRequest);
            }
        }


        public void getTheDetails(){

            //Log.i("rew", "Start");
            Response.Listener<JSONArray> success = new Response.Listener<JSONArray>() {
                public void onResponse(JSONArray response) {
                    //Log.d("rew", response.toString());
                    userdata = response;
                    int numberOfUserAfterQuery = userdata.length();
                    try{
                        jsonUser.clear();
                        for(int i=0;i<numberOfUserAfterQuery;i++){
                            jsonUser.add(userdata.getJSONObject(i));

                        }
                        //Log.i("rew", ""+jsonUser.size());
                        usernames.clear();
                        Boolean skipFlag = false;

                        Log.i("rew", "clear callsed");
                        for(JSONObject eachuser: jsonUser){
                            //Log.i("rew", eachuser.getString("nickname"));

                            Double localLatitude = eachuser.getDouble("latitude");
                            Double localLongitude = eachuser.getDouble("longitude");
                            String loccountry = eachuser.getString("country");
                            String locstate = eachuser.getString("state");



                            if (localLatitude == 0.0 && localLongitude ==0.0 ){
                                taskstart++;
                                doAsync(eachuser.getString("country"), eachuser.getString("state"), eachuser.getString("nickname"));
                                skipFlag =true;

                            }


                            if(!skipFlag) {
                                //Toast.makeText(getActivity(), "called after async", Toast.LENGTH_LONG ).show();
                                LatLng select = new LatLng(localLatitude, localLongitude);
                                mMap.addMarker(new MarkerOptions().position(select).title("Name:").snippet(eachuser.getString("nickname")));
                                skipFlag = false;
                            }
                            skipFlag =false;
                        }
                        if(taskstart==0){
                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                            search.setEnabled(true);
                        }


                    }catch (JSONException e){
                        //Handle
                        Log.i("rew", "Error Occured");
                    }



                }
            };
            Response.ErrorListener failure = new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                    Log.d("rew", error.toString());
                }
            };

            if(CountrySelectedInList == "None" && StateSelectedInlist =="None" && selectedYear =="None"){
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true";
                LatLng loc = new LatLng(0.0, 0.0);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(1),2000,null);
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30";

                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList != "None" && StateSelectedInlist =="None" && selectedYear =="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&country="+CountrySelectedInList;
                callGeocoder(CountrySelectedInList);
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList;


                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList != "None" && StateSelectedInlist !="None" && selectedYear =="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&" +
                        "country="+CountrySelectedInList+"&state="+StateSelectedInlist;
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList+"&state="+StateSelectedInlist;
                callGeocoder(CountrySelectedInList, StateSelectedInlist);

                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList != "None" && StateSelectedInlist !="None" && selectedYear !="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&country="+CountrySelectedInList+"&state="+StateSelectedInlist + "&year="+selectedYear;
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList+"&state="+StateSelectedInlist + "&year="+selectedYear;
                callGeocoder(CountrySelectedInList, StateSelectedInlist);

                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList == "None" && StateSelectedInlist !="None" && selectedYear =="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&state="+StateSelectedInlist;
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&state="+StateSelectedInlist;

                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList == "None" && StateSelectedInlist !="None" && selectedYear !="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&state="+StateSelectedInlist + "&year="+selectedYear;
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&state="+StateSelectedInlist + "&year="+selectedYear;

                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList == "None" && StateSelectedInlist =="None" && selectedYear !="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&year="+selectedYear;
                LatLng loc = new LatLng(0.0, 0.0);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(1),2000,null);


                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&year="+selectedYear;
                //+countryURL+stateURL+yearURL+"
            }
            else if(CountrySelectedInList != "None" && StateSelectedInlist =="None" && selectedYear !="None") {
                urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&country="+CountrySelectedInList+"&year="+selectedYear;
                callGeocoder(CountrySelectedInList);
                //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList+"&year="+selectedYear;
                //+countryURL+stateURL+yearURL+"
            }


            Log.i("rew", urlsent);
            JsonArrayRequest getRequest = new JsonArrayRequest(urlsent, success, failure);
            VolleyQueue.instance(getActivity()).add(getRequest);

        }

        @Override
        public void onClick(View v) {
            mMap.clear();


            search.setEnabled(false);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


            Toast.makeText(getActivity(), "Query in progress", Toast.LENGTH_LONG ).show();
            Log.i("rew", "user names"+usernames);

            getTheDetails();
        }
        public void callGeocoder(String CountrySelected){

            double latitude = 0.0;
            double longitude = 0.0;
            Geocoder locator = new Geocoder(getActivity());
            try {
                List<Address> state = locator.getFromLocationName(CountrySelected ,1);
                for (Address stateLocation: state) {
                    if (stateLocation.hasLatitude())
                        latitude = stateLocation.getLatitude();
                    if (stateLocation.hasLongitude())
                        longitude = stateLocation.getLongitude();
                }
            } catch (Exception error) {
                Log.e("rew", "Address lookup Error", error);
            }
            LatLng stateLatLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(stateLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(3),2000,null);

        }
        public  void callGeocoder(String CountrySelected, String StateSelected) {
            double latitude = 0.0;
            double longitude = 0.0;
            Geocoder locator = new Geocoder(getActivity());
            try {
                List<Address> state = locator.getFromLocationName(CountrySelected+", "+StateSelected, 1);
                for (Address stateLocation: state) {
                    if (stateLocation.hasLatitude())
                        latitude = stateLocation.getLatitude();
                    if (stateLocation.hasLongitude())
                        longitude = stateLocation.getLongitude();
                }
            } catch (Exception error) {
                Log.e("rew", "Address lookup Error", error);
            }
            LatLng stateLatLng = new LatLng(latitude, longitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(stateLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(6),2000,null);


        }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
