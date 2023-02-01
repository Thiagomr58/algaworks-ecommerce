package com.algaworks.ecommerce.iniciandocomjpa;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.model.Produto;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @author Thiago Rodrigues
 * @version 1.0.0
 * @since 31/01/2023
 */
public class OperacoesComTransacaoTest extends EntityManagerTest {

    @Test
    public void inserirOPrimeiroObjeto() {
        Produto produto = new Produto();
        produto.setId(2);
        produto.setNome("Câmera Canon");
        produto.setDescricao("A melhor definição para suas fotos.");
        produto.setPreco(new BigDecimal(5000));

        entityManager.getTransaction().begin();
        entityManager.persist(produto);
        // entityManager.flush(); // força o que está na memoria do EM e joga para o BD
        entityManager.getTransaction().commit();

        entityManager.clear(); // limpo pois o EM salva o registro em memoria, como eu faço o .clear() eu forço ele a ir no BD

        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoVerificado);
    }

    @Test
    public void abrirEFecharATransacao() {
        Produto produto = new Produto(); // somente para o metodo não mostrar erro
        entityManager.getTransaction().begin(); // metodo que inicia a transação
        // Aqui dentro da transação eu realizo as operação no BD


        entityManager.persist(produto);
        entityManager.merge(produto);
        entityManager.remove(produto);

        entityManager.getTransaction().commit(); // comitar a transação. finalizar ela

    }


}
