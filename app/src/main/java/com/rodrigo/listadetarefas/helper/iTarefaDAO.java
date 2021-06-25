package com.rodrigo.listadetarefas.helper;

import com.rodrigo.listadetarefas.Model.Tarefa;
import java.util.List;

public interface iTarefaDAO {

    public boolean salvar(Tarefa tarefa);
    public boolean atualizar(Tarefa tarefa);
    public boolean deletar(Tarefa tarefa);
    public List<Tarefa> listar();

}
