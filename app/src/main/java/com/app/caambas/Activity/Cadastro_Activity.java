package com.app.caambas.Activity;

import android.app.AlertDialog;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import dmax.dialog.SpotsDialog;
import com.app.caambas.BD.Banco;
import com.google.android.material.snackbar.Snackbar;
import com.app.caambas.R;
import com.app.caambas.Class.Users;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


import dmax.dialog.SpotsDialog;

public class Cadastro_Activity extends AppCompatActivity {

    //Componentes
    private BootstrapButton btncadastro,btncancel;
    private EditText edtNome,edtEmail,edtNum,edtSenha,edtConfirmar,edtCPF;
    private Spinner spinnerloc;
    private String[] locais ={"Porto Velho","Cacoal","Ariquemes","Humaita"
    };

    //Suporte
    private FirebaseAuth autenticacao;
    private DatabaseReference banco;
    private Context contexto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        contexto = Cadastro_Activity.this;
        banco = Banco.getBanco();
        autenticacao = Banco.getAutenticacao();
        inicia_componetes_da_tela();


        ActionBar Actionbar = getSupportActionBar();
        if(Actionbar != null){
            Actionbar.setDisplayShowHomeEnabled(true);
        }

        btncadastro.setOnClickListener(v-> {
            AlertDialog loading = new SpotsDialog.Builder().setContext(contexto).setMessage("Cadastrando novo usuário").setCancelable(false).build();
            loading.show();

            //olha campo vazio
            if(!verCamposVazios()){
                String  nome = edtNome.getText().toString();
                String  CPF = edtCPF.getText().toString();
                String  num = edtNum.getText().toString();


                //salvar dados em Users
                Users usuario = new Users(nome,num,CPF);

                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String confirmar = edtConfirmar.getText().toString();

                if (!senha.equals(confirmar)){
                    loading.dismiss();
                    Snackbar.make(v, "Senha incorreta!",Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_300)).show();
                    return;
                }

                autenticacao.createUserWithEmailAndPassword(email,senha).addOnSuccessListener(authResult -> {
                    FirebaseUser user = authResult.getUser();
                    if(user!=null){
                        banco.child("users").child(user.getUid()).setValue(usuario);
                        user.sendEmailVerification().addOnSuccessListener(unused -> {
                            View view = getLayoutInflater().inflate(R.layout.verifica_email ,null);
                            TextView  txtTitulo = view.findViewById(R.id.txt_verifica1);
                            TextView  txtCorpo = view.findViewById(R.id.txt_verifica2);
                            BootstrapButton btnconfirm = view.findViewById(R.id.btn_verifica);
                            txtTitulo.setText("Verificação de Email");
                            txtCorpo.setText("Foi enviado uma mensagem de verificação de email para "+email);
                            AlertDialog dialog = new AlertDialog.Builder(contexto).setView(view).setCancelable(false).create();

                            btnconfirm.setOnClickListener(v2 -> {
                                Toast.makeText(contexto, "Verificação de email foi realizada com sucesso!", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                            dialog.show();
                        });
                    }
                else {
                        Toast.makeText(contexto, "Ocorreu um erro, tente novamente!", Toast.LENGTH_SHORT).show();
                    }
                loading.dismiss();

                }).addOnFailureListener(e ->{
                Toast.makeText(contexto, "Ocorreu um erro, tente novamente!", Toast.LENGTH_SHORT).show();
                String erroException ="";
                try {
                    throw e;

                }catch (FirebaseAuthWeakPasswordException excp){
                    erroException = "Senha muito fraca, por favor digite uma mais forte";

                }catch (FirebaseAuthInvalidCredentialsException excp){
                    erroException = "Email incorreto ou inválido";

                }catch (FirebaseAuthUserCollisionException excp){
                    erroException = "Email já foi cadastrado";

                }catch (Exception excp){
                    erroException = "Erro no cadastro de usuario";

                }
                Toast.makeText(contexto,"ERRO:" + erroException,Toast.LENGTH_SHORT).show();
                loading.dismiss();
                });
            }
            else{loading.dismiss();
            }
        });

        btncancel.setOnClickListener(v -> {
            finish();
        });

        contexto = Cadastro_Activity.this;
    }

    private boolean verCamposVazios() {
      Drawable background_erro = ResourcesCompat.getDrawable(getResources(),R.drawable.background_erro, getTheme());
      View view = findViewById(android.R.id.content);
      boolean campoVazio = false;

      if(vazio(edtNome)){
          edtNome.setBackground(background_erro);
          Timer(edtNome);
          campoVazio = true;
      }

        if(vazio(edtCPF)){
            edtCPF.setBackground(background_erro);
            Timer(edtCPF);
            campoVazio = true;
        }

        if(vazio(edtNum)){
            edtNum.setBackground(background_erro);
            Timer(edtNum);
            campoVazio = true;
        }

        if(vazio(edtEmail)){
            edtEmail.setBackground(background_erro);
            Timer(edtEmail);
            campoVazio = true;
        }

        if(vazio(edtSenha)){
            edtSenha.setBackground(background_erro);
            Timer(edtSenha);
            campoVazio = true;
        }

        if(vazio(edtConfirmar)){
            edtConfirmar.setBackground(background_erro);
            Timer(edtConfirmar);
            campoVazio = true;
        }

        if(campoVazio){
            Snackbar.make(view, "Preencher todos os campos!",Snackbar.LENGTH_SHORT).setBackgroundTint(getColor(R.color.red_300)).show();
        }return campoVazio;
    }

    private boolean vazio(EditText campovazio) {
        return campovazio.getText().toString().isEmpty();
    }

    public void Timer(EditText campo){
        CountDownTimer timer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                volta_Backgroud(campo);
            }
        };
        timer.start();
    }

    private void volta_Backgroud(EditText campo) {
        Drawable background = ResourcesCompat.getDrawable(getResources(),R.drawable.volta_background,getTheme());
        campo.setBackground(background);
    }

    //Iniciar componentes
    private void inicia_componetes_da_tela(){
        btncancel =findViewById(R.id.btnCadastrarCancel);
        btncadastro =findViewById(R.id.btnCadastrarConfirm);
        edtNome =findViewById(R.id.editText1);
        edtCPF =findViewById(R.id.editText2);
        edtNum =findViewById(R.id.edtPhone);
        edtEmail =findViewById(R.id.editText3);
        edtSenha =findViewById(R.id.editText4);
        edtConfirmar =findViewById(R.id.editText5);
    }
}