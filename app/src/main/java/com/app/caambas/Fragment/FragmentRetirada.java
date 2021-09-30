package com.app.caambas.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.caambas.Adapter.AdapterCacamba;
import com.app.caambas.BD.Banco;
import com.app.caambas.Class.Cacambas;
import com.app.caambas.Class.Users;
import com.app.caambas.Listener.ListenerAdapter;
import com.app.caambas.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class FragmentRetirada extends Fragment {

    private View view;
    private RecyclerView recycler;
    private AdapterCacamba adapter;
    private List<Cacambas> listas;
    private DatabaseReference banco;
    private Context contexto;

    public FragmentRetirada(){
        super(R.layout.fragment_list);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list,container,false);
        iniciaCompenetes();
        iniciaRecycler();
        carregaCacambas();
        return view;
    }

    private void carregaCacambas() {
        AlertDialog loading = new SpotsDialog.Builder()
                .setContext(contexto)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();

        String cpfUsuario = Users.getUsuariologado().getCpf();
        listas.clear();
        loading.show();
        banco.child("Cacambas").child(cpfUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Cacambas cacambas = ds.getValue(Cacambas.class);
                        listas.add(cacambas);
                    }

                    if (listas.size() > 0) {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(contexto, "Não foi encontrado nenhum aluguel", Toast.LENGTH_SHORT).show();
                }
                loading.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(contexto, "Ocorreu um erro desconhecido", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }

    private void iniciaRecycler() {

        adapter = new AdapterCacamba(listas, new ListenerAdapter() {
            @Override
            //não tem função de clicar
            public void onClick(int posicao) {
                Cacambas cacamba = listas.get(posicao);
                AlertDialog dialog = new AlertDialog.Builder(contexto).setTitle("Confirmar retirada").setMessage("Deseja solicitar retirada?")
                        .setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(cacamba.getStatus().equals("Entregue")){
                                    SolicitarRetirada(cacamba);
                                    Toast.makeText(contexto, "Solicitação realizada", Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(contexto, "Não e possível solicitar a retirada", Toast.LENGTH_SHORT).show();

                                }
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();
                dialog.show();
            }
        });
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(contexto));

    }

    private void SolicitarRetirada(Cacambas cacamba) {
        cacamba.setStatus("Retirada Solicitada");
        banco.child("Cacambas").child(Users.getUsuariologado().getCpf()).child(String.valueOf(cacamba.getPlaca())).setValue(cacamba);
        requireActivity().finish();

    }

    private void iniciaCompenetes() {
        contexto = requireContext();
        recycler = view.findViewById(R.id.listaRecy);
        banco = Banco.getBanco();
        listas = new ArrayList<>();
    }
}
