import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Funcionarios } from '../../models/funcionarios';
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
    private funcService: FuncionarioService
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
    this.funcService.salvarFuncionario(f).subscribe(
      (func) => {
        console.log(func);
        
      }
    )
  }
}
