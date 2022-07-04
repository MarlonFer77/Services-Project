import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogClose, MatDialogRef } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Funcionarios } from '../../models/funcionarios';
import { FuncionarioService } from '../../services/funcionario.service';

@Component({
  selector: 'app-funcionario',
  templateUrl: './funcionario.component.html',
  styleUrls: ['./funcionario.component.css']
})
export class FuncionarioComponent implements OnInit {

  formFuncionario: FormGroup = new FormGroup({
    nome: new FormControl('', [ Validators.required ]),
    email: new FormControl('', [ Validators.required, Validators.email ]),
    foto: new FormControl('')
  })

  fotoBase: string = '/assets/avatar.webp'
  foto!: File
  fotoPreview: string = ''
  funcionario!: Funcionarios
  desabilitar: boolean = true



  constructor(
    private route: ActivatedRoute, // acessar os parâmetros da rota ativa
    private funcService: FuncionarioService,
    private fb: FormBuilder
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
      }
    )
  }

  editarFuncionario(func: Funcionarios): any {
    this.funcService.editarFuncionario(func).subscribe(
      (funcs) => {
        this.funcService.atualizarFuncionario(funcs)
        
      }
    )
  }

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

}
