package matheus.bancajornal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by matheusnno on 23/06/16.
 */
public class CadastroFuncActivity extends AppCompatActivity implements View.OnClickListener {
    private Button cadastrar, duvida;
    private EditText nome, email, telefone, usuario, senha;

    UsuarioDAO db = new UsuarioDAO(CadastroFuncActivity.this);
    Usuario existe = new Usuario();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_activity_funcionario);

        nome = (EditText) findViewById(R.id.nomeCadastro);
        email = (EditText) findViewById(R.id.emailCadastro);
        telefone = (EditText) findViewById(R.id.telefoneCadastro);
        usuario = (EditText) findViewById(R.id.usuarioCadastro);
        senha = (EditText) findViewById(R.id.senhaCadastro);
        cadastrar = (Button) findViewById(R.id.BtnCadastrar);
        duvida = (Button) findViewById(R.id.button2);

        cadastrar.setOnClickListener(CadastroFuncActivity.this);
        duvida.setOnClickListener(CadastroFuncActivity.this);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Deseja voltar para a tela de login?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent;
                        intent = new Intent(CadastroFuncActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnCadastrar) {
            if (!nome.getText().toString().equals("") && !email.getText().toString().equals("")
                    && !telefone.getText().toString().equals("") && !usuario.getText().toString().equals("")
                    && !senha.getText().toString().equals("")) {

                existe = db.buscarUsuario(usuario.getText().toString());
                if (existe.getUsuario() != null) {
                    Toast.makeText(CadastroFuncActivity.this, "Esse usuário já existe !", Toast.LENGTH_SHORT).show();
                } else {
                    Usuario cad = new Usuario();
                    cad.setNome(nome.getText().toString());
                    cad.setEmail(email.getText().toString());
                    cad.setTelefone(telefone.getText().toString());
                    cad.setUsuario(usuario.getText().toString());
                    cad.setSenha(senha.getText().toString());
                    cad.setTipo("funcionario");

                    db.inserirUsuario(cad);
                    Toast.makeText(CadastroFuncActivity.this, "Usuário " + cad.getNome() + " cadastrado com sucesso !",
                            Toast.LENGTH_SHORT).show();
                    nome.setText("");
                    email.setText("");
                    telefone.setText("");
                    usuario.setText("");
                    senha.setText("");
                }

            } else
                Toast.makeText(CadastroFuncActivity.this, "Favor preencher todos os dados!", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.button2) {
            new AlertDialog.Builder(this)
                    .setMessage(" -Para navegar entre as telas, utilize as teclas de volume !" +
                            "\n -Para voltar à tela de login, aperte o botão Voltar !")
                    .setPositiveButton("Ok", null)
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Intent intent;
            intent = new Intent(CadastroFuncActivity.this, CadastroProdutoActivity.class);
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja voltar para a tela de login?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent;
                            intent = new Intent(CadastroFuncActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
        return true;
    }
}
