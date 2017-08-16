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
public class UsuarioDAO extends SQLiteOpenHelper {

    //Caracteristicas da tabela do banco
    private static final int VERSAO = 1;
    private static final String TABELA = "Usuarios";
    private static final String DATABASE = "User_Cadastrados";

    public UsuarioDAO(Context context){
        super(context, DATABASE, null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String ddl = "CREATE TABLE " + TABELA
                + " (id INTEGER PRIMARY KEY, "
                + " nome TEXT NOT NULL,"
                + " email TEXT,"
                + " telefone TEXT,"
                + " tipo TEXT,"
                + " usuario TEXT,"
                + " senha TEXT);";
        db.execSQL(ddl);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String ddl = "DROP TABLE IF EXISTS " + TABELA;

        db.execSQL(ddl);
        onCreate(db);
    }

    public void inserirUsuario(Usuario usuario){
        //Cria uma tabela, como um dicionário em Python para os valores
        ContentValues values = new ContentValues();

        values.put("nome", usuario.getNome());
        values.put("telefone", usuario.getTelefone());
        values.put("email", usuario.getEmail());
        values.put("tipo", usuario.getTipo());
        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());

        //Pede uma versão do banco que pode ser escrita
        getWritableDatabase().insert(TABELA, null, values);
    }

    //Método para recuperar os dados do banco
    public List<Usuario> getLista(){
        //Cria uma lista para armazenar os dados da resposta
        List<Usuario> usuarios = new ArrayList<Usuario>();

        //Cria um objeto Cursor para navegar no banco de dados
        Cursor c = getReadableDatabase().
                rawQuery("SELECT * FROM "+TABELA+";", null);

        //Coloca o cursor na primeira posicao
        c.moveToFirst();

        //Enquanto ainda existirem alunos, recuperar os dados
        for (int i = 0; i < c.getCount(); i++){
            Usuario temp = new Usuario();
            temp.setId(c.getLong(c.getColumnIndex("id")));
            temp.setNome(c.getString(c.getColumnIndex("nome")));
            temp.setTelefone(c.getString(c.getColumnIndex("telefone")));
            temp.setEmail(c.getString(c.getColumnIndex("email")));
            c.moveToNext();
            //Adicionar o usuário temporario a lista
            usuarios.add(temp);
        }

        //Fecha o cursor
        c.close();
        return usuarios;
    }

    //Deletar um usuário
    public void deletar(Usuario usuario){
        String [] args = {usuario.getId().toString()};
        getWritableDatabase().delete(TABELA, "id=?", args);
    }

    //Busca um único usuário
    public Usuario buscarUsuario(String teste){
        Usuario usuario = new Usuario();

        //Busca o usuário fornecido no banco
        String [] args = {teste};
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+ TABELA+ " WHERE usuario=?;", args);
        c.moveToFirst();
        if (!(c.getCount() == 0)) {
            usuario.setId(c.getLong(c.getColumnIndex("id")));
            usuario.setNome(c.getString(c.getColumnIndex("nome")));
            usuario.setTelefone(c.getString(c.getColumnIndex("telefone")));
            usuario.setEmail(c.getString(c.getColumnIndex("email")));
            usuario.setUsuario(c.getString(c.getColumnIndex("usuario")));
            usuario.setSenha(c.getString(c.getColumnIndex("senha")));
            usuario.setTipo(c.getString(c.getColumnIndex("tipo")));

            c.close();

            return usuario;
        }
        return usuario;
    }

    //Altera o registro de um usuário
    public void alterar(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("telefone", usuario.getTelefone());
        values.put("email", usuario.getEmail());

        getWritableDatabase().update(TABELA, values, "id=?",
                new String[] {usuario.getId().toString()});
    }
}


