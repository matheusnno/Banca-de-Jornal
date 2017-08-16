package matheus.bancajornal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by matheusnno on 17/06/2016.
 */
public class ProdutosDAO extends SQLiteOpenHelper {

    //Caracteristicas da tabela do banco
    private static final int VERSAO = 1;
    private static final String TABELA = "Produtos";
    private static final String DATABASE = "Prod_Cadastrados";

    public ProdutosDAO(Context context){
        super(context, DATABASE, null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE " + TABELA
                + " (id INTEGER PRIMARY KEY, "
                + " cod TEXT NOT NULL, "
                + " categoria TEXT, "
                + " preco_venda REAL, "
                + " preco_compra REAL, "
                + " estoque INT, "
                + " nome TEXT);";

        db.execSQL(ddl);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS " + TABELA;

        db.execSQL(ddl);
        onCreate(db);
    }

    public void inserirProduto(Produtos produtos){
        //Cria uma tabela, como um dicionário em Python para os valores
        ContentValues values = new ContentValues();

        values.put("cod", produtos.getCod());
        values.put("categoria", produtos.getCategoria());
        values.put("preco_venda", produtos.getPreco_venda());
        values.put("preco_compra", produtos.getPreco_compra());
        values.put("estoque", produtos.getEstoque());
        values.put("nome", produtos.getNome());

        //Pede uma versão do banco que pode ser escrita
        getWritableDatabase().insert(TABELA, null, values);
    }

    //Método para recuperar os dados do banco
    public List<Produtos> getLista(){
        //Cria uma lista para armazenar os dados da resposta
        List<Produtos> produtos = new ArrayList<Produtos>();

        //Cria um objeto Cursor para navegar no banco de dados
        Cursor c = getReadableDatabase().
                rawQuery("SELECT * FROM "+TABELA+";", null);

        //Coloca o cursor na primeira posicao
        c.moveToFirst();

        //Enquanto ainda existirem alunos, recuperar os dados
        for (int i = 0; i < c.getCount(); i++){
            Produtos temp = new Produtos();
            temp.setId(c.getLong(c.getColumnIndex("id")));
            temp.setCod(c.getString(c.getColumnIndex("cod")));
            temp.setCategoria(c.getString(c.getColumnIndex("categoria")));
            temp.setPreco_compra(Float.parseFloat((c.getString(c.getColumnIndex("preco_compra")))));
            temp.setPreco_venda(Float.parseFloat(c.getString((c.getColumnIndex("preco_venda")))));
            temp.setEstoque(Integer.parseInt(c.getString(c.getColumnIndex("estoque"))));
            temp.setNome(c.getString(c.getColumnIndex("nome")));
            c.moveToNext();
            //Adicionar o usuário temporario a lista
            produtos.add(temp);
        }

        //Fecha o cursor
        c.close();
        return produtos;
    }

    //Deletar um usuário
    public void deletar(Produtos produtos){
        String [] args = {produtos.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    //Busca um único usuário
    public Produtos buscarProdutos(String teste){
        Produtos produtos = new Produtos();

        //Busca o produto fornecido no banco
        String [] args = {teste};
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+ TABELA+ " WHERE cod=?;", args);
        c.moveToFirst();
        if (!(c.getCount() == 0)) {
            produtos.setId(c.getLong(c.getColumnIndex("id")));
            produtos.setCod(c.getString(c.getColumnIndex("cod")));
            produtos.setCategoria(c.getString(c.getColumnIndex("categoria")));
            produtos.setPreco_venda(Float.parseFloat(c.getString(c.getColumnIndex("preco_venda"))));
            produtos.setPreco_compra(Float.parseFloat((c.getString(c.getColumnIndex("preco_compra")))));
            produtos.setEstoque(Integer.parseInt(c.getString(c.getColumnIndex("estoque"))));
            produtos.setNome(c.getString(c.getColumnIndex("nome")));

            c.close();

            return produtos;
        }
        return produtos;
    }

    public void acrescentarEstoque(Produtos produtos){
        ContentValues values = new ContentValues();
        values.put("cod", produtos.getCod());
        values.put("categoria", produtos.getCategoria());
        values.put("preco_venda", produtos.getPreco_venda());
        values.put("preco_compra", produtos.getPreco_compra());
        values.put("estoque", produtos.getEstoque());
        values.put("nome", produtos.getNome());

        getWritableDatabase().update(TABELA, values, "cod=?",
                new String[] {produtos.getCod().toString()});
    }
}


