import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly baseURL: string = 'http://localhost:8080'

  constructor(
    private http: HttpClient
  ) { }

  signIn(user: User): Observable<{ Authorization: string }> {
    return this.http.post<{ Authorization: string }>(`${this.baseURL}/login`, user).pipe(
      // tap - passar por algum passo antes de seguir seu fluxo normal
      tap((response) => {
        this.armazenarToken(response.Authorization)
      })
    )
  }

  armazenarToken(token: string): void {
    localStorage.setItem('authorization', token)
  }

  recuperarToken(): string | null {
    return localStorage.getItem('authorization')
  }
}