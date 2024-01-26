package com.example.stockapiapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private TextView idTvDetails, symbolTvDetails, priceTvDetails, changeTvDetails,changePercentTvDetails, highTvDetails, lowTvDetails;
    //db helper
    private DatabaseHelper db;

    //actionbar
    private ActionBar actionBar;

    private String recordID;
    public String symbol;

    private List<String> xValues = Arrays.asList("Low", "High");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        recordID = intent.getStringExtra("RECORD_ID");

        db = new DatabaseHelper(this);
        //idTvDetails = findViewById(R.id.idTvDetails);
        symbolTvDetails = findViewById(R.id.symbolTvDetails);
        priceTvDetails = findViewById(R.id.priceTvDetails);
        changeTvDetails = findViewById(R.id.changeTvDetails);
        changePercentTvDetails = findViewById(R.id.changePercentTvDetails);

        highTvDetails = findViewById(R.id.highTvDetails);
        lowTvDetails = findViewById(R.id.lowTvDetails);

        showRecordDetails();

        actionBar = getSupportActionBar();
        actionBar.setTitle("Szczegóły: " + symbol);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @SuppressLint("Range")
    private String showRecordDetails() {
              String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " +
                DatabaseHelper.COLUMN_ID + " =\"" + recordID +"\"";

        SQLiteDatabase dbHelper = db.getWritableDatabase();
        Cursor cursor = dbHelper.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                //get data
                //@SuppressLint("Range") String id = "" + cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                symbol = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_SYMBOL));

                //set data
                //idTvDetails.setText(id);
                symbolTvDetails.setText("Symbol: " + symbol);
          }while (cursor.moveToNext());

        }
        db.close();
        updateStockDataFromAPI();
        return symbol;
    }

    private void updateStockDataFromAPI() {
        AlphaVantageApi alphaVantageApi = RetrofitClient.getClient().create(AlphaVantageApi.class);

        Call<StockQuoteResponse> call = alphaVantageApi.getStockQuote(symbol);
        call.enqueue(new Callback<StockQuoteResponse>() {
            @Override
            public void onResponse(Call<StockQuoteResponse> call, Response<StockQuoteResponse> response) {
                if (response.isSuccessful()) {
                    StockQuote stockQuote = response.body().getGlobalQuote();
                    if (stockQuote != null) {
                        //symbolTvDetails.setText("Symbol: " + stockQuote.getHigh());
                        priceTvDetails.setText("Price: " + stockQuote.getPrice());
                        changeTvDetails.setText("Change: " + stockQuote.getChange());
                        changePercentTvDetails.setText("Change percent: " + stockQuote.getChangePercent());
                        //highTvDetails.setText("High: " + stockQuote.getHigh());
                        //lowTvDetails.setText("Low: " + stockQuote.getLow());


                        //chart
                        ValueFormatter customValueFormatter = new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                // You can customize the formatting as needed
                                return value + " $";
                            }
                        };
                        float low = Float.parseFloat(stockQuote.getLow());
                        float high = Float.parseFloat(stockQuote.getHigh());
                        BarChart barChart = findViewById(R.id.chart);
                        barChart.getAxisRight().setDrawLabels(false);

                        ArrayList<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(0, low));
                        entries.add(new BarEntry(1, high));

                        YAxis yAxis = barChart.getAxisLeft();
                        yAxis.setAxisMinimum(0f);
                        yAxis.setAxisMaximum(high);
                        yAxis.setAxisLineWidth(2f);
                        yAxis.setAxisLineColor(Color.BLACK);
                        yAxis.setLabelCount(2);

                        BarDataSet dataSet = new BarDataSet(entries, symbol);
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        dataSet.setValueTextSize(15f);
                        dataSet.setValueFormatter(customValueFormatter);

                        BarData barData = new BarData(dataSet);
                        barChart.setData(barData);

                        barChart.getDescription().setEnabled(false);
                        barChart.invalidate();
                        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
                        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        barChart.getXAxis().setGranularity(1f);
                        barChart.getXAxis().setGranularityEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<StockQuoteResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); //go to previous activity
        return super.onSupportNavigateUp();
    }
}