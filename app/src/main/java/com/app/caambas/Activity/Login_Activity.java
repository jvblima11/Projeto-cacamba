package com.app.caambas.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.app.caambas.R;
import com.beardedhen.androidbootstrap.BootstrapButton;
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

public class Login_Activity extends AppCompatActivity {

    //Componentes
    private EditText edtUsuario, edtSenha;
    private TextView txtCadastro, txtEsqueceu;
    private Button btnEntrar;

    //Suporte
    private Context contexto;
    private DatabaseReference banco;
    private FirebaseAuth autenticacao;
    private android.app.AlertDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        banco = Banco.getBanco();
        Toast.makeText(this, banco.getRoot().toString(), Toast.LENGTH_SHORT).show();

        btnEntrar = findViewById(R.id.btnEntrar);
        edtUsuario = findViewById(R.id.user_text);
        edtSenha = findViewById(R.id.password_text);
        txtCadastro = findViewById(R.id.text_cadastro);
        txtEsqueceu = findViewById(R.id.text_esquec);
        contexto = Login_Activity.this;
        loading = new SpotsDialog.Builder().setContext(contexto).setMessage("CARREGANDO").setCancelable(false).build();

        btnEntrar.setOnClickListener(v->{
            String usuario = edtUsuario.getText().toString();
            String senha = edtSenha.getText().toString();

            //Verificar campos do usuário
            if(!usuario.isEmpty()){
                if(!senha.isEmpty()){
                    loading.show();
                    autenticaUsuario(usuario,senha);
                }
                else{
                    Snackbar.make(v, "Insira a senha!", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.red_300, getTheme())).show();
                }
            }
            else{
                Snackbar.make(v, "Insira o nome de usuário!", Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.red_300, getTheme())).show();
            }
        });


        txtCadastro.setOnClickListener(v -> {
            vaiParaCadastro();
        });


        txtEsqueceu.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.esqueci_senha,null);
            EditText edtEmail =view.findViewById(R.id.esqueci_email);
            BootstrapButton btnConfirm = view.findViewById(R.id.btn_esqueci_confirm);
            BootstrapButton btnCancel = view.findViewById(R.id.btn_esqueci_cancel);

            AlertDialog dialog_Email = new AlertDialog.Builder(contexto).setView(view).create();
            dialog_Email.show();

            btnCancel.setOnClickListener(v1 -> {
                dialog_Email.dismiss();
            });

            btnConfirm.setOnClickListener(v1 -> {
                String email = edtEmail.getText().toString();
                if(!email.isEmpty()){
                    autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           Toast.makeText(contexto, "Email de redefiniçãp de senha enviado!", Toast.LENGTH_SHORT).show();
                       }
                       else{
                           Toast.makeText(contexto, "Ocorreu algum erro, tentar novamente!", Toast.LENGTH_SHORT).show();
                       }
                    });
                }
                else{
                    Snackbar.make(v,"informe seu email!",Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.red_300, getTheme()))
                            .show();
                }
            });
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        limparCampos();
        verificarUsuarioLogado();
    }

    private  void verificarUsuarioLogado(){
        if(Banco.temUsuarioLogado()){
            loading.show();
            FirebaseUser user = Banco.getUsuarioLogado();
            if(user.isEmailVerified()){
                recuperarUsuario();
            }
            else{
                loading.dismiss();
            }
        }
    }

    private void autenticaUsuario(String usuario, String senha) {
        autenticacao = Banco.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario,senha).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser usuariaLogado = Banco.getUsuarioLogado();
                if(usuariaLogado != null){
                    if(usuariaLogado.isEmailVerified()){
                        recuperarUsuario();
                    }
                    else{
                        loading.dismiss();
                        Toast.makeText(contexto, "Email incorreto!", Toast.LENGTH_SHORT).show();
                    }
                }
                loading.dismiss();
            }
        });
    }

    private void recuperarUsuario(){
        String idUsuario = Banco.getIdUsuarioLogado();
        if(idUsuario != null){
            DatabaseReference usuarioReference = banco.child("usuarios").child(idUsuario);
            usuarioReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Users users = snapshot.getValue(Users.class);
                        Users.setUsuariologado(users);
                        loading.dismiss();
                        vaiParaMain();
                    }
                    loading.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(contexto, "Algo deu errado ao recuperar o usuário!", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            });
        }
    }

    private void  vaiParaCadastro(){
        Intent intent = new Intent(Login_Activity.this , Cadastro_Activity.class);
        startActivity(intent);
    }

    private void vaiParaMain() {
        Intent intent = new Intent(Login_Activity.this, Main_Activity.class);
        startActivity(intent);
    }

    private void limparCampos(){
        edtUsuario.setText("");
        edtSenha.setText("");
    }

    private  void Componentes(){
        btnEntrar = findViewById(R.id.btnEntrar);
        edtUsuario = findViewById(R.id.user_text);
        edtSenha = findViewById(R.id.password_text);
        txtEsqueceu = findViewById(R.id.text_esquec);
        txtCadastro = findViewById(R.id.text_cadastro);

        contexto = Login_Activity.this;
        banco =Banco.getBanco();
        autenticacao = Banco.getAutenticacao();

        loading = new SpotsDialog.Builder().setContext(contexto).setMessage("Entrando").build();

    }
}


