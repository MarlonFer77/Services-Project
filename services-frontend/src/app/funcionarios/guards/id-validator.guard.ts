import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class IdValidatorGuard implements CanActivate {

  constructor(
    private router: Router,
    private snackbar: MatSnackBar
  ) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      // isNan() vai te informar se o valor que você passou como parâmetro não é um número
    
      const idFuncionario = Number(route.paramMap.get('idFuncionario') as string)
      

      if (isNaN(idFuncionario)) {
        this.snackbar.open('Valor do Id inválido', 'OK', {
          verticalPosition: 'top',
          duration: 3000
        })

        return this.router.parseUrl('/funcionarios')
      }
      
    return true;
  }
  
}
