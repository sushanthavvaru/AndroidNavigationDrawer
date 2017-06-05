package com.example.sushanth.identifyuser;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sushanth.identifyuser.MainActivity.closeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class listView extends Fragment implements View.OnClickListener {
    ArrayList<String> usernames = new ArrayList<String>();
    String urlsent;

    ListView listallsuers;
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

    public listView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);
        countryspinner = (Spinner) v.findViewById(R.id.countryspinner);
        statespinner = (Spinner) v.findViewById(R.id.statespinner);
        yearspinner = (Spinner) v.findViewById(R.id.yearspinner);
        listallsuers = (ListView) v.findViewById(R.id.listallusers);
        search = (Button) v.findViewById(R.id.search);
        search.setOnClickListener(this);
        listback = (Button) v.findViewById(R.id.listback);
        listnext = (Button) v.findViewById(R.id.listnext);
        listback.setVisibility(View.GONE);
        listnext.setVisibility(View.GONE);
        closeActivity = true;
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
            Toast.makeText(getActivity(), "Choose Country first", Toast.LENGTH_LONG ).show();

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
                                StateSelectedInlist = parent.getItemAtPosition(0).toString();;


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

                    Log.i("rew", "clear callsed");
                    for(JSONObject eachuser: jsonUser){
                        //Log.i("rew", eachuser.getString("nickname"));

                        usernames.add("Name:"+eachuser.getString("nickname") +"\nCountry:" +eachuser.getString("country")
                                +"\nState:" +eachuser.getString("state") +"\nCity:" +eachuser.getString("city")
                                +"\nYear:" +String.valueOf(eachuser.getInt("year")));



                    }
                    adapter1 =
                            new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, usernames);
                    listallsuers.setAdapter(adapter1);
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
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30";
            usernames.clear();
                    //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList != "None" && StateSelectedInlist =="None" && selectedYear =="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&country="+CountrySelectedInList;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList;

            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList != "None" && StateSelectedInlist !="None" && selectedYear =="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&" +
                    "country="+CountrySelectedInList+"&state="+StateSelectedInlist;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList+"&state="+StateSelectedInlist;
            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList != "None" && StateSelectedInlist !="None" && selectedYear !="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&country="+CountrySelectedInList+"&state="+StateSelectedInlist + "&year="+selectedYear;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList+"&state="+StateSelectedInlist + "&year="+selectedYear;
            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList == "None" && StateSelectedInlist !="None" && selectedYear =="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&state="+StateSelectedInlist;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&state="+StateSelectedInlist;
            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList == "None" && StateSelectedInlist !="None" && selectedYear !="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&state="+StateSelectedInlist + "&year="+selectedYear;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&state="+StateSelectedInlist + "&year="+selectedYear;
            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList == "None" && StateSelectedInlist =="None" && selectedYear !="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&year="+selectedYear;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&year="+selectedYear;
            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }
        else if(CountrySelectedInList != "None" && StateSelectedInlist =="None" && selectedYear !="None") {
            urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&country="+CountrySelectedInList+"&year="+selectedYear;
            //urlsent ="http://bismarck.sdsu.edu/hometown/users?&reverse=true&page=0&pagesize=30&country="+CountrySelectedInList+"&year="+selectedYear;
            usernames.clear();
            //+countryURL+stateURL+yearURL+"
        }


        Log.i("rew", urlsent);
        JsonArrayRequest getRequest = new JsonArrayRequest(urlsent, success, failure);
        VolleyQueue.instance(getActivity()).add(getRequest);

    }

    @Override
    public void onClick(View v) {


        Log.i("rew", "user names"+usernames);
        getTheDetails();
    }
}
