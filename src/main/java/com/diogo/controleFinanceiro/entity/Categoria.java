package com.diogo.controleFinanceiro.entity;

import com.diogo.controleFinanceiro.enums.TipoMovimentacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nulo para categorias padrão, compartilhadas entre todos os usuários
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    @Column(length = 255)
    private String descricao;

    @NotNull(message = "O tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimentacao tipo;

    @Column(nullable = false)
    private boolean padrao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void antesDePersistir() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
