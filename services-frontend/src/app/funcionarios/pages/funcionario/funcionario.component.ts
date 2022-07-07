import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalDeleteComponent } from '../../components/modal-delete/modal-delete.component';
import { ModalFuncionarioNaoEncontradoComponent } from '../../components/modal-funcionario-nao-encontrado/modal-funcionario-nao-encontrado.component';
import { Funcionarios } from '../../models/funcionarios';
import { FuncionarioService } from '../../services/funcionario.service';

@Component({
  selector: 'app-funcionario',
  templateUrl: './funcionario.component.html',
  styleUrls: ['./funcionario.component.css']
})
export class FuncionarioComponent implements OnInit {

  formFuncionario: FormGroup = this.fb.group({
    nome: ['', [ Validators.required ]],
    email: ['', [ Validators.required, Validators.email ]],
    foto: ['']
  })

  fotoBase: string = '/assets/avatar.webp'
  foto!: File
  fotoPreview: string = ''
  funcionario!: Funcionarios
  desabilitar: boolean = true
  spinner: boolean = true



  constructor(
    private route: ActivatedRoute, // acessar os parâmetros da rota ativa
    private funcService: FuncionarioService,
    private fb: FormBuilder,
    private dialog: MatDialog,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    // let idFuncionario = this.route.snapshot.paramMap.get('idFuncionario')
    
    this.route.paramMap.subscribe(
      (params) => {
        let idFuncionario = parseInt(params.get('idFuncionario') ?? '0' )
        this.recuperarFuncionario(idFuncionario)
        
      }
    )
  }

  recuperarFuncionario(id: number): void {
    this.funcService.getFuncionarioBydId(id).subscribe(
      func => {
        this.funcionario = func
        console.log(this.funcionario);
        this.formFuncionario.setValue({nome: this.funcionario.nome, email: this.funcionario.email, foto: ''})

        this.fotoPreview = func.foto

        this.valorMudou()
        
      },
      (erro: HttpErrorResponse) => {
        if (erro.status == 404) {
          this.dialog.open(ModalFuncionarioNaoEncontradoComponent)
        }
        this.spinner = false
      }
    )
  }

  /* editarFuncionario(id: any, func: Funcionarios): void {
    this.recuperarFuncionario(id)
    this.atualizarFuncionario(func)  
  } */

  recuperarFoto(event: any): void {
    this.foto = event.target.files[0]
    this.carregarPreview()
  }

  carregarPreview(): void {
    const reader = new FileReader() // objeto do js que faz leitura de arquivos

    reader.readAsDataURL(this.foto)

    reader.onload = () => { 
      this.fotoPreview = reader.result as string 
    }
  }

  valorMudou() {
    /* valueChanges é uma propriedade dos FormGroups
    que é um observable que quando um valor do seu formulário
    altera, esse observable te retorna essa modificação */
    this.formFuncionario.valueChanges
    .subscribe(
      /* o parâmetro valores é um objeto que é retornado te informando
      o valor de cada campo do seu reactive forms */
      valores => {
        /* O botão será desabilitado se as validações do formulário estiverem inválidas
        ou se o valor de algum campo do formulário estiver diferente do valor de alguma propriedade do objeto funcionário */
        this.desabilitar = this.formFuncionario.invalid || !(valores.nome != this.funcionario.nome || valores.email != this.funcionario.email || valores.foto.length > 0)
      }
    )
  }

  editarFuncionario(): void {
    this.desabilitar = true
    const f = this.formFuncionario.value
    f.id = this.funcionario.id
    f.foto = this.funcionario.foto
    this.funcService.atualizarFuncionario(f).subscribe(
      sucesso => {
        console.log("Teste");
          this.snackbar.open('Funcionario Atualizado', 'OK', {
            duration: 2000
          })
      },
      erro => {
        console.log(erro); 
      }
    )
  }

}
