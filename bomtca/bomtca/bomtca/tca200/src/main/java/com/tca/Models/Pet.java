package com.tca.Models;

public class Pet {
    private int id;
    private String nome;
    private String raca;
    private String tipo;
    private int idade;
    private String sexo;
    private Dono dono;

    public Pet() {}

    public Pet(int id, String nome, String raca, String tipo, int idade, String sexo, Dono dono) {
        this.id = id;
        this.nome = nome;
        this.raca = raca;
        this.tipo = tipo;
        this.idade = idade;
        this.sexo = sexo;
        this.dono = dono;
    }

    public Pet(String nome, String raca, String tipo, int idade, String sexo, Dono dono) {
        this.nome = nome;
        this.raca = raca;
        this.tipo = tipo;
        this.idade = idade;
        this.sexo = sexo;
        this.dono = dono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Dono getDono() {
        return dono;
    }

    public void setDono(Dono dono) {
        this.dono = dono;
    }
}