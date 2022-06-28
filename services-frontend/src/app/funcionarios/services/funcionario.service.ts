import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Funcionarios } from '../models/funcionarios';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {

  private readonly baseUrl: string = 'http://localhost:3000/funcionarios/'

  constructor(
    private http: HttpClient
  ) { }

  getFuncionario(): Observable<Funcionarios[]> {
    return this.http.get<Funcionarios[]>(this.baseUrl)
  }

  deleteFuncionario(id: number): Observable<Funcionarios>{
    return this.http.delete<any>(`${this.baseUrl}/${id}`)
  }

  // http://localhost:3000/funcionarios/id
  getFuncionarioBydId(id: number): Observable<Funcionarios>{
    return this.http.get<any>(`${this.baseUrl}/${id}`)
  }

  salvarFuncionario(func: Funcionarios): Observable<Funcionarios> {
    return this.http.post<Funcionarios>(this.baseUrl, func)
  }

  atualizarFuncionario(func: Funcionarios): Observable<Funcionarios> {
    return this.http.put<Funcionarios>(`${this.baseUrl}/${func.id}`, func)
  }
}