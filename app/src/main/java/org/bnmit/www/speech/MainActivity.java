package org.bnmit.www.speech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class MainActivity extends AppCompatActivity {
    private Spinner fromSpinner, toSpinner;
    private ImageView micIV;
    private TextInputEditText sourceEdt;
    private MaterialButton translateBtn;
    private TextView translatedTV;
    String [] fromLanguages = {"From", "English", "Afrikaans", "Bulgarians", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan", "Czech", "Hindi", "Welsh", "Urdu"};
    String [] toLanguages = {"To", "English", "Afrikaans", "Bulgarians", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan", "Czech", "Hindi", "Welsh", "Urdu"};
    private static final int REQUEST_CODE = 1;
    private int languageCode, fromLanguageCode, toLanguageCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fromSpinner = findViewById(R.id.idfromSpinner);
        toSpinner = findViewById(R.id.idToSpinner);
        sourceEdt = findViewById(R.id.idEdtSource);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translatedTV = findViewById(R.id.idTVTranslatedTV);
        micIV =findViewById(R.id.idVMic);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, fromLanguages);
        fromArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        fromSpinner.setAdapter(fromArrayAdapter);


        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = getLanguageCode(toLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter toArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, toLanguages);
        toArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        toSpinner.setAdapter(toArrayAdapter);
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if (sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "The source Text is Empty", Toast.LENGTH_SHORT).show();
                }else if (fromLanguageCode==0){
                    Toast.makeText(MainActivity.this, "Please Select Source Language", Toast.LENGTH_SHORT).show();
                }
                else if (toLanguageCode==0){
                    Toast.makeText(MainActivity.this, "Please Select language to be translated", Toast.LENGTH_SHORT).show();
                }
                else{
                    translateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
                }
            }
        });
    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String sourceEdt) {
        translatedTV.setText("Downloading Model...");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();
        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                translatedTV.setText("Translating...");
                translator.translate(sourceEdt).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        translatedTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error while translation", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed to download the model", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // String [] fromLanguages = {"From", "English", "Afrikaans", "Bulgarians", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan", "Czech", "Hindi", "Welsh", "Urdu"};
    private int getLanguageCode(String language) {
        int languageCode=0;
        switch (language)
        {
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "Bulgarian":
                languageCode = FirebaseTranslateLanguage.BG;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.KN;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Welsh":
                languageCode = FirebaseTranslateLanguage.CY;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            default:languageCode = 0;

        }
        return languageCode;
    }
}