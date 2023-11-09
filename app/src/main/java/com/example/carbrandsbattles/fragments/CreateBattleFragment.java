package com.example.carbrandsbattles.fragments;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.carbrandsbattles.R;

public class CreateBattleFragment extends Fragment {

    private static final String ARG_USERNAME = "userName";
    private static final String ARG_PUNISHMENT = "punishment";

    private EditText userNameEditText;
    private EditText punishmentEditText;
    private Button createBattleButton;

    public static CreateBattleFragment newInstance() {
        CreateBattleFragment fragment = new CreateBattleFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public CreateBattleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_battle,
                container, false);
        userNameEditText = view.findViewById(R.id.userNameEditText);
        punishmentEditText = view.findViewById(R.id.punishmentEditTextTextMultiLine);
        createBattleButton = view.findViewById(R.id.createBattleButton);
        createBattleButton.setOnClickListener(view1 -> {
            createBattle();
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_battle, container, false);
    }

    private void createBattle() {
        if(userNameEditText.getText().length() != 0 && punishmentEditText.getText().length() != 0){
            //TODO implement post request
        }
    }
}