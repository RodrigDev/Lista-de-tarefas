package com.rodrigo.listadetarefas.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rodrigo.listadetarefas.Model.Tarefa;
import com.rodrigo.listadetarefas.R;
import com.rodrigo.listadetarefas.helper.TarefaDAO;

public class AdicionarTarefa extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        editTarefa = findViewById(R.id.textTarefa);

        //Recuperar tarefa, caso seja edição
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("Tarefa selecionada");

        //Configurar tarefa na caixa de texto
        if(tarefaAtual != null){
            editTarefa.setText( tarefaAtual.getNomeTarefa() );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menu_salvar :
                TarefaDAO tarefaDao = new TarefaDAO( getApplicationContext() );
                if ( tarefaAtual != null ){ //Edição

                    String nomeTarefa = editTarefa.getText().toString();
                    if ( !nomeTarefa.isEmpty() ) {

                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa( nomeTarefa);
                        tarefa.setId( tarefaAtual.getId() );

                        //atualizar no banco de dados
                        if ( tarefaDao.atualizar( tarefa ) ){
                            finish();
                            Toast.makeText(getApplicationContext(),
                                    "Sucesso ao atualizar tarefa!",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Erro ao atualizar a tarefa!",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }else { //Salvar

                    String nomeTarefa = editTarefa.getText().toString();
                    if ( !nomeTarefa.isEmpty() ){
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa( nomeTarefa );

                        if ( tarefaDao.salvar( tarefa ) ) {
                            finish();
                            Toast.makeText(getApplicationContext(),
                                            "Sucesso ao salvar tarefa!",
                                            Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Sucesso ao salvar tarefa!",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }


                }
            break;
        }

        return super.onOptionsItemSelected(item);
    }
}
