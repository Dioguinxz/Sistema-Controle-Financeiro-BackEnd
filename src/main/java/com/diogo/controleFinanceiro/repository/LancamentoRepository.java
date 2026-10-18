package com.diogo.controleFinanceiro.repository;

import com.diogo.controleFinanceiro.entity.Lancamento;
import com.diogo.controleFinanceiro.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    Optional<Lancamento> findByIdAndUsuarioId(Long id, Long usuarioId);

    boolean existsByCategoriaId(Long categoriaId);

    // Histórico do período, do mais recente para o mais antigo, com filtros opcionais
    @Query("""
            select l from Lancamento l
            where l.usuario.id = :usuarioId
              and l.dataLancamento between :inicio and :fim
              and (:tipo is null or l.tipo = :tipo)
              and (:categoriaId is null or l.categoria.id = :categoriaId)
            order by l.dataLancamento desc, l.id desc
            """)
    List<Lancamento> buscarHistorico(@Param("usuarioId") Long usuarioId,
                                     @Param("inicio") LocalDate inicio,
                                     @Param("fim") LocalDate fim,
                                     @Param("tipo") TipoMovimentacao tipo,
                                     @Param("categoriaId") Long categoriaId);

    @Query("""
            select coalesce(sum(l.valor), 0) from Lancamento l
            where l.usuario.id = :usuarioId and l.tipo = :tipo
              and l.dataLancamento between :inicio and :fim
            """)
    BigDecimal somarPorTipo(@Param("usuarioId") Long usuarioId,
                            @Param("tipo") TipoMovimentacao tipo,
                            @Param("inicio") LocalDate inicio,
                            @Param("fim") LocalDate fim);

    @Query("""
            select l.categoria.nome, sum(l.valor) from Lancamento l
            where l.usuario.id = :usuarioId and l.tipo = com.diogo.controleFinanceiro.enums.TipoMovimentacao.DESPESA
              and l.dataLancamento between :inicio and :fim
            group by l.categoria.id, l.categoria.nome
            order by sum(l.valor) desc
            """)
    List<Object[]> somarDespesasPorCategoria(@Param("usuarioId") Long usuarioId,
                                             @Param("inicio") LocalDate inicio,
                                             @Param("fim") LocalDate fim);
}
