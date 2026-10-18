package com.diogo.controleFinanceiro.repository;

import com.diogo.controleFinanceiro.entity.Categoria;
import com.diogo.controleFinanceiro.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    boolean existsByPadraoTrueAndTipoAndNomeIgnoreCase(TipoMovimentacao tipo, String nome);

    boolean existsByUsuarioIdAndTipoAndNomeIgnoreCase(Long usuarioId, TipoMovimentacao tipo, String nome);

    boolean existsByUsuarioIdAndTipoAndNomeIgnoreCaseAndIdNot(Long usuarioId, TipoMovimentacao tipo, String nome, Long id);

    // Categorias acessíveis: padrão (compartilhadas) ou personalizadas do usuário
    @Query("""
            select c from Categoria c
            where c.padrao = true or c.usuario.id = :usuarioId
            order by c.tipo, c.nome
            """)
    List<Categoria> findAcessiveis(@Param("usuarioId") Long usuarioId);

    @Query("""
            select c from Categoria c
            where c.id = :id and (c.padrao = true or c.usuario.id = :usuarioId)
            """)
    Optional<Categoria> findAcessivelById(@Param("id") Long id, @Param("usuarioId") Long usuarioId);

    Optional<Categoria> findByIdAndUsuarioId(Long id, Long usuarioId);
}
