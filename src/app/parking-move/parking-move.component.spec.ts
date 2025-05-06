import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ParkingMoveComponent } from './parking-move.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef, } from '@angular/material/dialog';
import { MatTimepickerModule } from '@angular/material/timepicker';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

describe('ParkingMoveComponent', () => {
    let component: ParkingMoveComponent;
    let fixture: ComponentFixture<ParkingMoveComponent>;
    let httpTesting: HttpTestingController;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [ParkingMoveComponent,
                FormsModule, ReactiveFormsModule,
                MatFormFieldModule, MatInputModule, MatButtonModule,
                MatDialogModule, MatDatepickerModule, MatTimepickerModule,
            ],
            providers: [
                { provide: MAT_DIALOG_DATA, useValue: {} },
                { provide: MatDialogRef, useValue: { close: jasmine.createSpy('close') } },
                provideHttpClient(),
                provideHttpClientTesting(),
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(ParkingMoveComponent);
        component = fixture.componentInstance;
        httpTesting = TestBed.inject(HttpTestingController);
        fixture.detectChanges();
    });

    afterAll(() => {
        httpTesting.verify();
    })

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should handle error', () => {
        const fixture = TestBed.createComponent(ParkingMoveComponent);
        component.data.type = "entrada";
        component.data.licensePlate = "TEST-123";
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        compiled.querySelector('form')?.dispatchEvent(new Event('submit'));
        const req = httpTesting.expectOne("http://localhost:8080/neo/estancias/entrada", "Registrar entrada del vehiculo");
        req.flush({ errorMessage: "Error de prueba" }, {
            status: 500,
            statusText: "General Error"
        });
        expect(component.dialogRef.close).toHaveBeenCalledTimes(0);
    });

    it('should vehicle enter', () => {
        const fixture = TestBed.createComponent(ParkingMoveComponent);
        component.data.type = "entrada";
        component.data.licensePlate = "TEST-123";
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        compiled.querySelector('form')?.dispatchEvent(new Event('submit'));
        const req = httpTesting.expectOne("http://localhost:8080/neo/estancias/entrada", "Registrar entrada del vehiculo");
        req.flush({});
        expect(component.dialogRef.close).toHaveBeenCalled();
    });

    it('should vehicle exit', () => {
        const fixture = TestBed.createComponent(ParkingMoveComponent);
        component.data.type = "salida";
        component.data.licensePlate = "TEST-123";
        fixture.detectChanges();
        const compiled = fixture.nativeElement as HTMLElement;
        compiled.querySelector('form')?.dispatchEvent(new Event('submit'));
        const req = httpTesting.expectOne("http://localhost:8080/neo/estancias/salida", "Registrar salida del vehiculo");
        req.flush({});
        expect(component.dialogRef.close).toHaveBeenCalled();
    });
});
