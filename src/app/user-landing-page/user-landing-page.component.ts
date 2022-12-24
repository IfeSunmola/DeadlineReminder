import {Component, TemplateRef} from '@angular/core';
import {NgbDateStruct, NgbModal} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-user-landing-page',
  templateUrl: './user-landing-page.component.html',
  styleUrls: ['./user-landing-page.component.scss']
})
export class UserLandingPageComponent {
  minDate: any = {year: 0, month: 0, day: 0};
  minTime: any = {hour: 0, minute: 0};
  model: NgbDateStruct | undefined;

  constructor(private modalService: NgbModal) {
  }

  openAddDeadlineModal(content: TemplateRef<NgbDateStruct>) {
    this.minTime = {
      hour: new Date().getHours(),
      minute: new Date().getMinutes()
    }
    this.minDate = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate()
    }
    this.modalService.open(content, {centered: true});
  }
}
