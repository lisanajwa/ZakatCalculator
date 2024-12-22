package com.example.zakatcalculation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;

public class CalculationActivity extends AppCompatActivity {

    private EditText weightInput, goldValueInput;
    private RadioGroup goldTypeGroup;
    private TextView totalValueOutput, zakatPayableOutput, totalZakatOutput, urufOutput;
    private Button calculateButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        weightInput = findViewById(R.id.weight_input);
        goldValueInput = findViewById(R.id.gold_value_input);
        goldTypeGroup = findViewById(R.id.gold_type_group);
        totalValueOutput = findViewById(R.id.total_value_output);
        zakatPayableOutput = findViewById(R.id.zakat_payable_output);
        totalZakatOutput = findViewById(R.id.total_zakat_output);
        urufOutput = findViewById(R.id.uruf_output);
        calculateButton = findViewById(R.id.calculate_button);
        resetButton = findViewById(R.id.reset_button);

        calculateButton.setOnClickListener(v -> calculateZakat());

        resetButton.setOnClickListener(v -> resetFields());
    }

    private void calculateZakat() {
        try {
            double weight = Double.parseDouble(weightInput.getText().toString());
            double valuePerGram = Double.parseDouble(goldValueInput.getText().toString());
            int selectedTypeId = goldTypeGroup.getCheckedRadioButtonId();

            if (selectedTypeId == -1) {
                Toast.makeText(this, "Please select a gold type", Toast.LENGTH_SHORT).show();
                return;
            }

            double threshold;
            if (selectedTypeId == R.id.radio_keep) {
                threshold = 85;
            } else if (selectedTypeId == R.id.radio_wear) {
                threshold = 200;
            } else {
                throw new IllegalArgumentException("Invalid gold type selection");
            }

            double totalValue = weight * valuePerGram;
            double uruf = weight - threshold;
            double zakatPayableWeight = Math.max(0, uruf);
            double zakatPayableValue = zakatPayableWeight * valuePerGram;
            double totalZakat = zakatPayableValue * 0.025;

            totalValueOutput.setText("Total Gold Value: RM" + String.format("%.2f", totalValue));
            urufOutput.setText("Gold Weight Minus Uruf: " + String.format("%.2f", uruf) + " grams");
            zakatPayableOutput.setText("Zakat Payable Value: RM" + String.format("%.2f", zakatPayableValue));
            totalZakatOutput.setText("Total Zakat: RM" + String.format("%.2f", totalZakat));

        } catch (Exception e) {
            Toast.makeText(this, "Please fill all inputs correctly", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetFields() {
        weightInput.setText("");
        goldValueInput.setText("");
        goldTypeGroup.check(R.id.radio_keep); 
        totalValueOutput.setText("Total Gold Value: RM0.00");
        urufOutput.setText("Gold Weight Minus Uruf: 0 grams");
        zakatPayableOutput.setText("Zakat Payable: RM0.00");
        totalZakatOutput.setText("Total Zakat: RM0.00");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String appUrl = "https://github.com/lisanajwa";
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing Zakat Calculation App: " + appUrl);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
