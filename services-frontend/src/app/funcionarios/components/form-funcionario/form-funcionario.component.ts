import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { Funcionarios } from '../../models/funcionarios';
import { ListarFuncionariosComponent } from '../../pages/listar-funcionarios/listar-funcionarios.component';
import { FuncionarioService } from '../../services/funcionario.service';

@Component({
  selector: 'app-form-funcionario',
  templateUrl: './form-funcionario.component.html',
  styleUrls: ['./form-funcionario.component.css']
})
export class FormFuncionarioComponent implements OnInit {

  formFuncionario: FormGroup = this.fb.group({
    nome: ['', [ Validators.required ]],
    email: ['', [ Validators.required, Validators.email ]]
  })

  foto!: File
  fotoPreview: string = ''

  constructor(
    private fb: FormBuilder,
    private funcService: FuncionarioService,
    private dialogRef: MatDialogRef<FormFuncionarioComponent> // objeto que permite controlar o dialog aberto
  ) { }

  ngOnInit(): void {
  }

  recuperarFoto(event: any): void {
    this.foto = event.target.files[0]
    this.carregarPreview()
  }

  carregarPreview(): void {
    const reader = new FileReader()

    reader.readAsDataURL(this.foto) // leitura do arquivo -> gera um link pra usar ele

    reader.onload = () => { // depois de carregar, executa a função abaixo
      this.fotoPreview = reader.result as string // pega o resultado e transforma em string pra pode usar
    }
  }

  salvar(): void {
    const f: Funcionarios = this.formFuncionario.value
    f.foto = ''

    // iniciando salvamento do funcionário
    this.funcService.salvarFuncionario(f, this.foto).subscribe(
      (dados) => {
        // 1° Passo - recuperar o observable que me é retornado do primeiro subscribe

        // a função then é executado quando a promise consegue te retornar 
        // os dados com sucesso
        // nesse caso, o dado que será retornado é um observable com
        // o funcionário que foi salvo no banco de dados
         dados.then(
          (obs$) => {
            // inscrevendo-se no observable que nos retornará o funcionário salvo no banco de dados
            obs$.subscribe(
              (func) => {
                alert("Funcionário salvo com sucesso")
                this.dialogRef.close(func)
              }
            )
          }
          )
      }
    )
       
  }
}