import { Component, OnInit } from '@angular/core';
import { FuncionarioService } from '../../services/funcionario.service';

@Component({
  selector: 'app-modal-delete',
  templateUrl: './modal-delete.component.html',
  styleUrls: ['./modal-delete.component.css']
})
export class ModalDeleteComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
