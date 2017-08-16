package matheus.bancajornal;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by matheusnno on 02/08/16.
 */

public class CadastroProdutoActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner categoria;
    private EditText codigo, venda, compra, quantidade, nome;
    private Button cadastrar, le_codigo, duvida, pesquisar;
    private TextView TVcod, TVCat, TVPV, TVPC, TVQTDE, TVNome;

    String codigo_barras = "";

    ProdutosDAO db = new ProdutosDAO(CadastroProdutoActivity.this);
    Produtos existe = new Produtos();
    Produtos pr = new Produtos();
    String[] cat = new String[]{"Jornal", "Revista", "Bomboniere"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_produto_activity);

        Intent intent = getIntent();
        if (intent.hasExtra("codigo")) {
            codigo_barras = intent.getStringExtra("codigo");
        }

        categoria = (Spinner) findViewById(R.id.categoria);
        codigo = (EditText) findViewById(R.id.codigo);
        venda = (EditText) findViewById(R.id.preco_venda);
        compra = (EditText) findViewById(R.id.preco_compra);
        quantidade = (EditText) findViewById(R.id.quantidade);
        cadastrar = (Button) findViewById(R.id.cadastrarProd);
        nome = (EditText) findViewById(R.id.nome_prod);
        le_codigo = (Button) findViewById(R.id.le_codigo);
        TVcod = (TextView) findViewById(R.id.textView_codigo);
        duvida = (Button) findViewById(R.id.button);
        pesquisar = (Button) findViewById(R.id.Pesq_Prod);

        TVCat = (TextView) findViewById(R.id.textView_categoria);
        TVNome = (TextView) findViewById(R.id.textView_nome);
        TVPC = (TextView) findViewById(R.id.textView_compra);
        TVPV = (TextView) findViewById(R.id.textView_venda);
        TVQTDE = (TextView) findViewById(R.id.textView_quantidade);
        TVcod = (TextView) findViewById(R.id.textView_codigo);

        TVCat.setVisibility(View.INVISIBLE);
        TVNome.setVisibility(View.INVISIBLE);
        TVPC.setVisibility(View.INVISIBLE);
        TVPV.setVisibility(View.INVISIBLE);
        TVQTDE.setVisibility(View.INVISIBLE);
        TVcod.setVisibility(View.INVISIBLE);

