import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, map, mergeMap, Observable, take, tap } from 'rxjs';
import { Funcionarios } from '../models/funcionarios';
import { AngularFireStorage } from "@angular/fire/compat/storage";// importação do firestorage
import { AuthService } from 'src/app/auth/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class FuncionarioService {
  
  private readonly baseUrl: string = 'http://localhost:8080/servicos/funcionarios'
  public atualizarFuncionariosSub$: BehaviorSubject<boolean> = new BehaviorSubject(true)

  constructor(
    private http: HttpClient,
    private storage: AngularFireStorage, // objeto responsável por salvar os arquivos no firebase
    private authService: AuthService
  ) { }

  getFuncionario(): Observable<Funcionarios[]> {
    return this.http.get<Funcionarios[]>(this.baseUrl)
  }

  deleteFuncionario(func: Funcionarios): Observable<any>{
// se não tiver foto, apenas será deletado o email e nome

    if (func.foto.length > 0) {
      // 1° -> pegar a referência da imagem no fireStorage
      // refFromURL() -> pega a referência do arquivo do storage pelo link de acesso gerado pelo firebase

      return this.storage.refFromURL(func.foto).delete().pipe(
        mergeMap(() => {
          //mergeMap tem a função de pegar dois os mais observables e transformar todos em um só

          return this.http.delete<any>(`${this.baseUrl}/${func.idFuncionario}`)
        }), 
          tap(() => {
            this.atualizarFuncionariosSub$.next(true)
          })
        )
    }
    return this.http.delete<any>(`${this.baseUrl}/${func.idFuncionario}`).pipe(
      tap(() => {
        this.atualizarFuncionariosSub$.next(true)
      })
    )
  }

  // http://localhost:3000/funcionarios/id
  getFuncionarioBydId(id: number): Observable<Funcionarios>{
    return this.http.get<Funcionarios>(`${this.baseUrl}/${id}`)
  }

  // RXJS operators: funções que manipulam os dados que os observables te retornam
  salvarFuncionario(func: Funcionarios, foto?: File) /* foto?: // o "?" deixa o parâmetro opcional */{
    // Fazendo requisição POST para salvar os dados do funcionário
    // @return funcionário que acabou de ser salvo

    // A função pipe para colocar os operadores do RXJS que manipularão
    // os dados que são retornados dos observables 

    // o pipe manipula cada dado que o observable te retorna
    // transformando em algo diferente e te retorna esse dado modificado

    if (foto == undefined) { // se a foto não existe, será retornado um observable que apenas os salva os dados básico
      
      return this.http.post<Funcionarios>(this.baseUrl, func)
    } 
    return this.http.post<Funcionarios>(this.baseUrl, func).pipe(
      map(async (func) => {
        // 1° Passo - fazer upload da imagem e recuperar o link gerado
        const linkFotoFirebase = await this.uploadImagem(foto)
        
        // 2° Passo - atribuir o link gerado ao funcionário criado
        func.foto = linkFotoFirebase

        // 3° Passo - atualizar o funcionário com a foto
        return this.atualizarFuncionario(func)
      })
    )
    
  }

  atualizarFuncionario(func: Funcionarios, foto?: File): any {

    // se a foto não foi passada, atualizar com apenas os dados básicos
    if (foto == undefined) {
      return this.http.put<Funcionarios>(`${this.baseUrl}/${func.idFuncionario}`, func).pipe(
        tap((funcionario) => {
          this.atualizarFuncionariosSub$.next(true)
        })
      )
    }

    // se já existir uma foto ligada a esse funcionário, iremos  deletá-la para pôr a nova
    if (func.foto.length > 0) {
      const inscricao =  this.storage.refFromURL(func.foto).delete().subscribe(
        () => {
          inscricao.unsubscribe()
        }
      )
    }

    return this.http.put<Funcionarios>(`${this.baseUrl}/${func.idFuncionario}`, func).pipe(
      mergeMap(async (funcionarioAtualizado) => {
        const linkFotoFirebase = await this.uploadImagem(foto)

        funcionarioAtualizado.foto = linkFotoFirebase
        return this.atualizarFuncionario(funcionarioAtualizado)
      }),
      tap((funcionario) => {
        this.atualizarFuncionariosSub$.next(true)
      }) 
    )
  }
  //-----------------------------------------//
  // 1° Pegar a imagem
  // 2° Fazer o upload da imagem
  // 3° Gerar o link de download e retorná-lo

  private async uploadImagem(foto: File): Promise<string> {
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