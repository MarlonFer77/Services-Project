import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Funcionarios } from '../models/funcionarios';
import { AngularFireStorage } from "@angular/fire/compat/storage";// importação do firestorage

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {

  private readonly baseUrl: string = 'http://localhost:3000/funcionarios/'

  constructor(
    private http: HttpClient,
    private storage: AngularFireStorage // objeto responsável por salvar os arquivos no firebase
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
  //-----------------------------------------//
  // 1° Pegar a imagem
  // 2° Fazer o upload da imagem
  // 3° Gerar o link de download e retorná-lo

  async uploadImagem(foto: File): Promise<string> {
    // a palavra chave async informa que a função vai trabalhar com o código assíncrono, ou seja, códigos que demorar para serem executados 

    const nomeDoArquivo = Date.now() // retorna a data atual em milissegundos

    // faz o upload do arquivo para o firebase
    // 1° Parâmetro: nome do arquivo
    // 2° Parâmetro: o arquivo que deve ser enviado
    // await - palavra chave pra fazer o código esperar até outra coisa ser executada
    const dados =  await this.storage.upload(`${nomeDoArquivo}`, foto)

    // a propriedade ref é a referência do arquivo no firebase
    const downloadURL = await dados.ref.getDownloadURL() // retorna um link pro acesso da imagem 
    
    return downloadURL
  }
}