//        pr.setEstoque(50);
//        pr.setPreco_venda(Float.parseFloat("8.80"));
//        pr.setPreco_compra(Float.parseFloat("3.50"));
//        pr.setCod("123.456");
//        pr.setNome("Jornal Folha de São Paulo");
//        pr.setCategoria("Jornal");
//        db.inserirProduto(pr);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cat);
        categoria.setAdapter(adapter);
        cadastrar.setOnClickListener(CadastroProdutoActivity.this);
        le_codigo.setOnClickListener(CadastroProdutoActivity.this);
        duvida.setOnClickListener(CadastroProdutoActivity.this);
        pesquisar.setOnClickListener(CadastroProdutoActivity.this);

        if (!codigo_barras.equals("")) {
            inserirDados();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cadastrarProd) {
            if (!codigo.getText().toString().equals("") && !venda.getText().toString().equals("")
                    && !compra.getText().toString().equals("") && !quantidade.getText().toString().equals("")
                    && !nome.getText().toString().equals("")) {

                existe = db.buscarProdutos(codigo.getText().toString());
                if (existe.getId() != null) {
                    new AlertDialog.Builder(this)
                            .setMessage("Código já existente! Alterar os dados?")
                            .setCancelable(false)
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Produtos prod = new Produtos();
                                    prod.setCod(codigo.getText().toString());
                                    prod.setPreco_venda(Float.parseFloat(venda.getText().toString()));
                                    prod.setPreco_compra(Float.parseFloat(compra.getText().toString()));
                                    prod.setEstoque(Integer.parseInt(quantidade.getText().toString()));
                                    prod.setCategoria(cat[(int) categoria.getSelectedItemId()].toLowerCase());
                                    prod.setNome(nome.getText().toString());

                                    codigo.setText("");
                                    venda.setText("");
                                    compra.setText("");
                                    quantidade.setText("");
                                    nome.setText("");
                                    categoria.setSelection(1);

                                    db.acrescentarEstoque(prod);
                                    db.close();

                                    Toast.makeText(CadastroProdutoActivity.this, "Estoque alterado com sucesso! " +
                                            "Novo estoque para " + prod.getNome() + " é de "
                                            + prod.getEstoque() + " unidades!", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("Não", null)
                            .show();
                } else {
                    Produtos prod = new Produtos();
                    prod.setCod(codigo.getText().toString());
                    prod.setPreco_venda(Float.parseFloat(venda.getText().toString()));
                    prod.setPreco_compra(Float.parseFloat(compra.getText().toString()));
                    prod.setEstoque(Integer.parseInt(quantidade.getText().toString()));
                    prod.setCategoria(cat[(int) categoria.getSelectedItemId()].toLowerCase());
                    prod.setNome(nome.getText().toString());

                    db.inserirProduto(prod);
                    db.close();
                    Toast.makeText(CadastroProdutoActivity.this, "Código: " + prod.getCod() + ", " +
                            "Produto: " + prod.getNome() + " cadastrado com sucesso !", Toast.LENGTH_LONG).show();
                    codigo.setText("");
                    venda.setText("");
                    compra.setText("");
                    quantidade.setText("");
                    nome.setText("");
                }

            } else
                Toast.makeText(CadastroProdutoActivity.this, "Favor preencher todos os dados!", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.le_codigo) {
            Intent intent = new Intent(CadastroProdutoActivity.this, BarCodeActivity.class);
            intent.putExtra("tipo", "compra");
            startActivity(intent);
        } else if (v.getId() == R.id.button) {
            new AlertDialog.Builder(this)
                    .setMessage(" -Para navegar entre as telas, utilize as teclas de volume !" +
                            "\n -Para voltar à tela de login, aperte o botão Voltar !")
                    .setPositiveButton("Ok", null)
                    .show();
        } else if (v.getId() == R.id.Pesq_Prod) {
            if (!codigo.getText().toString().equals("")) {
                existe = db.buscarProdutos(codigo.getText().toString());
                if (existe.getId() != null) {
                    inserirDadosDigitados(existe);
                } else {
                    Toast.makeText(CadastroProdutoActivity.this, "Produto não cadastrado !", Toast.LENGTH_SHORT).show();
                    venda.setText("");
                    compra.setText("");
                    quantidade.setText("");
                    nome.setText("");
                }
            } else
                Toast.makeText(CadastroProdutoActivity.this, "Digite um código !", Toast.LENGTH_SHORT).show();
        }
    }

    private void inserirDados() {
        existe = db.buscarProdutos(codigo_barras);
        if (existe.getId() != null) {

            TVCat.setVisibility(View.VISIBLE);
            TVNome.setVisibility(View.VISIBLE);
            TVPC.setVisibility(View.VISIBLE);
            TVPV.setVisibility(View.VISIBLE);
            TVQTDE.setVisibility(View.VISIBLE);
            TVcod.setVisibility(View.VISIBLE);

            codigo.setText(codigo_barras);
            nome.setText(existe.getNome());
            venda.setText(Float.toString(existe.getPreco_venda()));
            compra.setText(Float.toString(existe.getPreco_compra()));
            quantidade.setText(Integer.toString(existe.getEstoque()));
            for (int i = 0; i < cat.length; i++) {
                if (existe.getCategoria().toLowerCase().equals(cat[i].toLowerCase())) {
                    categoria.setSelection(i);
                }

            }
        } else {
            codigo.setText(codigo_barras);
            TVcod.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Deseja voltar para a tela de login?")
                .setCancelable(false)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent;
                        intent = new Intent(CadastroProdutoActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            Intent intent;
            intent = new Intent(CadastroProdutoActivity.this, VendaActivity.class);
            intent.putExtra("tipo_usuario", "dono");
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            Intent intent;
            intent = new Intent(CadastroProdutoActivity.this, CadastroFuncActivity.class);
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja voltar para a tela de login?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent;
                            intent = new Intent(CadastroProdutoActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
        return true;
    }

    private void inserirDadosDigitados(Produtos existe) {
        if (existe.getId() != null) {

            TVCat.setVisibility(View.VISIBLE);
            TVNome.setVisibility(View.VISIBLE);
            TVPC.setVisibility(View.VISIBLE);
            TVPV.setVisibility(View.VISIBLE);
            TVQTDE.setVisibility(View.VISIBLE);
            TVcod.setVisibility(View.VISIBLE);

            nome.setText(existe.getNome());
            venda.setText(Float.toString(existe.getPreco_venda()));
            compra.setText(Float.toString(existe.getPreco_compra()));
            quantidade.setText(Integer.toString(existe.getEstoque()));
            for (int i = 0; i < cat.length; i++) {
                if (existe.getCategoria().toLowerCase().equals(cat[i].toLowerCase())) {
                    categoria.setSelection(i);
                }

            }
        } else {
            TVcod.setVisibility(View.VISIBLE);
        }
    }
}
