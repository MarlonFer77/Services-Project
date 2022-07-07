import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PodeSairGuard } from './guards/pode-sair.guard';
import { FuncionarioComponent } from './pages/funcionario/funcionario.component';
import { ListarFuncionariosComponent } from './pages/listar-funcionarios/listar-funcionarios.component';

const routes: Routes = [
  {
    path: '',
    //pathMatch - prefix -> pega o final da URL -- se vc n colocar o pathMatch ele é automaticamente prefix -- // full -> pega toda  URL
    component: ListarFuncionariosComponent,
    children: [
      {
        path: ':idFuncionario',
        component: FuncionarioComponent,
        canDeactivate: [
          PodeSairGuard
        ]
        
      } 
    ]
  }
]

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class FuncionariosRoutingModule { }
