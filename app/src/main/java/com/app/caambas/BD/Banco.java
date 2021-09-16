package com.app.caambas.BD;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.content.Context;

public class Banco {

    private static FirebaseAuth autenticacao = FirebaseAuth.getInstance();
    private static FirebaseDatabase banco = FirebaseDatabase.getInstance();

    public static DatabaseReference getBanco(){
        return banco.getReference();
    }

    public static FirebaseUser getUsuarioLogado(){
        return autenticacao.getCurrentUser();
    }

    public static FirebaseAuth getAutenticacao(){
        return autenticacao;
    }

    public static boolean temUsuarioLogado(){
        return(autenticacao.getCurrentUser()!=null);
    }

    public static String getIdUsuarioLogado() {
        if (temUsuarioLogado()) {
            return autenticacao.getCurrentUser().getUid();
        }
      return null;
    }

}
