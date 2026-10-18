package com.diogo.controleFinanceiro.entity;

import com.diogo.controleFinanceiro.enums.FormaPagamento;
import com.diogo.controleFinanceiro.enums.TipoMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lancamentos")
@Getter
@Setter
@NoArgsConstructor
public class Lancamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @NotNull(message = "O tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 150, message = "A descrição deve ter no máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2,
            message = "O valor deve ter no máximo duas casas decimais")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "A data é obrigatória")
    @PastOrPresent(message = "Não é permitido lançamento com data futura")
    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    // Obrigatória para despesas e nula para receitas (validado no serviço)
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", length = 30)
    private FormaPagamento formaPagamento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void antesDePersistir() {
        LocalDateTime agora = LocalDateTime.now();
        if (dataCriacao == null) {
            dataCriacao = agora;
        }
        dataAtualizacao = agora;
    }

    @PreUpdate
    protected void antesDeAtualizar() {
        dataAtualizacao = LocalDateTime.now();
    }
}
