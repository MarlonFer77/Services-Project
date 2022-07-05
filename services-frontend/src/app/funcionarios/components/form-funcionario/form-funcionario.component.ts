import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
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
    email: ['', [ Validators.required, Validators.email ]],
    foto: ['']
  })

  foto!: File
  fotoPreview: string = ''
  fotoBase: string = 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png'
  constructor(
    private fb: FormBuilder,
    private funcService: FuncionarioService,
    private dialogRef: MatDialogRef<FormFuncionarioComponent>, // objeto que permite controlar o dialog aberto
    private snackBar: MatSnackBar
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
    let obsSalvar: Observable<any>

    if (this.formFuncionario.value.foto.length > 0) {
      obsSalvar = this.funcService.salvarFuncionario(f,this.foto)
    }else{
      obsSalvar = this.funcService.salvarFuncionario(f)
    }

      obsSalvar.subscribe(
        (resultado) => {
          // 1° testar se o resultado é uma Promise ou não
          if (resultado instanceof Promise) {
            // se cair no if, significa que há uma promise e que tem uma foto para salvar
            // 1° Passo - recuperar o observable que me é retornado do primeiro subscribe

            // a função then é executado quando a promise consegue te retornar 
            // os dados com sucesso
            // nesse caso, o dado que será retornado é um observable com
            // o funcionário que foi salvo no banco de dados
            resultado.then((obs$) => {
              // inscrevendo-se no observable que nos retornará o funcionário salvo no banco de dados
              obs$.subscribe(
                () => {
                  this.snackBar.open('Funcionário Enviado', 'Ok', {
                    duration: 2000
                  });
                  this.dialogRef.close()
                }
              )
            })
          }else {
            // se cair no else, significa que o funcionário já foi salva
            // e nao tinha foto para enviar
            this.snackBar.open('Funcionário Enviado', 'Ok', {
              duration: 2000
            });
            this.dialogRef.close()
          }
        }
      )     
  }

  openSnackBar(message: string, action: string) {
    message = 'Enviando Funcionário'
    action = '...'
    this.snackBar.open(message, action);
  }
}