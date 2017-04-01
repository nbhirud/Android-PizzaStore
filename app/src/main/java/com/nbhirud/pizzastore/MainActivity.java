package com.nbhirud.pizzastore;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.sumeesh.pizzastore.R;

import java.util.ArrayList;

//Github sync test 1
public class MainActivity extends AppCompatActivity {

    Button addPizza, clearPizza, checkout;
    ProgressBar progressBar;
    CheckBox prevOrder, delivery;
    AlertDialog alert;
    ArrayList<Integer> items = new ArrayList<>();
    String[] topping;
    int resId[] = {R.drawable.bacon, R.drawable.cheese, R.drawable.garlic, R.drawable.green_pepper, R.drawable.mushroom, R.drawable.olives, R.drawable.onion, R.drawable.red_pepper, R.drawable.tomato};

    public static final String TOPPINGS = "TOPPINGS";
    public static final String IS_DELIVERED = "DELIVERY";
    public static final String MY_PREF = "MY_PREF";

    TableRow tr1, tr2;

    SharedPreferences prefs;

    Activity mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to display the App Icon on the Action Bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        topping = getResources().getStringArray(R.array.toppings);

        tr1 = (TableRow) findViewById(R.id.trow1);
        tr2 = (TableRow) findViewById(R.id.trow2);

        addPizza = (Button) findViewById(R.id.buttonAdd);
        clearPizza = (Button) findViewById(R.id.buttonClear);
        checkout = (Button) findViewById(R.id.buttonCheckout);

        delivery = (CheckBox) findViewById(R.id.checkBoxDelivery);
        prevOrder = (CheckBox) findViewById(R.id.checkBoxLoadPrev);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);

        prevOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(prevOrder.isChecked()) {
                    tr1.removeAllViews();
                    tr2.removeAllViews();
                    items.clear();
                    Log.d("demo","Checked");
                    SharedPreferences prefs = getSharedPreferences("MY_PREF",MODE_PRIVATE);
                    int size = prefs.getInt("TOPPINGS_SIZE", -1);
                    Log.d("demo","Size = "+size);
                    if(size >= 0) {
                        for(int i = 0; i < size; i++) {
                            items.add(prefs.getInt("TOPPING_"+i,0));
                        }
                        displayToppings();
                    } else {
                        Toast.makeText(MainActivity.this, "No Previous Order Available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    tr1.removeAllViews();
                    tr2.removeAllViews();
                    items.clear();
                }
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose a Topping");
        builder.setItems(topping, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items.size() < 10) {
                    items.add(which);
                    displayToppings();
                    progressBar.setProgress(items.size() * 10);
                } else {
                    Toast.makeText(MainActivity.this, "Can't add more topping, Maximum Capacity reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        alert = builder.create();

        //Add Topping functionality
        addPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });


        //Clear Button Functionality
        clearPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                tr1.removeAllViews();
                tr2.removeAllViews();
                prevOrder.setChecked(false);
                delivery.setChecked(false);
            }
        });


        //Checkout Button Functionality
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("demo","Intent here!!");

                Intent i = new Intent(MainActivity.this, OrderActivity.class);
                i.putExtra(TOPPINGS, items);
                i.putExtra(IS_DELIVERED, delivery.isChecked());
                startActivity(i);
                //finish();
                items.clear();
                tr1.removeAllViews();
                tr2.removeAllViews();
                prevOrder.setChecked(false);
                delivery.setChecked(false);
            }
        });

    }

    private void displayToppings() {
        tr1.removeAllViews();
        tr2.removeAllViews();
        for(int i = 0; i < items.size(); i++) {
            toppingImg img = new toppingImg(this);
            img.numTopping = items.get(i);
            img.setImageResource(resId[items.get(i)]);
            TableRow.LayoutParams params = new TableRow.LayoutParams(70,70);
            img.setLayoutParams(params);
            img.setOnClickListener(img);
            if(i < 5) {
                tr1.addView(img);
            } else {
                tr2.addView(img);
            }
        }
    }

    class toppingImg extends ImageView implements View.OnClickListener {
        int numTopping = -1;

        public toppingImg(Context context) {
            super(context);
        }

        @Override
        public void onClick(View v) {
            items.remove(items.indexOf(numTopping));
            displayToppings();
            progressBar.setProgress(items.size() * 10);
        }
    }

}
