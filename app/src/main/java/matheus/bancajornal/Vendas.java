package matheus.bancajornal;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mno on 10/06/16.
 */
public class Vendas implements Serializable {
    
        private Long id;
        private String cod_produto;
        private int cod_venda;
        private String categoria;
        private float preco_individual;
        private float preco_total;
        private int quantidade;
        private String DataVenda;
        private float preco_compra;

    public float getPreco_compra() {
        return preco_compra;
    }

    public void setPreco_compra(float preco_compra) {
        this.preco_compra = preco_compra;
    }

    public String getDataVenda() {
        return DataVenda;
    }

    public void setDataVenda(String DataVenda) {
        this.DataVenda = DataVenda;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCod_produto() {
        return cod_produto;
    }

    public void setCod_produto(String cod_produto) {
        this.cod_produto = cod_produto;
    }

    public int getCod_venda() {
        return cod_venda;
    }

    public void setCod_venda(int cod_venda) {
        this.cod_venda = cod_venda;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public float getPreco_individual() {
        return preco_individual;
    }

    public void setPreco_individual(float preco_individual) {
        this.preco_individual = preco_individual;
    }

    public float getPreco_total() {
        return preco_total;
    }

    public void setPreco_total(float preco_total) {
        this.preco_total = preco_total;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
