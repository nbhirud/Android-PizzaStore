package com.example.sumeesh.pizzastore;

import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        String items[] = getResources().getStringArray(R.array.toppings);

        TextView toppingPrice, deliveryPrice, toppingList, totalPrice;

        toppingPrice = (TextView) findViewById(R.id.toppingPrice);
        deliveryPrice = (TextView) findViewById(R.id.textViewDeliveryPrice);
        toppingList = (TextView) findViewById(R.id.textViewToppingList);
        totalPrice = (TextView) findViewById(R.id.textViewTotal);

        ArrayList<Integer> toppings = (ArrayList<Integer>) getIntent().getSerializableExtra(MainActivity.TOPPINGS);

        String toppingAppend = "";

        SharedPreferences prefs = getSharedPreferences("MY_PREF",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("TOPPINGS_SIZE", toppings.size());
        for(int i = 0; i < toppings.size(); i++){
            editor.remove("TOPPING_"+i);
            editor.putInt("TOPPING_"+i, toppings.get(i));
        }
        editor.apply();

        if(toppings.size() > 0) {
            toppingAppend = items[toppings.get(0)];
            for( int i = 1; i < toppings.size(); i++) {
                toppingAppend += "," + items[toppings.get(i)];
            }
        }

        double toppingCost = 1.5 * toppings.size();

        toppingPrice.setText("$"+toppingCost);

        toppingList.setText(toppingAppend);

        Boolean is_dilverychecked = getIntent().getBooleanExtra(MainActivity.IS_DELIVERED, false);

        double deliveryCharge = 0.0;

        if(is_dilverychecked) {
            deliveryCharge = 2.0;
            deliveryPrice.setText("$" + deliveryCharge);
        }

        double total;

        total = 6.5 + toppingCost + deliveryCharge;

        totalPrice.setText("$" + total);

        TextView totalLabel = (TextView) findViewById(R.id.textView5);
        totalLabel.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        totalPrice.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
}
