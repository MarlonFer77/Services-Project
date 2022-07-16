import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { User } from '../models/user';
import { JwtHelperService } from "@auth0/angular-jwt"; // pacote auth
@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly baseURL: string = 'http://localhost:8080'
  private jwt = new JwtHelperService() //esse objeto permitirá saber se o token está válido ou não
  constructor( 
    private http: HttpClient,
    private router: Router
  ) { }

  signIn(user: User): Observable<{ Authorization: string }> {
    return this.http.post<{ Authorization: string }>(`${this.baseURL}/login`, user).pipe(
      // tap - passar por algum passo antes de seguir seu fluxo normal
      tap((response) => {
        this.armazenarToken(response.Authorization)
      })
    )
  }

  signOut(): void {
    this.removerToken()
    this.router.navigateByUrl('/auth/login')
  }

  armazenarToken(token: string): void {
    localStorage.setItem('authorization', token)
  }

  removerToken(): void {
    localStorage.removeItem('authorization')
  }

  recuperarToken(): string | null {
    return localStorage.getItem('authorization')
  }

  logado(): boolean {
    // o usuário estará logado se o token estiver armazenado
    // e o token ainda for válido
    const token = this.recuperarToken()

    if (token == null) {
      return false 
    }
    return !this.jwt.isTokenExpired(token)
  }
}