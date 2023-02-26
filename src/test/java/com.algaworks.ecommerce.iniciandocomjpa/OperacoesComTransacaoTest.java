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
    public void mostrarDiferencaPersistMerge() {
        // Persist: Apenas inserção
        // Merge: Funciona das duas formas: Inserção/Atualização

        Produto produtoPersist = new Produto();

        produtoPersist.setId(5);
        produtoPersist.setNome("Smartphone One Plus");
        produtoPersist.setDescricao("O processador mais rapido.");
        produtoPersist.setPreco(new BigDecimal(2000));

        entityManager.getTransaction().begin();
        entityManager.persist(produtoPersist);
        produtoPersist.setNome("Smartphone Two Plus");
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacaoPersist = entityManager.find(Produto.class, produtoPersist.getId());
        Assert.assertNotNull(produtoVerificacaoPersist);

        Produto produtoMerge = new Produto();

        produtoMerge.setId(6);
        produtoMerge.setNome("Notebook Dell");
        produtoMerge.setDescricao("O melhor da categoria.");
        produtoMerge.setPreco(new BigDecimal(4000));

        entityManager.getTransaction().begin();

        // o merge gera um copia do obj e joga dentro do entityManager, ai, se eu atualizar o nome, não vai funcionar pq o set não é uma instancia gerenciada
        // entityManager.merge(produtoMerge);
        // produtoMerge.setNome("Notebook Dell Vostro ");

        // Pra funcionar eu faço o merge e recebo a copia do obj e dai sim eu consigo alterar o nome com o set.
        produtoMerge = entityManager.merge(produtoMerge);
        produtoMerge.setNome("Notebook Dell 2");

        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacaoMerge = entityManager.find(Produto.class, produtoMerge.getId());
        Assert.assertNotNull(produtoVerificacaoMerge);
    }

    @Test
    public void inserirObjetoComMerge() {
        Produto produto = new Produto();

        produto.setId(4);
        produto.setNome("Microfone Rode Videmio");
        produto.setDescricao("A melhor qualidade de som.");
        produto.setPreco(new BigDecimal(1000));

        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear(); // limpo pois o EM salva o registro em memoria, como eu faço o .clear() eu forço ele a ir no BD

        Produto produtoVerificado = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoVerificado);
    }

    @Test
    public void atualizarObjetoGerenciado() {
        Produto produto = entityManager.find(Produto.class, 1);

        entityManager.getTransaction().begin();
        produto.setNome("Kindle Paperwhite 2ª Geração");
        entityManager.getTransaction().commit();

        entityManager.clear();

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assert.assertEquals("Kindle Paperwhite 2ª Geração", produtoVerificacao.getNome());
    }

    @Test
    public void atualizarObjeto() {
        // Dessa forma o EM exige que todos os dados estejam preenchidos, caso um deles não esteja setado, o EM vai entender que vc quer setar null no valor do atributo que não foi preenchido.
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Kindle Paperwhite");
        produto.setDescricao("Conheça o novo Kindle.");
        produto.setPreco(new BigDecimal(599));

        // No metodo de merge, não tem a necesidade de begin/commit a transação
        // funciona pq o EM joga o obj na memoria e quando o EM percebe que tem um dado na memoria, ele joga para o BD
        entityManager.getTransaction().begin();
        entityManager.merge(produto);
        entityManager.getTransaction().commit();

        entityManager.clear(); // limpo pois o EM salva o registro em memoria, como eu faço o .clear() eu forço ele a ir no BD

        Produto produtoVerificacao = entityManager.find(Produto.class, produto.getId());
        Assert.assertNotNull(produtoVerificacao);
        Assert.assertEquals("Kindle Paperwhite", produtoVerificacao.getNome());
    }

    @Test
    public void removerObjeto() {
        Produto produto = entityManager.find(Produto.class, 3);

        entityManager.getTransaction().begin(); // No metodo de remover, não tem a necesidade de begin/commit a transação
        entityManager.remove(produto);
        entityManager.getTransaction().commit();

        // entityManager.clear(); Não é necessário na asserção para operação de remoção.

        Produto produtoVerificacao = entityManager.find(Produto.class, 3);
        Assert.assertNull(produtoVerificacao);
    }

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
