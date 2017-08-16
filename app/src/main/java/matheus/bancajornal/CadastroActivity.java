package matheus.bancajornal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
//import android.R;


/**
 * Created by matheusnno on 23/06/16.
 */
public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner tipo;
    private Button cadastrar;
    private EditText nome, email, telefone, usuario, senha;

    UsuarioDAO db = new UsuarioDAO(CadastroActivity.this);
    Usuario existe = new Usuario();

    String[] types = new String[]{"Dono", "Funcionário"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_activity);

        tipo = (Spinner)findViewById(R.id.tipo_usuario);
        nome = (EditText)findViewById(R.id.nomeCadastro);
        email = (EditText)findViewById(R.id.emailCadastro);
        telefone = (EditText)findViewById(R.id.telefoneCadastro);
        usuario = (EditText)findViewById(R.id.usuarioCadastro);
        senha = (EditText)findViewById(R.id.senhaCadastro);
        cadastrar = (Button)findViewById(R.id.BtnCadastrar);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        tipo.setAdapter(adapter);
        cadastrar.setOnClickListener(CadastroActivity.this);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Deseja voltar para a tela de login?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent;
                        intent = new Intent(CadastroActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnCadastrar){
            if (!nome.getText().toString().equals("") && !email.getText().toString().equals("")
                    && !telefone.getText().toString().equals("") && !usuario.getText().toString().equals("")
                    && !senha.getText().toString().equals("")){

                existe = db.buscarUsuario(usuario.getText().toString());
                if (existe.getUsuario() != null){
                    Toast.makeText(CadastroActivity.this, "Esse usuário já existe !", Toast.LENGTH_SHORT).show();
                }
                else {
                    Usuario cad = new Usuario();
                    cad.setNome(nome.getText().toString());
                    cad.setEmail(email.getText().toString());
                    cad.setTelefone(telefone.getText().toString());
                    cad.setUsuario(usuario.getText().toString());
                    cad.setSenha(senha.getText().toString());
                    cad.setTipo(types[(int)tipo.getSelectedItemId()].toLowerCase());

                    db.inserirUsuario(cad);
                    Toast.makeText(CadastroActivity.this, "Usuário " + cad.getNome() + " cadastrado com sucesso !"
                                    + types[(int)tipo.getSelectedItemId()],
                            Toast.LENGTH_SHORT).show();
                    nome.setText("");
                    email.setText("");
                    telefone.setText("");
                    usuario.setText("");
                    senha.setText("");
                }

            }
            else
                Toast.makeText(CadastroActivity.this, "Favor preencher todos os dados!", Toast.LENGTH_SHORT).show();
        }
    }
}
