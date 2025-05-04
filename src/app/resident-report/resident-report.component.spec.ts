import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResidentReportComponent } from './resident-report.component';

describe('ResidentReportComponent', () => {
  let component: ResidentReportComponent;
  let fixture: ComponentFixture<ResidentReportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResidentReportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResidentReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
