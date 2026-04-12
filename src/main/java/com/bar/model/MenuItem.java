package com.bar.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private Long id;
    private String nome;
    private Double prezzo;
    private String categoria;
}
