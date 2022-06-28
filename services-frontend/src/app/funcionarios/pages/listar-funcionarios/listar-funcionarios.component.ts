import {
  Component,
  OnInit
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { FormFuncionarioComponent } from '../../components/form-funcionario/form-funcionario.component';
import {
  Funcionarios
} from '../../models/funcionarios';
import {
  FuncionarioService
} from '../../services/funcionario.service';

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
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.recuperarFuncionario()
  }

  deleteFunc(id: number): void {
    const deletar = confirm("Realmente quer excluir o funcionário?")
    if (deletar) {
      this.funcService.deleteFuncionario(id).subscribe(
        () => {
          this.recuperarFuncionario()
        },
        (erro) => {
          console.log(erro);
        }
      )
    } else {}
  }

  recuperarFuncionario(): void {

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

  abrirFormFuncionario(): void {
    this.dialog.open(FormFuncionarioComponent)
  }

}