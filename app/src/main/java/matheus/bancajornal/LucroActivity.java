package matheus.bancajornal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by matheusnno on 12/08/16.
 */

public class LucroActivity extends AppCompatActivity implements View.OnClickListener {

    VendasDAO db_vendas = new VendasDAO(LucroActivity.this);
    ProdutosDAO db_produtos = new ProdutosDAO(LucroActivity.this);

    private Button botao, visu_lucro;
    private DatePicker inicio, fim;
    private ListView lista;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucro_parcial_activity);

        botao = (Button) findViewById(R.id.button4);
        visu_lucro = (Button) findViewById(R.id.lucroButton);
        lista = (ListView) findViewById(R.id.listView2);
        total = (TextView)findViewById(R.id.textView8);

        botao.setOnClickListener(LucroActivity.this);
        visu_lucro.setOnClickListener(LucroActivity.this);

        inicio = (DatePicker) findViewById(R.id.datePicker3);
        fim = (DatePicker) findViewById(R.id.datePicker4);

        total.setText("");

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button4) {
            new AlertDialog.Builder(this)
                    .setMessage(" -Para navegar entre as telas, utilize as teclas de volume !" +
                            "\n -Para voltar à tela de login, aperte o botão Voltar !")
                    .setPositiveButton("Ok", null)
                    .show();
        } else if (v.getId() == R.id.lucroButton) {

            total.setText("");

            ArrayAdapter<String> adapter;

            List<String> lista_string = new ArrayList<>();

            adapter = new ArrayAdapter<String>(LucroActivity.this, android.R.layout.simple_list_item_1, lista_string);
            lista.setAdapter(adapter);

            Date data_inicio = new Date();
            data_inicio.setDate(inicio.getDayOfMonth());
            data_inicio.setMonth(inicio.getMonth());
            data_inicio.setYear(inicio.getYear() - 1900);

            Date data_fim = new Date();
            data_fim.setDate(fim.getDayOfMonth());
            data_fim.setMonth(fim.getMonth());
            data_fim.setYear(fim.getYear() - 1900);


            List<Vendas> vend;
            List<Vendas> lucro_parcial = new ArrayList<>();
            vend = db_vendas.getLista();

            List<String> lista_produtos = new ArrayList<>();
            List<Integer> qtde_produtos = new ArrayList<>();
            List<Float> preco_compra_lista = new ArrayList<>();
            List<Float> preco_venda_lista = new ArrayList<>();

            for (int i = 0; i < vend.size(); i++) {
                Calendar c_ini = Calendar.getInstance();
                Calendar c_fim = Calendar.getInstance();
                Calendar c_venda = Calendar.getInstance();

                Date dat_vend = new Date();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String s = vend.get(i).getDataVenda();
                try {
                    dat_vend = df.parse(s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                c_ini.setTime(data_inicio);
                c_ini.set(Calendar.HOUR_OF_DAY, 0);
                c_ini.set(Calendar.MINUTE, 0);
                c_ini.set(Calendar.SECOND, 0);
                c_ini.set(Calendar.MILLISECOND, 0);

                c_fim.setTime(data_fim);
                c_fim.set(Calendar.HOUR_OF_DAY, 23);
                c_fim.set(Calendar.MINUTE, 59);
                c_fim.set(Calendar.SECOND, 59);
                c_fim.set(Calendar.MILLISECOND, 0);

                c_venda.setTime(dat_vend);
                c_venda.set(Calendar.HOUR_OF_DAY, 0);
                c_venda.set(Calendar.MINUTE, 0);
                c_venda.set(Calendar.SECOND, 0);
                c_venda.set(Calendar.MILLISECOND, 0);

                boolean j = (c_ini.before(c_venda) || c_ini.equals(c_venda));
                boolean k = c_fim.after(c_venda);

                //se data_inicio <= dat_vend and data_fim >= dat_vend
                if (j && k) {
                    lucro_parcial.add(vend.get(i));
                    if (!lista_produtos.contains(vend.get(i).getCod_produto())) {
                        lista_produtos.add(vend.get(i).getCod_produto());
                        qtde_produtos.add(vend.get(i).getQuantidade());
                        preco_compra_lista.add(vend.get(i).getPreco_compra());
                        preco_venda_lista.add(vend.get(i).getPreco_individual());
                    }
                    else {
                        for (int cont = 0; cont < qtde_produtos.size(); cont++){
                            if (lista_produtos.get(cont).equals(vend.get(i).getCod_produto())){
                                if (preco_compra_lista.get(cont) != vend.get(i).getPreco_compra()
                                        || preco_venda_lista.get(cont) != vend.get(i).getPreco_individual()){
                                    lista_produtos.add(vend.get(i).getCod_produto());
                                    qtde_produtos.add(vend.get(i).getQuantidade());
                                    preco_compra_lista.add(vend.get(i).getPreco_compra());
                                    preco_venda_lista.add(vend.get(i).getPreco_individual());
                                }
                                else qtde_produtos.set(cont, qtde_produtos.get(cont) + vend.get(i).getQuantidade());
                            }
                        }
                    }
                }
            }

            float receita = 0, despesa = 0, lucro = 0;
            for (int i = 0; i < qtde_produtos.size(); i++){
                receita += qtde_produtos.get(i) * preco_venda_lista.get(i);
                despesa += qtde_produtos.get(i) * preco_compra_lista.get(i);
            }
            lucro = receita - despesa;

            int itens_vendidos = 0;
            if (lucro_parcial.size() != 0) {
                for (int i = 0; i < qtde_produtos.size(); i++){
                    Produtos p = new Produtos();
                    p = db_produtos.buscarProdutos(lista_produtos.get(i));
                    if (p.getId() != null) {
                        lista_string.add(qtde_produtos.get(i) + " " + p.getNome() + " " + " vendido(s) a R$"
                                + String.format("%1$.2f", preco_venda_lista.get(i)) + " cada!\n" +
                                "O preço de custo foi R$" + String.format("%1$.2f", preco_compra_lista.get(i)));
                    }
                    itens_vendidos += qtde_produtos.get(i);
                }
                total.setText("O lucro para esse período é de: R$" + String.format("%1$.2f", lucro)
                                + "\n" + itens_vendidos + " itens foram vendidos!");
                adapter = new ArrayAdapter<String>(LucroActivity.this, android.R.layout.simple_list_item_1, lista_string);
                lista.setAdapter(adapter);
            }
            else Toast.makeText(LucroActivity.this, "Não existem vendas nesse período !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Intent intent = new Intent(LucroActivity.this, RemoverVendaActivity.class);
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja voltar para a tela de login?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent;
                            intent = new Intent(LucroActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
        return true;
    }
}
