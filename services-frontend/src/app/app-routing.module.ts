import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'funcionarios',
    loadChildren: () => import('./funcionarios/funcionarios.module').then(m => m.FuncionariosModule)
    // Lazy Loading
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'funcionarios'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
