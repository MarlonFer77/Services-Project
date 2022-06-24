import { Component, OnDestroy, OnInit } from '@angular/core';
import { Funcionarios } from '../../models/funcionarios';
import { FuncionarioService } from '../../services/funcionario.service';

@Component({
  selector: 'app-listar-funcionarios',
  templateUrl: './listar-funcionarios.component.html',
  styleUrls: ['./listar-funcionarios.component.css']
})
export class ListarFuncionariosComponent implements OnInit{

  funcionarios: Funcionarios[] = []

  colunas: Array<string> = ['id', 'nome', 'email', 'actions']

  constructor(
    private funcService: FuncionarioService
  ) { }
  
  ngOnInit(): void {
    // 1° sucesso -> retorna os dados
    // 2° erro -> ocorre um erro na fonte de dados
    // 3° complete -> a fonte de dados te retorna tuoo

    this.funcService.getFuncionario().subscribe(
      (funcs) => { // sucesso
        this.funcionarios = funcs
        
      },
      (erro) => { // erro
        console.log(erro);
        
      },
      () => { // complete
        console.log('Dados enviados com sucesso');
        
      }
      )
    }

    deleteFunc(id: number): void {
      this.funcService.deleteFuncionario(id).subscribe(
        (funcs) => {
          alert("Funcionário deletado com sucesso!")
          this.funcService.getNewFunc().subscribe(
            (funcs) => {
              this.funcionarios = funcs
            }
          )
        }
      )
    }
    


}
