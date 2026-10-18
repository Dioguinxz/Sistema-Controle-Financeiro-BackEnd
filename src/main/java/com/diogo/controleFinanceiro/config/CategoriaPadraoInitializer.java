package com.diogo.controleFinanceiro.config;

import com.diogo.controleFinanceiro.entity.Categoria;
import com.diogo.controleFinanceiro.enums.TipoMovimentacao;
import com.diogo.controleFinanceiro.repository.CategoriaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoriaPadraoInitializer implements ApplicationRunner {

    private static final List<String> RECEITAS =
            List.of("Salário", "Renda extra", "Investimentos", "Vendas");

    private static final List<String> DESPESAS =
            List.of("Moradia", "Alimentação", "Transporte", "Saúde", "Educação", "Lazer", "Assinaturas");

    private final CategoriaRepository categoriaRepository;

    public CategoriaPadraoInitializer(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        RECEITAS.forEach(nome -> criarSeAusente(nome, TipoMovimentacao.RECEITA));
        DESPESAS.forEach(nome -> criarSeAusente(nome, TipoMovimentacao.DESPESA));
    }

    private void criarSeAusente(String nome, TipoMovimentacao tipo) {
        if (categoriaRepository.existsByPadraoTrueAndTipoAndNomeIgnoreCase(tipo, nome)) {
            return;
        }
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoria.setTipo(tipo);
        categoria.setPadrao(true);
        categoriaRepository.save(categoria);
    }
}
