package com.app.caambas.Class;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

public class Users {
    public static Users usuariologado;
    String nome;
    String cpf;
    String cidade;

    public static void setUsuariologado(Users usuario){
        usuariologado = usuario;
    }

    public static Users getUsuariologado(){
        return usuariologado;
    }
}
