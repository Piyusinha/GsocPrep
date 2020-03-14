package com.gsoc.gsocprep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.gsoc.gsocprep.model.manufacturers;
import com.gsoc.gsocprep.model.modelinformation;
import com.gsoc.gsocprep.model.vehicleModels;
import com.gsoc.gsocprep.utils.APIInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Context context;
    String selectedhsm;
    AutoCompleteTextView modeautol;
    private String TAG = "app";
    AutoCompleteTextView manufacature;
    String selected;
    ArrayList<String> carname = new ArrayList<String>();
    ArrayList<manufacturers> carhs = new ArrayList<manufacturers>();
    ArrayList<String> model = new ArrayList<String>();
    ArrayList<vehicleModels> carmodel = new ArrayList<vehicleModels>();
    AutoCompleteTextView modelyear;
    String modelselected;
    ArrayAdapter<String> adapter1;
    ArrayList<String> date = new ArrayList<String>();
    String tsn ;
    ArrayAdapter<String> adapter2;
    String selectedDate;
    AutoCompleteTextView fuel;
    TextView enginecapacity;
    String engine;

    String[] fueltype = {"Benzene","Diesel","Gas","Hybrid","Electric"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "HIEWW", Toast.LENGTH_SHORT).show();
        manufacatureapi();


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, carname);
        manufacature = findViewById(R.id.mandropmenu);
        manufacature.setAdapter(adapter);
        manufacature.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                new datacomparing().execute();
            }
        });




    }

    private void modelapi(String hsm) {

        model.clear();
        APIInterface apiInterface = RetrofitClient.getClient(context).create(APIInterface.class);
        apiInterface.getCommercialName(hsm).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<vehicleModels>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<vehicleModels> vehicleModels) {
                        for (int i = 0; i < vehicleModels.size(); i++) {


                            model.add(vehicleModels.get(i).getCommercialName());
                            com.gsoc.gsocprep.model.vehicleModels v = new vehicleModels(vehicleModels.get(i).getTsn(), vehicleModels.get(i).getCommercialName(), vehicleModels.get(i).getAllotmentDate());
                            carmodel.add(v);


                        }
                        Log.d(TAG, "onNext: " + model.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getLocalizedMessage());

                    }

                    @Override
                    public void onComplete() {
                        adapter1 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, model);

                        modeautol = findViewById(R.id.modellist);

                        modeautol.setAdapter(adapter1);
                        modeautol.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                modelselected = parent.getItemAtPosition(position).toString();
                                new finddateallotment().execute();
                            }
                        });
                        adapter1.notifyDataSetChanged();


                    }
                });

    }
    public void findengine()

    {
        APIInterface apiInterface = RetrofitClient.getClient(context).create(APIInterface.class);
        apiInterface.getcategory(selectedhsm,tsn).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<modelinformation>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(modelinformation modelinformation) {
                       engine=modelinformation.getEngineCapacity();
                        Toast.makeText(MainActivity.this, engine, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        enginecapacity=findViewById(R.id.enginecapacit);
                        enginecapacity.setText(engine);
                    }
                });

    }

    private void manufacatureapi() {
        APIInterface apiInterface = RetrofitClient.getClient(context).create(APIInterface.class);
        apiInterface.getName().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<manufacturers>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<manufacturers> manufacturers) {
                        for (int i = 0; i < manufacturers.size(); i++) {
                            com.gsoc.gsocprep.model.manufacturers m = new manufacturers(manufacturers.get(i).getHsn(), manufacturers.get(i).getName());
                            carhs.add(m);
                            carname.add(manufacturers.get(i).getName());


                        }
                        Log.d(TAG, "onNext: " + carname.toString());
                    }

                    @Override
                    public void onError(Throwable e) {


                    }

                    @Override
                    public void onComplete() {
                        Collections.sort(carname, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(o2);
                            }
                        });


                    }
                });
    }

    public class finddateallotment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            date.clear();
            Iterator<vehicleModels> itr = carmodel.iterator();
            while (itr.hasNext()) {
                vehicleModels v = itr.next();
                if (v.getCommercialName().equals(modelselected)) {
                    date.add(v.getAllotmentDate());


                }


            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
            modelyear = findViewById(R.id.modelyear);
            adapter2 = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, date);
            modelyear.setAdapter(adapter2);
            modelyear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedDate=parent.getItemAtPosition(position).toString();
                    Toast.makeText(MainActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                    new findvehicletsn().execute();
                    ArrayAdapter<String> fueladapter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item,fueltype);
                    fuel= findViewById(R.id.fueltype);
                    fuel.setAdapter(fueladapter);
                    fuel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            findengine();
                        }
                    });



                }

            });

        }
    }

    public class findvehicletsn extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings) {
            Iterator<vehicleModels> itr = carmodel.iterator();
            while (itr.hasNext()) {
                vehicleModels v = itr.next();
                if (v.getCommercialName().equals(modelselected)&&v.getAllotmentDate().equals(selectedDate)) {
                    tsn=v.getTsn();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, tsn, Toast.LENGTH_SHORT).show();
        }

    }






    public class datacomparing extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            modelapi(selectedhsm);




        }

        @Override
        protected String doInBackground(String... strings) {
            Iterator<manufacturers> itr = carhs.iterator();

            while (itr.hasNext()) {

                manufacturers m = itr.next();

                if (m.getName().equals(selected)) {
                    selectedhsm = m.getHsn();
                } else {

                }
            }

            return null;
        }


    }


}
