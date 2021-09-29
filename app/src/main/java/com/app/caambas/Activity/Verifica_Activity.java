package com.app.caambas.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.app.caambas.Adapter.AdapterCacamba;
import com.app.caambas.BD.Banco;
import com.app.caambas.Class.Cacambas;
import com.app.caambas.Class.Users;
import com.app.caambas.Fragment.FragmentRetirada;
import com.app.caambas.Fragment.FragmentVerifica;
import com.app.caambas.Listener.ListenerAdapter;
import com.app.caambas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class Verifica_Activity extends AppCompatActivity {

    private Context contexto;
    private String origem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifica_activity);
        iniciaCompenetes();

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null){
            origem = bundle.getString("origem");
        }
        else{
            Toast.makeText(contexto, "Algo deu errado!", Toast.LENGTH_SHORT).show();
            finish();
        }
        carregarFragmento();
    }

    private void carregarFragmento() {
        if(origem.equals("retirada")){
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.fragmento, FragmentRetirada.class,null).commit();
        }
        else{
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.fragmento, FragmentVerifica.class,null).commit();
        }
    }


    private void iniciaCompenetes() {
        contexto = Verifica_Activity.this;

    }
}