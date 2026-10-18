package com.diogo.controleFinanceiro.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 100,
            message = "O nome deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Informe um e-mail válido")
    @Size(max = 150, message = "O e-mail é muito longo")
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void antesDePersistir() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
    }
}
