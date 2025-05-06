import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleAddComponent } from './vehicle-add.component';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { GetVehicleResponse } from '../vehicle-detail/vehicle-detail.model';

describe('VehicleAddComponent', () => {
    let httpTesting: HttpTestingController;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            imports: [VehicleAddComponent, MatFormFieldModule, MatInputModule, FormsModule, ReactiveFormsModule, MatButtonModule, MatDialogModule,],
            providers: [
                { provide: MAT_DIALOG_DATA, useValue: { vehicleType: "oficial" } },
                { provide: MatDialogRef, useValue: { close: jasmine.createSpy('close') } },
                provideHttpClient(),
                provideHttpClientTesting(),
            ]
        }).compileComponents();

        httpTesting = TestBed.inject(HttpTestingController);
    });

    afterAll(() => {
        httpTesting.verify();
    });

    it('should create', () => {
        const fixture = TestBed.createComponent(VehicleAddComponent);
        fixture.detectChanges();
        expect(fixture.componentInstance).toBeTruthy();
    });

    it('should fail validation', () => {
        const fixture = TestBed.createComponent(VehicleAddComponent);
        const component = fixture.componentInstance;
        component.licensePlateFormControl.updateValueAndValidity();
        fixture.detectChanges();
        expect(component.licensePlateFormControl.invalid).toBeTruthy();
        expect(component.licensePlateFormControl.hasError('required')).toBeTruthy();
        component.licensePlateFormControl.setValue("TEST");
        component.licensePlateFormControl.updateValueAndValidity();
        fixture.detectChanges();
        expect(component.licensePlateFormControl.invalid).toBeTruthy();
        expect(component.licensePlateFormControl.hasError('minlength')).toBeTruthy();
        component.licensePlateFormControl.setValue("TESTTEST");
        component.licensePlateFormControl.updateValueAndValidity();
        fixture.detectChanges();
        expect(component.licensePlateFormControl.invalid).toBeTruthy();
        expect(component.licensePlateFormControl.hasError('pattern')).toBeTruthy();
        component.licensePlateFormControl.setValue("TEST-123!");
        component.licensePlateFormControl.updateValueAndValidity();
        fixture.detectChanges();
        expect(component.licensePlateFormControl.invalid).toBeTruthy();
        expect(component.licensePlateFormControl.hasError('pattern')).toBeTruthy();
        component.licensePlateFormControl.setValue("TEST-123");
        component.licensePlateFormControl.updateValueAndValidity();
        fixture.detectChanges();
        expect(component.licensePlateFormControl.valid).toBeTruthy();
        expect(component.licensePlateFormControl.errors).toBeFalsy();
    });

    it('should add oficial vehicle', () => {
        const fixture = TestBed.createComponent(VehicleAddComponent);
        const component = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        component.licensePlateFormControl.setValue("TEST-123");
        fixture.detectChanges();
        compiled.querySelector("form")?.dispatchEvent(new Event("submit"));
        const req = httpTesting.expectOne("http://localhost:8080/neo/vehiculos/oficiales");
        req.flush({});
        fixture.detectChanges();
        expect(component.dialogRef.close).toHaveBeenCalled();
    });

    it('should add residente vehicle', () => {
        const fixture = TestBed.createComponent(VehicleAddComponent);
        const component = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        component.data.vehicleType = "residente";
        component.licensePlateFormControl.setValue("TEST-123");
        fixture.detectChanges();
        compiled.querySelector("form")?.dispatchEvent(new Event("submit"));
        const req = httpTesting.expectOne("http://localhost:8080/neo/vehiculos/residentes");
        req.flush({});
        fixture.detectChanges();
        expect(component.dialogRef.close).toHaveBeenCalled();
    });

    it('should add no-residente vehicle', () => {
        const fixture = TestBed.createComponent(VehicleAddComponent);
        const component = fixture.componentInstance;
        const compiled = fixture.nativeElement as HTMLElement;
        component.data.vehicleType = "no-residente";
        component.licensePlateFormControl.setValue("TEST-123");
        fixture.detectChanges();
        compiled.querySelector("form")?.dispatchEvent(new Event("submit"));
        const req = httpTesting.expectOne("http://localhost:8080/neo/vehiculos/no-residentes");
        const vehicleResponse: GetVehicleResponse = {
            licensePlate: "TEST-123",
            activeParking: false,
            vehicleType: {
                vehicleType: "no-residente",
                name: "No-Residente",
            },
        }
        req.flush(vehicleResponse);
        fixture.detectChanges();
        expect(component.dialogRef.close).toHaveBeenCalled();
    });
});
