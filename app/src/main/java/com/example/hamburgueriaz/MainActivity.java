package com.example.hamburgueriaz;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button mbuttomIncrease, mbuttomDecrease, mEnviarPedido;
    private AutoCompleteTextView mName;
    private TextView mValue, mQuantity, mResumoPedidoBody;
    private CheckBox mBacon, mCheese, mOnionRings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState());
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mbuttomIncrease = findViewById(R.id.buttonIncrease);
        mbuttomDecrease = findViewById(R.id.buttonDecrease);
        mQuantity = findViewById(R.id.QuantityTextView);
        mName = findViewById(R.id.NameTextView);
        mValue = findViewById(R.id.value);
        mBacon = findViewById(R.id.Bacon);
        mCheese = findViewById(R.id.Cheese);
        mOnionRings = findViewById(R.id.OnionRings);
        mResumoPedidoBody = findViewById(R.id.ResumoPedidoBody);
        mEnviarPedido = findViewById(R.id.EnviarPedido);

        // Definir valores iniciais
        mQuantity.setText("0");
        mValue.setText("00.00");

        mbuttomIncrease.setOnClickListener(v -> atualizarQuantidade(1));
        mbuttomDecrease.setOnClickListener(v -> atualizarQuantidade(-1));


        mBacon.setOnCheckedChangeListener((buttonView, isChecked) -> calcularValor());
        mCheese.setOnCheckedChangeListener((buttonView, isChecked) -> calcularValor());
        mOnionRings.setOnCheckedChangeListener((buttonView, isChecked) -> calcularValor());

        mEnviarPedido.setOnClickListener(v -> enviarPedido() );

    }


    private Bundle savedInstanceState() {
        return null;
    };

    private void atualizarQuantidade(int delta) {
        int quantidade = Integer.parseInt(mQuantity.getText().toString());
        quantidade = Math.max(0, quantidade + delta);
        mQuantity.setText(String.valueOf(quantidade));
        calcularValor();
    }

    private void calcularValor() {
        int quantidade = Integer.parseInt(mQuantity.getText().toString());
        float valorBase = quantidade * 20.0f;
        float adicionais = 0.0f;

        if (mBacon.isChecked()) adicionais += 2;
        if (mCheese.isChecked()) adicionais += 2;
        if (mOnionRings.isChecked()) adicionais += 3;

        float total = valorBase + adicionais;
        mValue.setText(String.format("%.2f", total));
    }

    private void enviarPedido(){
        String nomeCliente = mName.getText().toString().trim();
        int quantidade = Integer.parseInt(mQuantity.getText().toString());
        float precoFinal = Float.parseFloat(mValue.getText().toString());

        HashMap<String, Boolean> adicionais = new HashMap<>();
        adicionais.put("Bacon", mBacon.isChecked());
        adicionais.put("Queijo", mCheese.isChecked());
        adicionais.put("Onion Rings", mOnionRings.isChecked());


        String resumo = "Nome do Cliente: " + (nomeCliente.isEmpty() ? "Não informado" : nomeCliente) + "\n"
                + "Tem Bacon? " + (adicionais.get("Bacon") ? "Sim" : "Não") + "\n"
                + "Tem Queijo? " + (adicionais.get("Queijo") ? "Sim" : "Não") + "\n"
                + "Tem Onion Rings? " + (adicionais.get("Onion Rings") ? "Sim" : "Não") + "\n"
                + "Quantidade: " + quantidade + "\n"
                + "Preço Final: R$ " + String.format("%.2f", precoFinal);

        mResumoPedidoBody.setText(resumo);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"allyssoncidade@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Nome do cliente:  " + nomeCliente);
        intent.putExtra(Intent.EXTRA_TEXT, resumo);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
