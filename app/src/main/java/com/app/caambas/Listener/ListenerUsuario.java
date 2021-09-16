package com.app.caambas.Listener;

import com.google.firebase.firestore.auth.User;

public interface ListenerUsuario {
    void sucesso(User usuario);
}
