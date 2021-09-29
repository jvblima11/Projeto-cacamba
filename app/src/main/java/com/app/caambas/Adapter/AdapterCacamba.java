package com.app.caambas.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.caambas.Class.Cacambas;
import com.app.caambas.Listener.ListenerAdapter;
import com.app.caambas.R;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AdapterCacamba extends RecyclerView.Adapter<AdapterCacamba.ViewHolder> {
    private List<Cacambas> lista;
    private ListenerAdapter retorno;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_verifica,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cacambas c = lista.get(position);
        holder.txtPlaca.setText("Placa: "+String.valueOf(c.getPlaca()));
        holder.vData.setText("Data de entrega: "+(c.getData()));
        holder.vForma.setText("Pagamento em: "+(c.getForma()));
        holder.vLocal.setText("Local: "+(c.getLocal()));
        holder.vPreco.setText("Valor: "+(c.getPreco()));
        holder.vStatus.setText("Status: "+(c.getStatus()));
        holder.vTAM.setText("Tamanho: "+(c.getTAM()));

        holder.itemView.setOnClickListener(v ->{
            retorno.onClick(position);
        });

    }

    @Override
    public int getItemCount() {

        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView vStatus,vForma,vPreco,vTAM,vData,vLocal,txtPlaca;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            vStatus = itemView.findViewById(R.id.txtStatus);
            vForma = itemView.findViewById(R.id.formaPagamento);
            vPreco = itemView.findViewById(R.id.Valor);
            vTAM = itemView.findViewById(R.id.txtTAM);
            vData = itemView.findViewById(R.id.txtDATA);
            vLocal =itemView.findViewById(R.id.txtlocal);
            txtPlaca = itemView.findViewById(R.id.txtplaca);
        }
    }
}
