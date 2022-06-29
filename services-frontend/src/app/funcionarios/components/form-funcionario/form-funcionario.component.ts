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
    this.funcService.salvarFuncionario(f).subscribe(
      async (func) => {
        // Após salvar os dados básico dos funcionários, vamos salvar a imagem e gerar o link dela
        const link = await this.funcService.uploadImagem(this.foto) // enviando a imagem para o firebase
        // salvando a imagem no firebase e recuperando o link de acesso dela
        func.foto = link // atribuindo o link da imagem ao funcionário
        this.funcService.atualizarFuncionario(func).subscribe(
          (fun) => {
            // Quando a imagem for salva na API, ele mostrará a mensagem do alert e fechará o dialog
            alert("Funcionário salvo com sucesso!")
            this.dialogRef.close() // essa função fecha o dialog pelo typescript
          }
        ) // atualizando o funcionário com a URL da imagem que foi enviada
      }
    )
  }
}
