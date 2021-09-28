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

public class Cacambas {
    public static Cacambas cacambasAlugadas;
    String Valor;
    String Forma;
    String Local;
    String Status;
    String Preco;
    String Data;
    String TAM;
    int Placa=0;
    private Users Cliente;

    public  static void setCacambasAlugadas(Cacambas cacambas){
        cacambasAlugadas = cacambas;
    }

    public static Cacambas getCacambasAlugadas(){
        return cacambasAlugadas;
    }
}
