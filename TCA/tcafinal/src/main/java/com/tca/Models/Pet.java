package com.tca.Models;

public class Pet {
    private String nome;
    private String raca;
    private String tipo;
    private int idade;
    private String sexo;

    // Construtor ajustado para aceitar os parâmetros
    public Pet(String nome, String raca, String tipo, int idade, String sexo) {
        this.nome = nome;
        this.raca = raca;
        this.tipo = tipo;
        this.idade = idade;
        this.sexo = sexo;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
