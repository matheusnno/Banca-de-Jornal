package matheus.bancajornal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by matheusnno on 03/08/16.
 */
public class VendaActivity extends AppCompatActivity implements View.OnClickListener {

    String codigo_barras = "";
    Produtos existe = new Produtos();
    ProdutosDAO db = new ProdutosDAO(VendaActivity.this);
    VendasDAO db_vendas = new VendasDAO(VendaActivity.this);
    String tipo_user = "";

    private Button novo_produto, finaliza_venda, ler_codigo, duvida, remover;
    private EditText cod, preco_uni, nome, qtde, rem;
    private ListView prod;
    private TextView preco_total;

    private List<Produtos> lista = new ArrayList<Produtos>();
    private List<Integer> qtde_prod = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venda_activity);

        Intent intent = getIntent();
        if (intent.hasExtra("codigo")) {
            codigo_barras = intent.getStringExtra("codigo");
        }
        if (intent.hasExtra("lista_prod")) {
            lista = (List<Produtos>) intent.getSerializableExtra("lista_prod");
        }
        if (intent.hasExtra("lista_qtde")) {
            qtde_prod = (List<Integer>) intent.getSerializableExtra("lista_qtde");
        }
        if (intent.hasExtra("tipo_usuario")) {
            tipo_user = intent.getStringExtra("tipo_usuario");
        }

        novo_produto = (Button) findViewById(R.id.AddProd);
        ler_codigo = (Button) findViewById(R.id.lerCodigo);
        finaliza_venda = (Button) findViewById(R.id.FinaVenda);
        cod = (EditText) findViewById(R.id.codigo_produto);
        prod = (ListView) findViewById(R.id.listView);
        duvida = (Button) findViewById(R.id.button4);
        preco_uni = (EditText) findViewById(R.id.preco_venda_activity);
        nome = (EditText) findViewById(R.id.nome_produto_venda);
        qtde = (EditText) findViewById(R.id.qtdeVenda);
        preco_total = (TextView) findViewById(R.id.textView4);
        remover = (Button)findViewById(R.id.RemoverItem);
        rem = (EditText)findViewById(R.id.cancelar_text);

        preco_total.setText("R$0,00");

        novo_produto.setOnClickListener(VendaActivity.this);
        ler_codigo.setOnClickListener(VendaActivity.this);
        finaliza_venda.setOnClickListener(VendaActivity.this);
        duvida.setOnClickListener(VendaActivity.this);
        remover.setOnClickListener(VendaActivity.this);

        ListaProdutos();


        if (!codigo_barras.equals("")) {
            inserirDados();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.lerCodigo) {
            Intent intent = new Intent(VendaActivity.this, BarCodeActivity.class);
            intent.putExtra("tipo", "venda");
            intent.putExtra("lista_prod", (Serializable) lista);
            intent.putExtra("lista_qtde", (Serializable) qtde_prod);
            intent.putExtra("tipo_usuario", tipo_user);
            startActivity(intent);
        } else if (v.getId() == R.id.AddProd) {
            existe = db.buscarProdutos(cod.getText().toString());
            if (existe.getId() != null) {
                if (qtde.getText().equals("")) {
                    qtde.setText("1");
                }

                int qtde_total = Integer.parseInt(qtde.getText().toString());
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).getCod().equals(cod.getText().toString())) {
                        qtde_total += qtde_prod.get(i);
                    }
                }

                if (qtde_total <= existe.getEstoque()) {
                    lista.add(lista.size(), existe);
                    qtde_prod.add(Integer.parseInt(qtde.getText().toString()));
                    ListaProdutos();
                } else
                    Toast.makeText(VendaActivity.this, "O estoque de " + existe.getNome() + " é de " + existe.getEstoque()
                            + " unidades !", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(VendaActivity.this, "Produto não cadastrado !", Toast.LENGTH_LONG).show();
        } else if (v.getId() == R.id.button4) {
            if (tipo_user.equals("dono")) {
                new AlertDialog.Builder(this)
                        .setMessage(" -Para navegar entre as telas, utilize as teclas de volume !" +
                                "\n -Para voltar à tela de login, aperte o botão Voltar !")
                        .setPositiveButton("Ok", null)
                        .show();
            }
            else {
                new AlertDialog.Builder(this)
                        .setMessage(" -Para voltar à tela de login, aperte o botão Voltar !")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        } else if (v.getId() == R.id.FinaVenda) {
            float preco_total_venda = 0;
            int codigo_venda = db_vendas.maxCodVendas();
            preco_total.setText("R$0,00");

            for (int i = 0; i < lista.size(); i++) {
                Vendas vendas = new Vendas();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String data_venda = df.format(c.getTime());

                Produtos p = new Produtos();
                p = db.buscarProdutos(lista.get(i).getCod());
                p.setEstoque(p.getEstoque() - qtde_prod.get(i));

                preco_total_venda = preco_total_venda + lista.get(i).getPreco_venda() * qtde_prod.get(i);

                vendas.setCategoria(lista.get(i).getCategoria());
                vendas.setDataVenda(data_venda);
                vendas.setQuantidade(qtde_prod.get(i));
                vendas.setCod_produto(lista.get(i).getCod());
                vendas.setPreco_individual(lista.get(i).getPreco_venda());
                vendas.setPreco_total(lista.get(i).getPreco_venda() * qtde_prod.get(i));
                vendas.setCod_venda(codigo_venda + 1);
                vendas.setPreco_compra(lista.get(i).getPreco_compra());

                db_vendas.inserirVenda(vendas);
                db.acrescentarEstoque(p);
            }
            rem.setText("");
            lista.clear();
            qtde_prod.clear();
            ListaProdutos();

            Toast.makeText(VendaActivity.this, String.format("Venda número " + Integer.toString(codigo_venda + 1)
                    + " cadastrada com sucesso! Valor total: R$%1$.2f", preco_total_venda), Toast.LENGTH_LONG).show();
        }
        else if (v.getId() == R.id.RemoverItem) {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja remover o item número " + rem.getText().toString() + " ?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            lista.remove(Integer.parseInt(rem.getText().toString()) - 1);
                            qtde_prod.remove(Integer.parseInt(rem.getText().toString()) - 1);
                            ListaProdutos();
                            rem.setText("");
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) && (tipo_user.equals("dono"))) {
            Intent intent;
            intent = new Intent(VendaActivity.this, CadastroProdutoActivity.class);
            startActivity(intent);
        }
        else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) && (tipo_user.equals("dono"))) {
            Intent intent;
            intent = new Intent(VendaActivity.this, RemoverVendaActivity.class);
            startActivity(intent);
        }
        else if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja voltar para a tela de login?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent;
                            intent = new Intent(VendaActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
        return true;
    }

    private void inserirDados() {
        existe = db.buscarProdutos(codigo_barras);
        if (existe.getId() != null) {
            cod.setText(codigo_barras);
            preco_uni.setText(String.format("R$%1$.2f", existe.getPreco_venda()));
            nome.setText(existe.getNome());
        } else
            Toast.makeText(VendaActivity.this, "Produto não cadastrado !", Toast.LENGTH_LONG).show();
    }

    private void ListaProdutos() {
        ArrayAdapter<String> adapter;

        List<String> lista_string = new ArrayList<>();

        cod.setText("");
        preco_uni.setText("");
        nome.setText("");
        qtde.setText("1");
        float valor_total = 0;

        for (int i = 0; i < lista.size(); i++) {
            valor_total += lista.get(i).getPreco_venda() * qtde_prod.get(i);
            lista_string.add(String.format(Integer.toString(i+1) + " | " + lista.get(i).getCod() + " | " + lista.get(i).getNome() +
                     " | " + qtde_prod.get(i) + " | R$%1$.2f", lista.get(i).getPreco_venda()));
        }
        preco_total.setText(String.format("R$%1$.2f", valor_total));

        adapter = new ArrayAdapter<String>(VendaActivity.this, android.R.layout.simple_list_item_1, lista_string);
        prod.setAdapter(adapter);
    }

}
