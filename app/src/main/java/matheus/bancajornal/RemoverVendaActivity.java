package matheus.bancajornal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by matheusnno on 03/08/16.
 */
public class RemoverVendaActivity extends AppCompatActivity implements View.OnClickListener {

    ProdutosDAO db = new ProdutosDAO(RemoverVendaActivity.this);
    VendasDAO db_vendas = new VendasDAO(RemoverVendaActivity.this);

    private Button cancela_venda, button4, pesquisar;
    private DatePicker inicio, fim;
    private EditText cancelar;
    private ListView vendas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venda_activity_remover);

        cancela_venda = (Button) findViewById(R.id.CancVenda);
        vendas = (ListView) findViewById(R.id.listView);
        cancelar = (EditText) findViewById(R.id.cancelar_text);
        button4 = (Button)findViewById(R.id.button4);
        pesquisar = (Button)findViewById(R.id.FiltrarVendas);
        inicio = (DatePicker)findViewById(R.id.datePicker);
        fim = (DatePicker)findViewById(R.id.datePicker2);

        cancela_venda.setOnClickListener(RemoverVendaActivity.this);
        button4.setOnClickListener(RemoverVendaActivity.this);
        pesquisar.setOnClickListener(RemoverVendaActivity.this);

        //ListaVendas();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.CancVenda) {
            if (!cancelar.getText().toString().equals("")) {
                String item_venda = cancelar.getText().toString();
                List<Vendas> vend = new ArrayList<Vendas>();
                vend = db_vendas.buscarVenda(item_venda);

                if (!vend.isEmpty()) {
                    for (int i = 0; i < vend.size(); i++) {
                        String prod = vend.get(i).getCod_produto();

                        Produtos p = new Produtos();
                        p = db.buscarProdutos(prod);
                        if (p.getId() != null) {
                            p.setEstoque(p.getEstoque() + vend.get(i).getQuantidade());
                        }
                        db.acrescentarEstoque(p);
                        db_vendas.deletar(vend.get(i));
                    }
                    cancelar.setText("");
                    Toast.makeText(RemoverVendaActivity.this, "Venda número " + item_venda +
                            " cancelada com sucesso! ", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(RemoverVendaActivity.this, "Venda não existente !", Toast.LENGTH_SHORT).show();
                try {
                    ListaVendas();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else
                Toast.makeText(RemoverVendaActivity.this, "Preencha o número da venda !", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.button4) {
            new AlertDialog.Builder(this)
                    .setMessage(" -Para navegar entre as telas, utilize as teclas de volume !" +
                            "\n -Para voltar à tela de login, aperte o botão Voltar !")
                    .setPositiveButton("Ok", null)
                    .show();
        } else if (v.getId() == R.id.FiltrarVendas){
            try {
                ListaVendas();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            Intent intent;
            intent = new Intent(RemoverVendaActivity.this, VendaActivity.class);
            intent.putExtra("tipo_usuario", "dono");
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            Intent intent;
            intent = new Intent(RemoverVendaActivity.this, LucroActivity.class);
            startActivity(intent);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setMessage("Deseja voltar para a tela de login?")
                    .setCancelable(false)
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent;
                            intent = new Intent(RemoverVendaActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Não", null)
                    .show();
        }
        return true;
    }

    private void ListaVendas() throws ParseException {
        Date data_inicio = new Date();
        data_inicio.setDate(inicio.getDayOfMonth());
        data_inicio.setMonth(inicio.getMonth());
        data_inicio.setYear(inicio.getYear() - 1900);

        Date data_fim = new Date();
        data_fim.setDate(fim.getDayOfMonth());
        data_fim.setMonth(fim.getMonth());
        data_fim.setYear(fim.getYear() - 1900);


        ArrayAdapter<String> adapter;

        List<String> lista_string = new ArrayList<>();

        List<Vendas> vend = new ArrayList<Vendas>();
        vend = db_vendas.getLista();

        for (int i = 0; i < vend.size(); i++) {
            Produtos p = new Produtos();
            Date dat_vend = new Date();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String s = vend.get(i).getDataVenda();
            dat_vend = df.parse(s);

            Calendar c_ini = Calendar.getInstance();
            Calendar c_fim = Calendar.getInstance();
            Calendar c_venda = Calendar.getInstance();

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
            if (j && k){
                p = db.buscarProdutos(vend.get(i).getCod_produto());
                lista_string.add(Integer.toString(vend.get(i).getCod_venda()) + " | " +
                        vend.get(i).getCod_produto() + " | " +
                        p.getNome() + " | " +
                        vend.get(i).getQuantidade() + " | " +
                        vend.get(i).getPreco_individual() + " | " +
                        vend.get(i).getDataVenda());
            }
        }


        adapter = new ArrayAdapter<String>(RemoverVendaActivity.this, android.R.layout.simple_list_item_1, lista_string);
        vendas.setAdapter(adapter);
    }

}
