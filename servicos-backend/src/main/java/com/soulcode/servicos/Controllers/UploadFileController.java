package com.soulcode.servicos.Controllers;

import com.soulcode.servicos.Services.FuncionarioServices;
import com.soulcode.servicos.Util.UploadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@CrossOrigin
@RestController
@RequestMapping("servicos")
public class UploadFileController {

    @Autowired
    FuncionarioServices funcionarioServices;

    @PostMapping("/funcionarios/envioFoto/{idFuncionario}")
    public ResponseEntity<Void> enviarFoto(@PathVariable Integer idFuncionario,
                                           MultipartFile file,
                                           @RequestParam("nome") String nome){

        String fileName = nome;
        String uploadDir = "C:/Users/marlo/OneDrive/Documents/fotosfunc";
        String nomeMaisCaminho = "C:/Users/marlo/OneDrive/Documents/fotosfunc/" + nome;

        try{
            UploadFile.saveFile(uploadDir, fileName, file);
            funcionarioServices.salvarFoto(idFuncionario, nomeMaisCaminho);
        } catch (IOException e) {
            System.out.println(("O arquivo não foi enviado: " + e.getMessage()));
        }
        return  ResponseEntity.ok().build();
    }
}
