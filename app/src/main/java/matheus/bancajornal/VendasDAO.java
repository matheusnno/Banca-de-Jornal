package matheus.bancajornal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheusnno on 17/06/2016.
 */
public class VendasDAO extends SQLiteOpenHelper {

    //Caracteristicas da tabela do banco
    private static final int VERSAO = 2;
    private static final String TABELA = "Vendas";
    private static final String DATABASE = "Vendas_Cadastrados";

    public VendasDAO(Context context){
        super(context, DATABASE, null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE " + TABELA
                + " (id INTEGER PRIMARY KEY, "
                + " cod_produto TEXT NOT NULL, "
                + " cod_venda INTEGER NOT NULL, "
                + " categoria TEXT, "
                + " preco_individual FLOAT, " +
                  " preco_compra FLOAT, " +
                  " preco_total FLOAT, " +
                  " data_venda TEXT, "
                + " quantidade INTEGER);";
        db.execSQL(ddl);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS " + TABELA;

        db.execSQL(ddl);
        onCreate(db);
    }

    public void inserirVenda(Vendas vendas){
        //Cria uma tabela, como um dicionário em Python para os valores
        ContentValues values = new ContentValues();
        values.put("cod_produto", vendas.getCod_produto());
        values.put("cod_venda", vendas.getCod_venda());
        values.put("categoria", vendas.getCategoria());
        values.put("preco_individual", vendas.getPreco_individual());
        values.put("preco_total", vendas.getPreco_total());
        values.put("quantidade", vendas.getQuantidade());
        values.put("data_venda", vendas.getDataVenda());
        values.put("preco_compra", vendas.getPreco_compra());

        //Pede uma versão do banco que pode ser escrita
        getWritableDatabase().insert(TABELA, null, values);
    }

    //Método para recuperar os dados do banco
    public List<Vendas> getLista(){
        //Cria uma lista para armazenar os dados da resposta
        List<Vendas> vendas = new ArrayList<Vendas>();

        //Cria um objeto Cursor para navegar no banco de dados
        Cursor c = getReadableDatabase().
                rawQuery("SELECT * FROM "+TABELA+";", null);

        //Coloca o cursor na primeira posicao
        c.moveToFirst();

        //Enquanto ainda existirem alunos, recuperar os dados
        for (int i = 0; i < c.getCount(); i++){
            Vendas temp = new Vendas();
            temp.setId(c.getLong(c.getColumnIndex("id")));
            temp.setCod_produto(c.getString(c.getColumnIndex("cod_produto")));
            temp.setCod_venda(Integer.parseInt(c.getString(c.getColumnIndex("cod_venda"))));
            temp.setCategoria(c.getString(c.getColumnIndex("categoria")));
            temp.setPreco_individual(Float.parseFloat(c.getString(c.getColumnIndex("preco_individual"))));
            temp.setPreco_total(Float.parseFloat(c.getString((c.getColumnIndex("preco_total")))));
            temp.setQuantidade(Integer.parseInt(c.getString(c.getColumnIndex("quantidade"))));
            temp.setDataVenda(c.getString(c.getColumnIndex("data_venda")));
            temp.setPreco_compra(Float.parseFloat(c.getString(c.getColumnIndex("preco_compra"))));
            c.moveToNext();
            //Adicionar o usuário temporario a lista
            vendas.add(temp);
        }

        //Fecha o cursor
        c.close();
        return vendas;
    }

    //Deletar um usuário
    public void deletar(Vendas vendas){
        String [] args = {vendas.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    public List<Vendas> buscarVenda(String teste){
        //Busca a venda fornecida no banco
        String [] args = {teste};
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+ TABELA+ " WHERE cod_venda=?;", args);
        c.moveToFirst();
        List<Vendas> v = new ArrayList<Vendas>();

        for (int i = 0; i < c.getCount(); i++){
            Vendas vendas = new Vendas();
            vendas.setId(c.getLong(c.getColumnIndex("id")));
            vendas.setCod_produto(c.getString(c.getColumnIndex("cod_produto")));
            vendas.setCod_venda(Integer.parseInt(c.getString(c.getColumnIndex("cod_venda"))));
            vendas.setCategoria(c.getString(c.getColumnIndex("categoria")));
            vendas.setPreco_individual(Float.parseFloat(c.getString(c.getColumnIndex("preco_individual"))));
            vendas.setPreco_total(Float.parseFloat(c.getString(c.getColumnIndex("preco_total"))));
            vendas.setQuantidade(Integer.parseInt(c.getString(c.getColumnIndex("quantidade"))));
            vendas.setDataVenda(c.getString(c.getColumnIndex("data_venda")));
            vendas.setPreco_compra(Float.parseFloat(c.getString(c.getColumnIndex("preco_compra"))));
            v.add(vendas);
            c.moveToNext();
        }
        return v;
    }

    //Altera o registro de um usuário
    public void alterar(Vendas vendas){
        ContentValues values = new ContentValues();
        values.put("cod_produto", vendas.getCod_produto());
        values.put("cod_venda", vendas.getCod_venda());
        values.put("categoria", vendas.getCategoria());
        values.put("preco_individual", vendas.getPreco_individual());
        values.put("preco_total", vendas.getPreco_total());
        values.put("quantidade", vendas.getQuantidade());
        values.put("data_venda", vendas.getDataVenda());
        values.put("preco_compra", vendas.getPreco_compra());

        getWritableDatabase().update(TABELA, values, "id=?",
                new String[] {vendas.getId().toString()});
    }

    public int maxCodVendas(){
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+";", null);
        c.moveToLast();
        if (c.getCount() > 0) {
            return Integer.parseInt(c.getString(c.getColumnIndex("cod_venda")));
        }
        else return 0;
    }
}


