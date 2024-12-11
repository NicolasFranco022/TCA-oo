package com.tca.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Agendamento {
    private Pet pet;
    private String motivoConsulta;
    private LocalDateTime dataAgendamento;

    // Construtor com LocalDateTime
    public Agendamento(Pet pet, LocalDateTime dataHora, String motivoConsulta) {
        this.pet = pet;
        this.dataAgendamento = dataHora; // Corrigido: Atribuindo dataHora corretamente
        this.motivoConsulta = motivoConsulta;
    }

    // Construtor com Dono, Pet, LocalDate e motivo
    public Agendamento(Dono dono, Pet pet, LocalDate data, String motivoConsulta) {
        this.pet = pet;
        // Converte o LocalDate para LocalDateTime (com hora, minuto e segundo definidos para 00:00)
        this.dataAgendamento = data.atStartOfDay(); // Conversão de LocalDate para LocalDateTime
        this.motivoConsulta = motivoConsulta;
    }

    // Getters e Setters
    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDateTime dataHora) {
        this.dataAgendamento = dataHora; // Corrigido: agora usando o parâmetro correto
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    @Override
    public String toString() {
        return "Agendamento [pet=" + pet.getNome() + ", dataAgendamento=" + dataAgendamento + ", motivoConsulta=" + motivoConsulta + "]";
    }
}
