import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormFuncionarioComponent } from '../../components/form-funcionario/form-funcionario.component';
import { ModalDeleteComponent } from '../../components/modal-delete/modal-delete.component';
import { Funcionarios } from '../../models/funcionarios';
import { FuncionarioService } from '../../services/funcionario.service';

@Component({
  selector: 'app-listar-funcionarios',
  templateUrl: './listar-funcionarios.component.html',
  styleUrls: ['./listar-funcionarios.component.css']
})
export class ListarFuncionariosComponent implements OnInit {

  funcionarios: Funcionarios[] = []

  colunas: Array < string > = ['id', 'nome', 'email', 'actions']

  constructor(
    private funcService: FuncionarioService,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.funcService.atualizarFuncionariosSub$.subscribe(
      (precisaAtualizar) => {
        if (precisaAtualizar == true) {
          this.recuperarFuncionario() 
        }
      }
    )
  }

  deleteFunc(func: Funcionarios): void {
    const dialogRef = this.dialog.open(ModalDeleteComponent)
    dialogRef.afterClosed().subscribe(
      value => {
        if(value) {
          value = this.funcService.deleteFuncionario(func).subscribe(
            () => {
              this.recuperarFuncionario()
              this.snackbar.open('Funcionário Deletado', 'Ok', {
                duration: 1000
              })
            },
            (erro) => {
              console.log(erro);
            }
          )
        }else{
          value = this.dialog.closeAll()
        }
      }
    )
      
    } 
  

  recuperarFuncionario(): void {

    // 1° sucesso -> retorna os dados
    // 2° erro -> ocorre um erro na fonte de dados
    // 3° complete -> a fonte de dados te retorna tuoo

    this.funcService.getFuncionario().subscribe(
      (funcs) => { // sucesso
        this.funcionarios = funcs.reverse() // reverse- reverterá a array para que na lista os funcionários apareçam do mais novo para o mais antigo
      },
      (erro) => { // erro
        console.log(erro);

      },
      () => { // complete
        console.log('Dados enviados com sucesso');

      }
    )
  }

  abrirFormFuncionario(): void {
    const referenciaDialog = this.dialog.open(FormFuncionarioComponent) // abrindo o formulário do funcionário e recuperando a referência desse componente e guardando ba variável
    referenciaDialog.afterClosed().subscribe(
      () => {
        // a função afterClosed() nos retorna um observable que notifica quando o dialog acabou de ser fechado
        // quando o dialog for fechado, chamaremos a função que faz a requisição dos funcionários novamente
        this.recuperarFuncionario()
      }
    )
  }

}