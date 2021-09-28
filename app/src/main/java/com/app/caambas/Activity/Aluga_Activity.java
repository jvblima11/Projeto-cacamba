package com.app.caambas.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.app.caambas.BD.Banco;
import com.app.caambas.Class.Cacambas;
import com.app.caambas.Listener.ListenerData;
import com.app.caambas.R;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.app.caambas.Class.Users;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dmax.dialog.SpotsDialog;


public class Aluga_Activity extends AppCompatActivity {

    //tela
    private BootstrapButton btsdata,btsconfirm,btscancel;
    private RadioButton rb_tam4,rb_tam5,rb_tam7,rbdinheiro,rbcredito,rbdebito;
    private EditText edtrua,edtnum,edtbairro,edtcidade;
    private TextView txtpreco;

    //suporte
    private Cacambas cacambas = new Cacambas();
    private  DatabaseReference banco;
    private AlertDialog loading;
    private Context contexto;
    private String dataEntrega,Tamanho,metodoPagamento,rua,num,bairro,cidade,Preco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluga);

        iniciaCompenetes();


        //radio button dos tamanhos

        rb_tam4.setOnClickListener(v->{
            rb_tam5.setChecked(false);
            rb_tam7.setChecked(false);
            txtpreco.setText("200 R$");
            Preco = "200 R$";
            Tamanho = "4 metros cubicos";
        });

        rb_tam5.setOnClickListener(v->{
            rb_tam4.setChecked(false);
            rb_tam7.setChecked(false);
            txtpreco.setText("280 R$");
            Preco = "280 R$";
            Tamanho = "5 metros cubicos";
        });

        rb_tam7.setOnClickListener(v->{
            rb_tam5.setChecked(false);
            rb_tam4.setChecked(false);
            txtpreco.setText("350 R$");
            Preco = "350 R$";
            Tamanho = "7 metros cubicos";
        });

        rb_tam4.performClick();

        //radio button de Pagamento

        rbdinheiro.setOnClickListener(v ->{
            rbcredito.setChecked(false);
            rbdebito.setChecked(false);
            metodoPagamento ="Dinheiro";
        });

        rbdebito.setOnClickListener(v ->{
            rbcredito.setChecked(false);
            rbdinheiro.setChecked(false);
            metodoPagamento ="Debito";
        });

        rbcredito.setOnClickListener(v ->{
            rbdinheiro.setChecked(false);
            rbdebito.setChecked(false);
            metodoPagamento ="Credito";
        });

        rbdinheiro.performClick();

        btsdata.setOnClickListener(v ->{
            MostraCalendario(new ListenerData(){

                public void data(String data){
                    btsdata.setText(data);
                    dataEntrega = data;
                }

            });
        });

        btsconfirm.setOnClickListener(v ->{
            Log.d("confirma","confirmando");
            loading = new SpotsDialog.Builder().setContext(contexto).setCancelable(false).setMessage("Solicitando cacamba!").build();
            loading.show();

            if(!verCamposVazios()){
                Log.d("confirma2","confirmando2");
                rua = edtrua.getText().toString();
                bairro = edtbairro.getText().toString();
                num = edtnum.getText().toString();
                cidade = edtcidade.getText().toString();
                String local = String.format(Locale.getDefault(),"%s, %s - %s (%s)",rua,num,bairro,cidade);

                cacambas.setStatus("A caminho!");
                cacambas.setLocal(local);
                cacambas.setForma(metodoPagamento);
                cacambas.setPreco(Preco);
                cacambas.setTAM(Tamanho);
                cacambas.setData(dataEntrega);
                cacambas.setCliente(Users.getUsuariologado());

                if(cacambas.getPlaca()!=0){
                    Log.d("confirma3","confirmando3");
                    salvaAluguel(cacambas);
                }
                else{

                    View vg = getLayoutInflater().inflate(R.layout.lista,null);
                    TextView vStatus,vForma,vPreco,vTAM,vData,vLocal;
                    BootstrapButton vConfirm,vCancel;
                    vStatus = vg.findViewById(R.id.txtStatus);
                    vForma = vg.findViewById(R.id.formaPagamento);
                    vPreco = vg.findViewById(R.id.Valor);
                    vTAM = vg.findViewById(R.id.txtTAM);
                    vData = vg.findViewById(R.id.txtDATA);
                    vLocal =vg.findViewById(R.id.txtlocal);
                    vConfirm= vg.findViewById(R.id.btsVerifica_confirm);
                    vCancel = vg.findViewById(R.id.btsVerifica_cancel);

                    vStatus.setText(cacambas.getStatus());
                    vForma.setText(cacambas.getForma());
                    vData.setText(cacambas.getData());
                    vLocal.setText(cacambas.getLocal());
                    vPreco.setText(cacambas.getPreco());
                    vTAM.setText(cacambas.getTAM());

                    AlertDialog confirm = new AlertDialog.Builder(contexto).setView(vg).create();
                    vConfirm.setOnClickListener(v1->{
                        confirm.dismiss();
                        PegaPlaca(cacambas);
                    });

                    vCancel.setOnClickListener(v1 ->{
                        confirm.dismiss();
                        loading.dismiss();
                    });
                    confirm.show();
                }

            }

            else{
                loading.dismiss();
            }
        });

        btscancel.setOnClickListener(v ->{
            finish();
        });


    }

    private void salvaAluguel(Cacambas cacambas){
        String cpf = Users.getUsuariologado().getCpf();
        banco.child("Cacambas").child(cpf).child(String.valueOf(cacambas.getPlaca())).setValue(cacambas);
        loading.dismiss();
        Toast.makeText(contexto, "Aluguel realizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();

    }

    private  void PegaPlaca(Cacambas cacambas){
        banco.child("Cacambas").child("Placa").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int placa;
                if(snapshot.exists()){
                    placa =snapshot.getValue(Integer.class);
                    placa += 1;
                }
                else{
                    placa = 1;
                }

                banco.child("Cacambas").child("Placa").setValue(placa);
                cacambas.setPlaca(placa);
                salvaAluguel(cacambas);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contexto,"Ocorreu um erro tente novamente!",Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }
    private boolean verCamposVazios(){
        boolean seVazio = false;
        if (btsdata.getText().toString().equals("dd/MM/yyyy")){
            Toast.makeText(contexto, "Selecine uma data para a entrega!", Toast.LENGTH_SHORT).show();
            seVazio = true;
        }

        if(edtrua.getText().toString().equals("")){
            Toast.makeText(contexto, "Inserir o nome da rua!", Toast.LENGTH_SHORT).show();
            seVazio=true;
        }

        if(edtnum.getText().toString().equals("")){
            Toast.makeText(contexto, "Inserir o numero do endereço!", Toast.LENGTH_SHORT).show();
            seVazio=true;
        }

        if(edtbairro.getText().toString().equals("")){
            Toast.makeText(contexto, "Inserir o nome do bairro!", Toast.LENGTH_SHORT).show();
            seVazio=true;
        }

        if(edtcidade.getText().toString().equals("")){
            Toast.makeText(contexto, "Inserir o nome da cidade!", Toast.LENGTH_SHORT).show();
            seVazio=true;
        }

        return seVazio;
    }

    //função para fechar o teclado caso esteja aberto ainda
    public void FechaTeclado(){
        View v = Aluga_Activity.this.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(v!=null){
            imm.hideSoftInputFromWindow(v.getWindowToken(),0);
        }
    }

    private void MostraCalendario(ListenerData retorno){

        //So deixa escolher data a partir do dia atual no dispositivo
        CalendarConstraints constraints = new CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build();

        MaterialDatePicker<Long> data =MaterialDatePicker.Builder.datePicker().setTitleText("Selecionar data:").setSelection(new Date().getTime()).setCalendarConstraints(constraints).build();

        data.show(getSupportFragmentManager(),"Data");

        data.addOnPositiveButtonClickListener(selection ->{
            long selecao = selection + (24 * 60 * 60 * 1000);//isso aqui é so pra pro dia ?
            String dataselect = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(selecao));
            retorno.data(dataselect);
        });
    }

    private void iniciaCompenetes(){
        btsdata = findViewById(R.id.bts_data);
        btsconfirm = findViewById(R.id.btsaluga_confirm);
        btscancel = findViewById(R.id.btsaluga_cancel);
        rb_tam4 = findViewById(R.id.rb_tam4);
        rb_tam5 = findViewById(R.id.rb_tam5);
        rb_tam7 = findViewById(R.id.rb_tam7);
        rbdinheiro = findViewById(R.id.rbDinheiro);
        rbcredito = findViewById(R.id.rbCredito);
        rbdebito = findViewById(R.id.rbDebito);
        edtrua = findViewById(R.id.edt_rua);
        edtnum = findViewById(R.id.edt_num);
        edtbairro = findViewById(R.id.edtDeBairro);
        edtcidade = findViewById(R.id.edtCidade);
        txtpreco = findViewById(R.id.txtPreco);
        contexto = Aluga_Activity.this;
        banco = Banco.getBanco();

    }
}