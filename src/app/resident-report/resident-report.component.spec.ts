import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResidentReportComponent } from './resident-report.component';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatProgressSpinner } from '@angular/material/progress-spinner';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTableModule } from '@angular/material/table';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatTimepickerModule } from '@angular/material/timepicker';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { MatDialog } from '@angular/material/dialog';
import { GetResidentLogResponse } from './resident-report.model';

describe('ResidentReportComponent', () => {
    let httpTesting: HttpTestingController;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ResidentReportComponent,
                RouterLink,
                FormsModule, ReactiveFormsModule, MatProgressSpinner,
                MatFormFieldModule, MatInputModule, MatButtonModule,
                MatTableModule, MatDatepickerModule, MatTimepickerModule,
            ],
            providers: [
                { provide: ActivatedRoute, useValue: {} },
                {
                    provide: MatDialog, useValue: {
                        open: jasmine.createSpy('open').and.returnValue({ afterClosed: () => ({ subscribe: () => { } }) })
                    }
                },
                provideHttpClient(),
                provideHttpClientTesting(),
            ],
        }).compileComponents();

        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterAll(() => {
        httpTesting.verify();
    })

    it('should create', () => {
        const fixture = TestBed.createComponent(ResidentReportComponent);
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        const req = httpTesting.expectOne("http://localhost:8080/neo/residentes/pagos?licensePlate=TEST-123", "Obtener log de pagos");
        const emptyRes: GetResidentLogResponse = {
            resident: {
                licencePlate: "TEST-123",
                accumulatedTime: 0,
                accumulatedRate: 0,
                coveredAmount: 0,
                pendingAmount: 0,
            },
            payments: [],
        };
        req.flush(emptyRes);
        expect(fixture.componentInstance).toBeTruthy();
    });

    it('should redirect', () => {
        const fixture = TestBed.createComponent(ResidentReportComponent);
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        compiled.querySelector('form')?.dispatchEvent(new Event('submit'));
        const req = httpTesting.expectOne("http://localhost:8080/neo/residentes/pagos?licensePlate=TEST-123", "Obtener log de pagos");
        req.flush(
            { errorMessage: "Error de prueba" },
            { status: 404, statusText: "General Not Found Error" });
        expect(fixture.componentInstance.dialog.open).toHaveBeenCalledTimes(1);
    });

    it('should prevent payment', () => {
        const fixture = TestBed.createComponent(ResidentReportComponent);
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        const refreshLogReq = httpTesting.expectOne("http://localhost:8080/neo/residentes/pagos?licensePlate=TEST-123", "Obtener log de pagos");
        const completeRes: GetResidentLogResponse = {
            resident: {
                licencePlate: "TEST-123",
                accumulatedTime: 10000,
                accumulatedRate: 500,
                coveredAmount: 500,
                pendingAmount: 0,
            },
            payments: [
                { id: 1, licensePlate: "TEST-123", paymentAmount: 400, paymentDate: new Date().toISOString() },
                { id: 1, licensePlate: "TEST-123", paymentAmount: 100, paymentDate: new Date().toISOString() }
            ],
        };
        refreshLogReq.flush(completeRes);
        fixture.detectChanges();
        expect(compiled.querySelector('form')).toBeFalsy();
    });

    it('should create payment', () => {
        const fixture = TestBed.createComponent(ResidentReportComponent);
        fixture.componentRef.setInput("licensePlate", "TEST-123");
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        const firstLoadReq = httpTesting.expectOne("http://localhost:8080/neo/residentes/pagos?licensePlate=TEST-123", "Obtener log de pagos");
        const basicRes: GetResidentLogResponse = {
            resident: {
                licencePlate: "TEST-123",
                accumulatedTime: 10000,
                accumulatedRate: 500,
                coveredAmount: 400,
                pendingAmount: 100,
            },
            payments: [
                { id: 1, licensePlate: "TEST-123", paymentAmount: 400, paymentDate: new Date().toISOString() }
            ],
        };
        firstLoadReq.flush(basicRes);
        fixture.detectChanges();
        expect(compiled.querySelector('form')).toBeTruthy();
        fixture.componentInstance.amountFormControl.setValue(100);
        compiled.querySelector('form')?.dispatchEvent(new Event('submit'));
        const paymentReq = httpTesting.expectOne("http://localhost:8080/neo/residentes/pagos", "Registrar pago");
        paymentReq.flush({});
        const refreshLogReq = httpTesting.expectOne("http://localhost:8080/neo/residentes/pagos?licensePlate=TEST-123", "Obtener log de pagos");
        const completeRes: GetResidentLogResponse = {
            resident: {
                licencePlate: "TEST-123",
                accumulatedTime: 10000,
                accumulatedRate: 500,
                coveredAmount: 500,
                pendingAmount: 0,
            },
            payments: [
                { id: 1, licensePlate: "TEST-123", paymentAmount: 400, paymentDate: new Date().toISOString() },
                { id: 1, licensePlate: "TEST-123", paymentAmount: 100, paymentDate: new Date().toISOString() }
            ],
        };
        refreshLogReq.flush(completeRes);
        fixture.detectChanges();
        expect(compiled.querySelector('form')).toBeFalsy();
    });
});
