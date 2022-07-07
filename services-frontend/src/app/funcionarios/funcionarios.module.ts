import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FuncionariosRoutingModule } from './funcionarios-routing.module';
import { ListarFuncionariosComponent } from './pages/listar-funcionarios/listar-funcionarios.component';
import { MaterialModule } from '../material/material.module';
import { FuncionarioComponent } from './pages/funcionario/funcionario.component';
import { FormFuncionarioComponent } from './components/form-funcionario/form-funcionario.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ModalDeleteComponent } from './components/modal-delete/modal-delete.component';
import { ModalFuncionarioNaoEncontradoComponent } from './components/modal-funcionario-nao-encontrado/modal-funcionario-nao-encontrado.component';
import { ModalRetornoSalvarComponent } from './components/modal-retorno-salvar/modal-retorno-salvar.component';



@NgModule({
  declarations: [
    ListarFuncionariosComponent,
    FuncionarioComponent,
    FormFuncionarioComponent,
    ModalDeleteComponent,
    ModalFuncionarioNaoEncontradoComponent,
    ModalRetornoSalvarComponent
  ],
  imports: [
    CommonModule,
    FuncionariosRoutingModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class FuncionariosModule { }
