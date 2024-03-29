package com.be.two.c.apibetwoc.repository;

import com.be.two.c.apibetwoc.model.ProdutoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoTagRepository extends JpaRepository<ProdutoTag, Long> {
    @Query("SELECT pt FROM ProdutoTag pt WHERE pt.produto.id =:id")
    List<ProdutoTag> buscarPorProduto(Long id);

    void deleteByIdIn(List<Long> idTag);

    void deleteByProdutoId(Long id);

    List<ProdutoTag> findByProdutoId(Long id);
    List<ProdutoTag> findByTagDescricaoIgnoreCase(String nome);

    void deleteAllByProdutoId(Long id);
}
