package com.emem.handyapps;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.emem.handyapps.database.DatabaseHelper;
import com.emem.handyapps.database.Unit;

import java.util.ArrayList;
import java.util.List;

public class ConverterActivity extends AppCompatActivity {
    private Unit source;
    private Unit target;
    private List<Unit> units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);
        Spinner unitType = findViewById(R.id.unitType);
        unitType.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectedType = parent.getItemAtPosition(position).toString();
                        onUnitTypeSelect(selectedType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                }
        );
        List<String> unitTypes = new ArrayList<>();

        unitTypes.add(Unit.CURRENCY);
        unitTypes.add(Unit.LENGTH);
        unitTypes.add(Unit.AREA);
        unitTypes.add(Unit.VOLUME);
        unitTypes.add(Unit.WEIGHT);

        ArrayAdapter<String> unitTypesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitTypes);
        unitType.setAdapter(unitTypesAdapter);
    }

    private void onUnitTypeSelect(String unitType){
        DatabaseHelper dbHelper = new DatabaseHelper(getApplication());
        units = Unit.getUnits(dbHelper, unitType);
        source = null;
        target = null;
        Spinner sourceUnit = findViewById(R.id.sourceUnit);
        sourceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUnit = parent.getItemAtPosition(position).toString();
                for (Unit u: units){
                    if (u.getName().equals(selectedUnit))
                        source = u;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Spinner targetUnit = findViewById(R.id.targetUnit);
        targetUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUnit = parent.getItemAtPosition(position).toString();
                for (Unit u: units){
                    if (u.getName().equals(selectedUnit))
                        target = u;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        List<String> unitList = new ArrayList<>();
        for (Unit u: units)
            unitList.add(u.getName());

        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitList);
        sourceUnit.setAdapter(unitsAdapter);
        targetUnit.setAdapter(unitsAdapter);
    }

    public void onConvertButton(View view){
        EditText sourceValue = findViewById(R.id.sourceValue);
        TextView targetValue = findViewById(R.id.targetValue);
        Double sourceVal = Double.parseDouble(sourceValue.getText().toString());
        if (target!=null && source!=null){
            Double value = sourceVal;
            value = value / source.getValue();
            value = value * target.getValue();
            targetValue.setText(value.toString());
        }else{
            targetValue.setText("");
        }
    }
}
