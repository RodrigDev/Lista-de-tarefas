package com.rodrigo.listadetarefas.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rodrigo.listadetarefas.Adapter.TarefaAdapter;
import com.rodrigo.listadetarefas.Model.Tarefa;
import com.rodrigo.listadetarefas.R;
import com.rodrigo.listadetarefas.helper.DbHelper;
import com.rodrigo.listadetarefas.helper.RecyclerItemClickListener;
import com.rodrigo.listadetarefas.helper.TarefaDAO;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.DragStartHelper;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Configurar recycle
        recyclerView = findViewById(R.id.recyclerView);

        DbHelper db = new DbHelper( getApplicationContext() );

        ContentValues cv = new ContentValues();
        cv.putNull("nome");

        db.getWritableDatabase().insert("tarefas", null, cv );


        //Adicionar evento de clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //Recuperar a tarefa para editar
                                Tarefa tarefaSelecionada = listaTarefas.get( position );

                                //Enviar tarefa para tela adicionar tarefa
                                Intent intent = new Intent(MainActivity.this, AdicionarTarefa.class);
                                intent.putExtra("Tarefa selecionada", tarefaSelecionada);

                                startActivity( intent );


                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                //Recuperar a tarefa para deletar
                                tarefaSelecionada = listaTarefas.get( position );

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                                //Configura titulo e mensagem
                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir a tarefa: " + tarefaSelecionada.getNomeTarefa() + "?");

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                                        if ( tarefaDAO.deletar(tarefaSelecionada) ){
                                            carregarListaTarefas();
                                            Toast.makeText(getApplicationContext(),
                                                    "Sucesso ao excluir a tarefa!",
                                                    Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getApplicationContext(),
                                                    "Erro ao excluir tarefa!",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                dialog.setNegativeButton("Não", null);

                                //Exibir dialog
                                dialog.create();
                                dialog.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), AdicionarTarefa.class);
                startActivity( intent );

            }
        });
    }

    public void carregarListaTarefas(){

        //Listar tarefas

        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        //Exibi lista de tarefas no Recycle View



        //Configurar adapter
        tarefaAdapter = new TarefaAdapter( listaTarefas );

        //Configurar Recycler View
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter( tarefaAdapter );
    }

    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
