package matheus.bancajornal;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.view.MotionEvent;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login, duvida;
    private EditText user, pass;

    UsuarioDAO db = new UsuarioDAO(LoginActivity.this);
    Usuario us = new Usuario();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.botaoLogin);
        duvida = (Button) findViewById(R.id.button3);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);

        user.setText("", TextView.BufferType.EDITABLE);
        pass.setText("", TextView.BufferType.EDITABLE);

        login.setOnClickListener(LoginActivity.this);
        duvida.setOnClickListener(LoginActivity.this);

//        us.setSenha("1234");
//        us.setUsuario("matheusnno");
//        us.setTelefone("44557544");
//        us.setEmail("matheusnno@gmail.com");
//        us.setNome("Matheus");
//        us.setTipo("root");
//        db.inserirUsuario(us);

        //matheusnno 1234 root
        //fmisiuk 242424 dono
        //buga coleira funcionario
    }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.botaoLogin) {
                long id = 1;

                String useraux = user.getText().toString();
                String passaux = pass.getText().toString();
                if (!useraux.equals("") && !passaux.equals("")) {
                    Usuario aux = db.buscarUsuario(useraux);
                    if ((aux.getUsuario() != "") || !aux.getUsuario().isEmpty() || (aux.getUsuario() != null)) {
                        if (useraux.equals(aux.getUsuario()) && passaux.equals(aux.getSenha())) {
                            Toast.makeText(LoginActivity.this, "Bem vindo, " + aux.getNome() + " !", Toast.LENGTH_SHORT).show();
                            Intent intent;
                            if (aux.getTipo().toLowerCase().equals("root")) {
                                db.close();
                                intent = new Intent(LoginActivity.this, CadastroActivity.class);
                                startActivity(intent);
                            }
                            else if (aux.getTipo().toLowerCase().equals("dono")){
                                db.close();
                                intent = new Intent(LoginActivity.this, CadastroFuncActivity.class);
                                intent.putExtra("codigo", "");
                                startActivity(intent);

                            }
                            else if (aux.getTipo().toLowerCase().equals(("funcionário"))){
                                db.close();
                                intent = new Intent(LoginActivity.this, VendaActivity.class);
                                startActivity(intent);
                            }
                        } else
                            Toast.makeText(LoginActivity.this, "Dados incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(LoginActivity.this, "Digite a senha/usuário", Toast.LENGTH_SHORT).show();
            }
            else if (v.getId() == R.id.button3){
                new AlertDialog.Builder(this)
                        .setMessage("Contatos: " +
                                "\n -Matheus N. N. de Oliveira - matheusnno@gmail.com" +
                                "\n -Felipe Misiuk - felipe.misiuk@gmail.com")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        }

        @Override
        public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja encerrar o aplicativo?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.close();
                            moveTaskToBack(true);
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
}
