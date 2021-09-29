package com.app.caambas.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.app.caambas.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.app.caambas.Class.Users;
import com.app.caambas.BD.Banco;

import dmax.dialog.SpotsDialog;

public class Main_Activity extends AppCompatActivity {
    //Componentes da tela
    private Button btnaluga, btnverifica,btnretirada;
    private NavigationView navigation;
    private MaterialToolbar toolbar;
    private DrawerLayout draw;

    //suporte
    private FirebaseAuth autenticacao;
    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contexto =Main_Activity.this;
        autenticacao = Banco.getAutenticacao();
        iniciaComponentes();

        btnverifica.setOnClickListener(v -> {
            Intent intent = new Intent(contexto,Verifica_Activity.class);
            intent.putExtra("origem","verifica");
            startActivity(intent);
        });

        btnretirada.setOnClickListener(v -> {
            Intent intent = new Intent(contexto,Verifica_Activity.class);
            intent.putExtra("origem","retirada");
            startActivity(intent);
        });

        btnaluga.setOnClickListener(v ->{
            //fazer o aluga activity
            Intent intent = new Intent(contexto,Aluga_Activity.class);
            startActivity(intent);
        });

        //barra de ação
        ActionBar actionBar =getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        //barra de ferramenta
        toolbar.setNavigationOnClickListener(v ->{
            draw.openDrawer(GravityCompat.START);
        });

        //barra de navegação
        navigation.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.action_sair) {
                deslogar();
            }
            draw.closeDrawer(GravityCompat.START);
            return  true;
        });
    }

    public void deslogar(){
        autenticacao.signOut();
        Users.setUsuariologado(null);
        finish();
    }
    public  void iniciaComponentes(){
        btnaluga = findViewById(R.id.btn_aluga);
        btnverifica = findViewById(R.id.btn_verifica_aluga);
        btnretirada = findViewById(R.id.btn_retirada);
        toolbar = findViewById(R.id.toolbar);
        navigation =findViewById(R.id.barra_navegacao);
        draw = findViewById(R.id.drawer_layout);
    }
}