package com.example.texto;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText et;
    TextView tv;
    int counter = 0;
    int note = 0;
    public HashMap<String, ArrayList<String>> table;
    public HashMap<String, Integer> freq;
    String input, WORD, present_input;
    int index = 0;
    ImageView next, prev;
    boolean Sugg;
    ArrayList<String> suggestions, auto_cmp;
    Button select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        freq = new HashMap<>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        select = findViewById(R.id.select);
        input = Utility.input;
        input = input.toLowerCase();

        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        table = SetMarkovChain(input);

        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Sugg = true;
                String input_s = s.toString();
                String[] inputarr = s.toString().split(" ");
                String lastword = inputarr[inputarr.length-1];
                WORD = lastword;
                lastword = lastword.toLowerCase();
                Log.d("MAIN: Word", lastword);
                present_input = s.toString();
                index = 0;
                if(Sugg){
                    suggestions = markovChain(lastword, table);
                }

                if(suggestions.isEmpty()){
                    suggestions.add("Try something else.......");
                }

                for(String string: suggestions){
                    Log.d("MAIN: Sugg", string);
                }
                if(suggestions != null){
                    tv.setText(suggestions.get(0));
                    index = 0;
                } else {
                    tv.setText("Try something else........");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        select.setOnClickListener(v->{
            if(!suggestions.isEmpty()){
                if(Sugg){
                    String present_sugg = suggestions.get(index);
                    Log.d("MAIN: PRE ", present_input+" "+present_sugg);
                    String output = present_input+" "+present_sugg;
                    et.setText(output);
                    et.setSelection(output.length());
                }
            }
        });

        next.setOnClickListener(v -> {
            if(suggestions != null){
                index = (index+1)%(suggestions.size());
                tv.setText(suggestions.get(index));
            } else {
                tv.setText("Try something else........");
            }
        });

        prev.setOnClickListener(v->{
            if(suggestions != null){
                index--;
                if(index < 0){
                    index = suggestions.size()-1;
                }
                tv.setText(suggestions.get(index));
            } else {
                tv.setText("Try something else........");
            }
        });
    }

    public ArrayList<String> markovChain(String currentWord , HashMap< String,ArrayList<String>> table){
        ArrayList<String> matched = new ArrayList<>();
        for(Map.Entry<String,ArrayList<String>> entry : table.entrySet()){
            if(entry.getKey().equals(currentWord)){
                matched = entry.getValue();
                return matched;
            }
        }
        return matched;
    }

    public  HashMap<String, ArrayList<String>> SetMarkovChain(String input){
        HashMap<String, ArrayList<AutoCom>> suggestions = new HashMap<>();
        String[] words = input.split(" ");
        for(String s: words){
            if(freq.containsKey(s)){
                int c = freq.get(s);
                freq.put(s, c+1);
            } else {
                freq.put(s, 1);
            }
        }
        int count = 0;
        for(int i = 0; i <= words.length-2; i++){

            if(suggestions.containsKey(words[i])){
                suggestions.get(words[i]).add(new AutoCom(words[i+1], freq.get(words[i+1])));
            } else {
                suggestions.put(words[i], new ArrayList<AutoCom>());
                suggestions.get(words[i]).add(new AutoCom(words[i+1], freq.get(words[i+1])));
            }

        }

        HashMap<String, ArrayList<String>> final_hash = new HashMap<>();

        for(Map.Entry<String,ArrayList<AutoCom>> entry : suggestions.entrySet()) {
            ArrayList<AutoCom> cmp = entry.getValue();
            Collections.sort(cmp);
            ArrayList<String> arrayList = new ArrayList<>();
            // add new unique elements arraylist and set it
            for(AutoCom c: cmp){
                // if contains then continue else add
                if(arrayList.contains(c)){
                    continue;
                }
                arrayList.add(c.str);
                Log.d("Freq", c.str + " " + c.occ + " HERE");
            }
            final_hash.put(entry.getKey(), arrayList);
        }

        return final_hash;
    }
}